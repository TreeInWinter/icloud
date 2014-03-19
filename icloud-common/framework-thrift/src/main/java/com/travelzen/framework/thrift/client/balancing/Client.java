package com.travelzen.framework.thrift.client.balancing;

public interface Client<T> {
    public T proxy() throws Exception;
    public boolean isHealthy();
}