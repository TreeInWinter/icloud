package com.icloud.framework.thrift.balancing;

public interface Client<T> {
    public T proxy() throws Exception;
    public boolean isHealthy();
}