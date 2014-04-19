package com.icloud.framework.thrift.balancing;

public interface ThriftInvocation {
    public Object doBusiness(Invocation invocation) throws Throwable;
}