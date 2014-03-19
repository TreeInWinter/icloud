package com.travelzen.framework.thrift.client.balancing;

public interface ThriftInvocation {
    public Object doBusiness(Invocation invocation) throws Throwable;
}