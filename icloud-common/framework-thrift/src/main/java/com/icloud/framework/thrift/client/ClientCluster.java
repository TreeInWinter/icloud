package com.icloud.framework.thrift.client;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ClientCluster {

	String serviceURL;
	int port;
	int socketTimeout;
	String nodeName;


	public ClientCluster(String serviceURL, int port, int socketTimeout) {
		this.serviceURL = serviceURL;
		this.port = port;
		this.socketTimeout = socketTimeout;
		this.nodeName = "";
	}

	public ClientCluster(String serviceURL, int port, int socketTimeout,
			String nodeName) {
		super();
		this.serviceURL = serviceURL;
		this.port = port;
		this.socketTimeout = socketTimeout;
		this.nodeName = nodeName;
	}


	public ClientCluster(String connStr, String nodeName, int socketTimeout) {
		if (connStr != null) {
			String[] tmp = connStr.split(":");
			if (tmp.length == 2) {
				this.serviceURL = tmp[0];
				try {
					this.port = Integer.parseInt(tmp[1]);
				} catch (Exception e) {
					this.port = -1;
				}
			}
		}
		this.nodeName = nodeName;
		this.socketTimeout = socketTimeout;
	}

	public void update(String connStr, String nodeName, int socketTimeout){
		if (connStr != null) {
			String[] tmp = connStr.split(":");
			if (tmp.length == 2) {
				this.serviceURL = tmp[0];
				try {
					this.port = Integer.parseInt(tmp[1]);
				} catch (Exception e) {
					this.port = -1;
				}
			}
		}
		this.nodeName = nodeName;
		this.socketTimeout = socketTimeout;
	}

	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}


	@Override
    public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
