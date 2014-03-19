package com.travelzen.framework.net.http.entity;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author jianyesun
 * 
 */
public class HttpServerConfig {

	private static Logger logger = LoggerFactory.getLogger(HttpServerConfig.class);
	private String name;

	private int port;

	private Map<String, Context> contexts = new HashMap<String, Context>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Map<String, Context> getContexts() {
		return contexts;
	}

	public Context getContext(String name) {
		return contexts.get(name);
	}

	public void setContext(String name, Context context) {
		this.contexts.put(name, context);
	}

	public void print() {

		logger.debug("name = {}", name);
		logger.debug("port = {}", port);

		for (Context context : contexts.values()) {
			context.print();
		}

	}

}
