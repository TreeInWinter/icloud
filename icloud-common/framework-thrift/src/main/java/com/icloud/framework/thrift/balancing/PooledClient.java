package com.icloud.framework.thrift.balancing;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.StackObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PooledClient<T> implements Client<T> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected Class<? extends T> type;
	private String name;
	private int maxAllowableFailures = 5;
	private long retryInterval = 20000;

	private ObjectPool pool;

	private volatile long wentUnhealthyAt;
	private volatile AtomicInteger numFailures = new AtomicInteger(0);
	private int retryTimes = 0;

	public PooledClient(Class<? extends T> clazz) {

		// log.debug("init PooledClient for: {}", clazz.getName());
		type = clazz;
		// PropertiesConfiguration conf = null;

		// try {
		// conf = new PropertiesConfiguration("properties/poolConf.properties");
		// } catch (Exception e) {
		// logger.error("can not load config: poolConf.properties.");
		// // throw new IllegalStateException();
		// }

		// GenericObjectPool.Config poolConf = new GenericObjectPool.Config();
		// poolConf.lifo = false;
		// poolConf.maxActive = conf.getInt("maxActive", 100);
		// poolConf.maxIdle = conf.getInt("maxIdle", 20);
		// poolConf.maxWait = conf.getLong("maxWait", 10000);

		// pool = new GenericObjectPool(new ConnectionFactory(), poolConf);
		// int maxIdle = conf.getInt("maxIdle", 20);
		int maxIdle = 20;
		pool = new StackObjectPool(new ConnectionFactory(), maxIdle);
		// retryTimes = conf.getInt("retryTime", 3);
		retryTimes = 1;
	}

	public abstract Connection<T> createConnection();

	@Override
	public boolean isHealthy() {
		long now = System.currentTimeMillis();
		if (numFailures.intValue() <= maxAllowableFailures) {
			return true;
		}
		if ((wentUnhealthyAt < now - retryInterval) && (numFailures.intValue() > maxAllowableFailures)) {
			markHealthy();
			return true;
		} else
			return false;
	}

	public void didSucceed() {
		markHealthy();
	}

	public void didFail() {
		if (numFailures.intValue() > maxAllowableFailures) {
			return;
		}
		if (numFailures.incrementAndGet() > maxAllowableFailures)
			markUnhealthy();
	}

	public void markUnhealthy() {
		logger.warn("PooledClient went unhealthy!");
		// System.out.println("PooledClient went unhealthy!");
		wentUnhealthyAt = System.currentTimeMillis();
		logEvent(new UnhealthyEvent(wentUnhealthyAt));
	}

	public void markHealthy() {
		logEvent(new HealthyEvent(System.currentTimeMillis(), wentUnhealthyAt));
		wentUnhealthyAt = -1;
		numFailures.set(0);
	}

	public void logEvent(ClientEvent e) {

	}

	public Connection<T> get() throws Exception {
		// log.debug("get Connection from pool:");
		if (isHealthy()) {
			try {
				return (Connection<T>) pool.borrowObject();
			} catch (java.util.NoSuchElementException e) {
				logger.error(e.getMessage(), e);
				didFail();
				return null;
			}
		} else
			return null;
	}

	public void put(Connection<T> conn) throws Exception {
		// log.debug("return connection to pool.");
		if (conn.didFail || !conn.isHealthy()) {
			didFail();
		} else {
			pool.returnObject(conn);
			didSucceed();
		}
	}

	public void remove(Connection<T> conn) throws Exception {
		// log.debug("return connection to pool.");
		if (conn.didFail || !conn.isHealthy()) {
			didFail();
		} else {
			pool.invalidateObject(conn);
			didSucceed();
		}
	}

	private ThriftInvocation thriftInvocation = new ThriftInvocation() {
		@Override
		public Object doBusiness(Invocation invocation) throws Throwable {
			// log.debug("pool stats: numActive={}, numIdle={}.",
			// pool.getNumActive(), pool.getNumIdle());
			Connection<T> conn = null;
			T client = null;
			try {
				conn = get();
				if (conn == null) {
					didFail();
					throw new ClientUnavailableException();
				}
			} catch (Exception e) {
				logger.error("create connection error.", e);
				didFail();
				throw new ClientUnavailableException();
			}
			// log.debug("conn = " + conn);
			// int i=0;

			// while(i<retryTimes){
			// i++;
			try {
				client = conn.getClient();
				Object res = invocation.doInvoke(client);
				put(conn);
				return res;
			} catch (Throwable e) {
				e.printStackTrace();
				remove(conn);
				conn = get();
			}
			// }
			return null;
		}
	};

	@Override
	public synchronized T proxy() throws Exception {
		try {
			return ClientProxy.apply(type, thriftInvocation);
		} catch (Throwable ex) {
			logger.error(ex.getMessage(), ex);
			throw new Exception(ex);
		}
	}

	class ConnectionFactory implements PoolableObjectFactory {
		@Override
		public Object makeObject() {
			Connection<T> c = createConnection();
			c.ensureOpen();
			return (Object) c;
		}

		@Override
		public boolean validateObject(Object o) {
			return ((Connection<T>) o).isHealthy();
		}

		@Override
		public void destroyObject(Object o) {
			((Connection<T>) o).teardown();
		}

		@Override
		public void activateObject(Object o) {
			((Connection<T>) o).ensureOpen();
		}

		@Override
		public void passivateObject(Object o) {
			((Connection<T>) o).flush();
		}
	}
}

abstract class ClientEvent {
}

class UnhealthyEvent extends ClientEvent {
	public UnhealthyEvent(long time) {

	}
}

class HealthyEvent extends ClientEvent {
	public HealthyEvent(long time, long unhealthyTime) {

	}
}

class TimeoutEvent extends ClientEvent {
	public TimeoutEvent(long time) {

	}
}

class ClientException extends RuntimeException {
}

class ClientUnavailableException extends ClientException {
}

class ClientTimedOutException extends ClientException {
}
