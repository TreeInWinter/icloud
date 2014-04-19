package com.icloud.framework.thrift.client;

import org.apache.thrift.TException;


public interface Function<F, T> {
	T apply(  F input, ThriftClientContext cxt)throws TException;
}
