package com.icloud.framework.thrift.client;

import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.commons.pool.impl.GenericKeyedObjectPoolFactory;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icloud.framework.thrift.client.ThriftClientPool.ExhaustedAction;

/**
 * Cassandra client multi pool implements, about it's detail behavior please
 * refer to CassandraClinetPool.
 *
 * @author sanli
 * @param <T>
 *
 */
public abstract class ThriftClientMultiPoolImpl<T extends org.apache.thrift.TServiceClient>
		extends BaseThriftClientPool implements ThriftClientMultiPool<T> {

	private static Logger logger = LoggerFactory
			.getLogger(ThriftClientMultiPoolImpl.class);

	/**
	 * the object pool, all client instance managed by this pool
	 */
	GenericKeyedObjectPoolFactory _poolfactory = null;
	GenericKeyedObjectPool _pool = null;
	KeyedPoolableObjectFactory _clientfactory = null;

	/**
	 * Constructor for a multi pool, init parameter is a map from each key to
	 * serviceURL and port. as bellow: { pool1 :
	 *
	 * @param serviceURL
	 * @param port
	 */
	public ThriftClientMultiPoolImpl(Map<String, ClientCluster> clusters) {
		this(clusters, null, DEFAULT_MAX_ACTIVE, DEFAULT_EXHAUSTED_ACTION,
				DEFAULT_MAX_WAITTIME_WHEN_EXHAUSTED, DEFAULT_MAX_IDLE);

	}

	/**
	 * inner class for create and destory Cassandra.Client
	 *
	 * @author dayong
	 */
	public class DefaultKeyedPoolableObjectFactory extends
			BaseKeyedPoolableObjectFactory implements
			KeyedPoolableObjectFactory {

		@Override
		public void destroyObject(Object key, Object obj) throws Exception {
			if (!clusters.containsKey(key)) {
				throw new IllegalArgumentException("invalide cluster name:"
						+ key);
			}

			T client = (T) obj;
			if (logger.isDebugEnabled())
				logger.debug("close client " + client.toString());

			closeClient(client);

		}

		/**
		 * do a simple Cassandra request
		 */
		@Override
		public boolean validateObject(Object key, Object obj) {
			return validateClient(obj);
		}

		@Override
		public Object makeObject(Object key) throws Exception {
			if (!clusters.containsKey(key)) {
				throw new IllegalArgumentException("invalide cluster name:"
						+ key);
			}
			ClientCluster cc = clusters.get(key);

			if (logger.isDebugEnabled())
				logger.debug(
						"create thrift client (url:{} port:{} socketTimeout:{})",
						cc.getServiceURL(), cc.getPort(), cc.getSocketTimeout());

			try {
				return createClient(cc.getServiceURL(), cc.getPort(),
						cc.getSocketTimeout());
			} catch (TTransportException e) {
				logger.error("create client error:", e);
				throw e;
			} catch (TException e) {
				logger.error("create client error:", e);
				throw e;
			}
		}
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
		_pool.setMaxActive(maxActive);
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
		_pool.setMaxIdle(maxIdle);
	}

	public long getMaxWaitWhenBlockByExhausted() {
		return maxWaitWhenBlockByExhausted;
	}

	public void setMaxWaitWhenBlockByExhausted(long maxWaitWhenBlockByExhausted) {
		this.maxWaitWhenBlockByExhausted = maxWaitWhenBlockByExhausted;
		_pool.setMaxWait(maxWaitWhenBlockByExhausted);
	}

	// ************************************ protected method
	// ***********************************

	/**
	 * this construct method wamaxWaitWhenBlockExhausteds for unit test, for
	 * regulary usage, should not given the client factory
	 */
	public ThriftClientMultiPoolImpl(Map<String, ClientCluster> clusters,
			KeyedPoolableObjectFactory clientfactory, int maxActive,
			ExhaustedAction exhaustedAction, long maxWait, int maxIdle) {
		super(maxActive, maxIdle, exhaustedAction, maxWait);
		this.clusters = clusters;

		// if not give a client factory, will create the default
		// PoolableClientFactory
		if (clientfactory == null) {
			this._clientfactory = new DefaultKeyedPoolableObjectFactory();
			_poolfactory = new GenericKeyedObjectPoolFactory(_clientfactory,
					maxActive,
					ThriftClientPoolImpl
							.getObjectPoolExhaustedAction(exhaustedAction),
					maxWait, maxIdle);
		} else {
			_poolfactory = new GenericKeyedObjectPoolFactory(clientfactory,
					maxActive,
					ThriftClientPoolImpl
							.getObjectPoolExhaustedAction(exhaustedAction),
					maxWait, maxIdle);
		}

		_pool = (GenericKeyedObjectPool) _poolfactory.createPool();
		_pool.setTestOnBorrow(true);
	}

	// ---------------------------------------------------------------
	// implements ThriftClientPool
	// ---------------------------------------------------------------

	@Override
	public T getClient(String key) throws Exception, NoSuchElementException,
			IllegalStateException {
		T cc = (T) _pool.borrowObject(key);
		return cc;
	}

	@Override
	public void releaseClient(String key, T client) throws Exception {
		_pool.returnObject(key, client);
	}

	@Override
	public int getAvailableNum() {
		return _pool.getMaxActive() - _pool.getNumActive();
	}

	@Override
	public int getActiveNum(String key) {
		return _pool.getNumActive(key);
	}

	@Override
	public void close() {
		try {
			_pool.close();
		} catch (Exception e) {
			logger.error("close client pool error", e);
		}
	}

	// ********************************** private method
	// *********************************

	private Map<String, ClientCluster> clusters;

	public Map<String, ClientCluster> getClusters() {
		return clusters;
	}

}
