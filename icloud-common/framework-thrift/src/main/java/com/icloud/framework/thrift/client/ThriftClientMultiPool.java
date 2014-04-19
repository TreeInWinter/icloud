/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.icloud.framework.thrift.client;

import java.util.NoSuchElementException;

import org.apache.commons.pool.PoolableObjectFactory;

/**
 * The Cassandra Client multi pool interface. mainly method is : getClient(key)
 * & releaseClient()
 * 
 * code like:
 * 
 * CassandraClient client = clientpool.getClient(key); try{ // do something with
 * client and buessiness logic }catch(Exception e){ // process exception
 * }finally{ clientpool.releaseClient(client); }
 * 
 * @author dayong
 * @version $Revision$ $Date$
 */
public interface ThriftClientMultiPool<ThriftClientType extends org.apache.thrift.TServiceClient> {

	/**
	 * Obtain a client instance from pool, the behavior when there was not extra
	 * idle client was depend on implementations, it can be block, or throw
	 * exceptions(should only throw exceptions, not return null).
	 * 
	 * @return an instance from this pool.
	 * @throws IllegalStateException
	 *             after {@link #close close} has been called on this pool.
	 * @throws Exception
	 *             when {@link PoolableObjectFactory#makeObject makeObject}
	 *             throws an exception.
	 * @throws NoSuchElementException
	 *             when the pool is exhausted and cannot or will not return
	 *             another instance.
	 */
	public ThriftClientType getClient(String key) throws Exception,
			NoSuchElementException, IllegalStateException;;

	/**
	 * return a client to pool, the client must was a instance get from pool,
	 * otherwize will throw out exception
	 * 
	 * @param client
	 */
	public void releaseClient(String key, ThriftClientType client)
			throws Exception;
	

	/**
	 * Return current available client number, this number follow bellow rule:
	 * AvailableNum = MaxNum - UsingNum .
	 * 
	 * @return available client in pool
	 */
	public int getAvailableNum();

	/**
	 * return current in using client number, each time call getClient(), the
	 * value will decrease, call releaseClient will increase value.
	 * 
	 * @return
	 */
	public int getActiveNum(String key);

	/**
	 * Close this pool, and free any resources associated with it. Calling
	 * getClient() or releaseClient() after invoking this method on a pool will
	 * cause them to throw an IllegalStateException.
	 * 
	 * duplication call close will cause nothing. And close() function will keep
	 * silence, in any fail.
	 * 
	 */
	void close();

}
