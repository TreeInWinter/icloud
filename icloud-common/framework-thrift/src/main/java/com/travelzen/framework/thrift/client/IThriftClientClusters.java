package com.travelzen.framework.thrift.client;


import com.travelzen.framework.thrift.client.ThriftClientContext;

public interface IThriftClientClusters<T extends org.apache.thrift.TServiceClient> {
	/**
	 * 
	 * @param preTcCxt
	 * @return when no cluster ,return null
	 */
	ThriftClientContext<T> getClientContext(ThriftClientContext<T> preTcCxt);

	/**
	 * 
	 * @param tcCxt
	 * @return when no cluster ,return null
	 */
	ThriftClientContext<T> nextClientContext(ThriftClientContext<T> tcCxt);

	void releaseClient(ThriftClientContext<T> tcCxt);

	<P, R> R processWithRetry(Function<P, R> f, P searchReq);
	
}