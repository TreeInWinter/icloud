package com.icloud.framework.thrift.client;

import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.transport.TTransportException;

import com.icloud.framework.thrift.client.ThriftClientPool.ExhaustedAction;

public abstract class BaseThriftClientPool {

	/**
	 * default max active number is 50, because Cassandra have multiple servers
	 * so 50 should be a good value.
	 */
	public static final int DEFAULT_MAX_ACTIVE = 50;

	/**
	 * default max idle number is 5, so when the client keep idle, the total
	 * connection number will release to 5
	 */
	public static final int DEFAULT_MAX_IDLE = 5;

	/**
	 * default exhausted action is block for 1 minute, because in many time,
	 * cassandra was being used in a web backed, so too long time block will
	 * cause very bad user experience. for some other non-synchronize
	 * application, this value can configure to a more long time.
	 */
	public static final ExhaustedAction DEFAULT_EXHAUSTED_ACTION = ExhaustedAction.WHEN_EXHAUSTED_BLOCK;

	/**
	 * the default max wait time when exhausted happened, default value is 1
	 * minute
	 */
	public static final long DEFAULT_MAX_WAITTIME_WHEN_EXHAUSTED = 3 * 60 * 1000;

	int maxActive;
	int maxIdle;
	ExhaustedAction exhaustedAction;
	long maxWaitWhenBlockByExhausted;

	public BaseThriftClientPool(int maxActive, int maxIdle,
			ExhaustedAction exhaustedAction, long maxWaitWhenBlockByExhausted) {
		super();
		this.maxActive = maxActive;
		this.maxIdle = maxIdle;
		this.exhaustedAction = exhaustedAction;
		this.maxWaitWhenBlockByExhausted = maxWaitWhenBlockByExhausted;
	}
	

	protected abstract boolean validateClient(Object client);

	protected abstract void closeClient(Object client);

	protected abstract TServiceClient createClient(String serviceURL, int port,
			int socketTimeout) throws TTransportException, TException;

}
