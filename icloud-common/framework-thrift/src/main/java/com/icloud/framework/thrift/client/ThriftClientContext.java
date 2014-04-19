package com.icloud.framework.thrift.client;

import java.util.Enumeration;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.thrift.TServiceClient;

public class ThriftClientContext<T extends TServiceClient> {
	ClientCluster cluster;
	T client;
	String clusterKey;
	String errMsg;

	Enumeration<String> allClustersKeyCopy;
	boolean isNoCluster = false;

	public ThriftClientContext(T client, String clusterKey,
			ClientCluster cluster) {
		this.client = client;
		this.clusterKey = clusterKey;
		this.cluster = cluster;
	}

	public ThriftClientContext() {
	}

	public T getClient() {
		return client;
	}

	public void setClient(T client) {
		this.client = client;
	}

	public String getClusterKey() {
		return clusterKey;
	}

	public void setClusterKey(String clusterKey) {
		this.clusterKey = clusterKey;
	}

	public Enumeration<String> getAllClustersKeyCopy() {
		return allClustersKeyCopy;
	}

	public void setAllClustersKeyCopy(Enumeration<String> allClustersKeyCopy) {
		this.allClustersKeyCopy = allClustersKeyCopy;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public boolean isNoCluster() {
		return isNoCluster;
	}

	public void setNoCluster(boolean isNoCluster) {
		this.isNoCluster = isNoCluster;
	}

	public ClientCluster getCluster() {
		return cluster;
	}

	public void setCluster(ClientCluster cluster) {
		this.cluster = cluster;
	}
}
