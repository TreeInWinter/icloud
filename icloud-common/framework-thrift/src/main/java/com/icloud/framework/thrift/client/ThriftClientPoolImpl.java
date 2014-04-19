package com.icloud.framework.thrift.client;

import java.util.NoSuchElementException;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPoolFactory;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple client pool, have some configuration item to make it can work under
 * different request. it based on org.apache.commons.pool.GenericObjectPool.
 * 
 * configuration item include: maxActive controls the maximum number of client
 * that can be created by the pool at a given time. When non-positive, there is
 * no limit to the number of client that can be managed by the pool at one time.
 * When maxActive is reached, the pool is said to be exhausted. The default
 * setting for this parameter is 50, this is large number, but should work for
 * us, because cassandra was a server cluster, if we using round-robine DNS LB,
 * and have 10 server in cluster, there was avg 5 connect to each server, that
 * would be not a problem
 * 
 * maxIdle controls the maximum number of objects that can sit idle in the pool
 * at any time. When negative, there is no limit to the number of objects that
 * may be idle at one time. The default setting for this parameter is 5( 1/10 of
 * maxActive, for same the client resource).<br>
 * 
 * exhaustedAction specifies the behavior of the getClinet() method when the
 * pool is exhausted: When exhaustedAction is WHEN_EXHAUSTED_FAIL getClient()
 * will throw a NoSuchElementException
 * 
 * When exhaustedAction WHEN_EXHAUSTED_GROW getClinet will create a new object
 * and return it (essentially making maxActive meaningless.)
 * 
 * When exhaustedAction is WHEN_EXHAUSTED_BLOCK, getClient() will block until a
 * new or idle object is available. If a positive maxWait value is supplied,
 * then getClient will block for at most that many milliseconds, after which a
 * NoSuchElementException will be thrown. If maxWait is non-positive, the
 * getClient() method will block indefinitely.
 * 
 * The default exhaustedAction setting is WHEN_EXHAUSTED_BLOCK} and the default
 * maxWaitWhenBlock setting is 60000. By default, therefore, getClinet() will
 * block 1 minutes, then throw out a exception, if we can not get a connect in 1
 * minutes, there must be something wrong.
 * 
 * 
 * state table: ========= =========== ========= ============ |init(0)| ->
 * getClient -> |maxActive| -> ReleaseClient -> |maxIdle| -> Wait Idle time ->
 * |minIdle(0)| ========= =========== ========= ============ ^ | |
 * |-------------------when new getClient request arrive-----------------|
 * 
 * @author dayong
 * @param <T>
 */
public abstract class ThriftClientPoolImpl<T> extends BaseThriftClientPool
		implements ThriftClientPool<T> {

	private static Logger logger = LoggerFactory
			.getLogger(ThriftClientPoolImpl.class);

	public static final int DEFAULT_CLIENT_SOCKET_TIMEOUT = 500; // 500ms

	/**
	 * the object pool, all client instance managed by this pool
	 */
	GenericObjectPoolFactory _poolfactory = null;
	GenericObjectPool _pool = null;
	PoolableObjectFactory _clientfactory = null;

	public ThriftClientPoolImpl(String serviceURL, int port) {
		this(serviceURL, port, null, DEFAULT_MAX_ACTIVE,
				DEFAULT_EXHAUSTED_ACTION, DEFAULT_MAX_WAITTIME_WHEN_EXHAUSTED,
				DEFAULT_MAX_IDLE, DEFAULT_CLIENT_SOCKET_TIMEOUT);
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
	protected ThriftClientPoolImpl(String serviceURL, int port,
			PoolableObjectFactory clientfactory, int maxActive,
			ExhaustedAction exhaustedAction, long maxWait, int maxIdle,
			int socketTimeout) {
		super(maxActive, maxIdle, exhaustedAction, maxWait);

		// if not give a client factory, will create the default
		// PoolableClientFactory
		if (clientfactory == null) {
			this._clientfactory = new BaseThriftPoolableClientFactory(
					serviceURL, port, socketTimeout);
			_poolfactory = new GenericObjectPoolFactory(_clientfactory,
					maxActive, getObjectPoolExhaustedAction(exhaustedAction),
					maxWait, maxIdle);
		} else {
			_poolfactory = new GenericObjectPoolFactory(clientfactory,
					maxActive, getObjectPoolExhaustedAction(exhaustedAction),
					maxWait, maxIdle);
		}
		_pool = (GenericObjectPool) _poolfactory.createPool();
		_pool.setTestOnBorrow(true);
	}

	public static byte getObjectPoolExhaustedAction(
			ExhaustedAction exhaustedAction) {
		switch (exhaustedAction) {
		case WHEN_EXHAUSTED_FAIL:
			return GenericObjectPool.WHEN_EXHAUSTED_FAIL;
		case WHEN_EXHAUSTED_BLOCK:
			return GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
		case WHEN_EXHAUSTED_GROW:
			return GenericObjectPool.WHEN_EXHAUSTED_GROW;
		default:
			return GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
		}
	}

	class BaseThriftPoolableClientFactory extends BasePoolableObjectFactory
			implements PoolableObjectFactory {

		private String serviceURL;
		private int port;
		private int socketTimeout;// ms

		public BaseThriftPoolableClientFactory(String serviceURL, int port,
				int socketTimeout) {
			super();
			this.serviceURL = serviceURL;
			this.port = port;
			this.socketTimeout = socketTimeout;
		}

		@Override
		public void destroyObject(Object obj) throws Exception {
			T client = (T) obj;
			if (logger.isDebugEnabled())
				logger.debug("close client " + client.toString());

			closeClient(client);
		}

		@Override
		public Object makeObject() throws Exception {
			if (logger.isDebugEnabled())
				logger.debug(
						"create thrift client (url:{} port:{} socketTimeout:{})",
						this.serviceURL, this.port, this.socketTimeout);

			try {
				return createClient(this.serviceURL, this.port,
						this.socketTimeout);
			} catch (TTransportException e) {
				logger.error("create client error:", e);
				throw e;
			} catch (TException e) {
				logger.error("create client error:", e);
				throw e;
			}
		}

		@Override
		public boolean validateObject(Object obj) {
			return validateClient(obj);
		}
	}

	// ---------------------------------------------------------------
	// implements ThriftClientPool
	// ---------------------------------------------------------------

	@Override
	public T getClient() throws Exception, NoSuchElementException,
			IllegalStateException {
		T cc = (T) _pool.borrowObject();
		return cc;
	}

	@Override
	public void releaseClient(T client) throws Exception {
		_pool.returnObject(client);
	}

	@Override
	public int getAvailableNum() {
		return _pool.getMaxActive() - _pool.getNumActive();
	}

	@Override
	public int getActiveNum() {
		return _pool.getNumActive();
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

	private int maxActive;
	private int maxIdle;
	private ExhaustedAction exhaustedAction;
	private long maxWaitWhenBlockByExhausted;

}
