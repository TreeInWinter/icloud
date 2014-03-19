package com.travelzen.framework.net.http.entity;

/**
 * 
 * @author jianyesun
 * 
 */
public class HttpClientConfig {

	private String IP;

	private int port;

	private String target;

	public String getIP() {
		return IP;
	}

	/**
	 * 
	 */
	public HttpClientConfig() {
	}

	public void setIP(String ip) {
		IP = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
