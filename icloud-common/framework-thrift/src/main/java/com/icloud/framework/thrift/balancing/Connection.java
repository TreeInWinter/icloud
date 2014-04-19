package com.icloud.framework.thrift.balancing;

public abstract class Connection<T> {
    public String host;
    public int port;
    public boolean didFail;

    public abstract T getClient() throws Exception;
    public abstract void ensureOpen();
    public abstract void teardown();
    public abstract void flush();

    public boolean isHealthy() {
        return !didFail;
    }

    public void markFailed() {
        didFail = true;
    }
}