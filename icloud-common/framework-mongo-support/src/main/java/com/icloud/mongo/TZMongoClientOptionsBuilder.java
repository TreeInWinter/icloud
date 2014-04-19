package com.icloud.mongo;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;

public class TZMongoClientOptionsBuilder {

    private Builder optionsBuilder = new MongoClientOptions.Builder();

    public MongoClientOptions build() {
        return optionsBuilder.build();
    }

    /**
     * 服务器是否自动重连，默认为false
     * */
    public void setAutoConnectRetry(boolean retry) {
        optionsBuilder.autoConnectRetry(retry);
    }

    /**
     * 对同一个服务器尝试重连的时间(毫秒)，默认为0
     */
    public void setMaxAutoConnectRetryTime(long time) {
        optionsBuilder.maxAutoConnectRetryTime(time);
    }

    /**
     * 与每个主机的连接数，默认为100
     */
    public void setConnectionsPerHost(int count) {
        optionsBuilder.connectionsPerHost(count);
    }

    /**
     * 连接超时时间(毫秒)，默认为1000 * 10
     */
    public void setConnectTimeout(int timeout) {
        optionsBuilder.connectTimeout(timeout);
    }

    /**
     * 是否创建一个finalize方法，以便在客户端没有关闭DBCursor的实例时，清理掉它。默认为true
     */
    public void setCursorFinalizerEnabled(boolean enabled) {
        optionsBuilder.cursorFinalizerEnabled(enabled);
    }

    /**
     * 线程等待连接可用的最大时间(毫秒)，默认为1000 * 60 * 2
     */
    public void setMaxWaitTime(int maxWaitTime) {
        optionsBuilder.maxWaitTime(maxWaitTime);
    }

    /**
     * 可等待线程倍数，默认为5。
     * 例如connectionsPerHost最大允许10个连接，则10*5=50个线程可以等待，更多的线程将直接抛异常
     */
    public void setThreadsAllowedToBlockForConnectionMultiplier(int multiplier) {
        optionsBuilder.threadsAllowedToBlockForConnectionMultiplier(multiplier);
    }

    /**
     * socket读写时超时时间(毫秒)，默认为0，不超时
     */
    public void setSocketTimeout(int timeout) {
        optionsBuilder.socketTimeout(timeout);
    }

    /**
     * 是socket连接在防火墙上保持活动的特性，默认为false
     */
    public void setSocketKeepAlive(boolean socketKeepAlive) {
        optionsBuilder.socketKeepAlive(socketKeepAlive);
    }

    /**
     * Sets whether JMX beans registered by the driver should always be MBeans,
     * regardless of whether the VM is Java 6 or greater. If false, the driver
     * will use MXBeans if the VM is Java 6 or greater, and use MBeans if the VM
     * is Java 5.
     * Default: false
     */
    public void setAlwaysUseMBeans(boolean alwaysUseMBeans) {
        optionsBuilder.alwaysUseMBeans(alwaysUseMBeans);
    }

}
