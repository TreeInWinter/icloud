package com.icloud.framework.net.http.entity;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author jianyesun
 * 
 */
public class Context {
	private static Logger LOG = LoggerFactory.getLogger(Context.class);
	private String name;

	private String contextpath;

	private String contexthandler;

	private Map<String, Response> responselist = new HashMap<String, Response>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContextpath() {
		return contextpath;
	}

	public void setContextpath(String contextpath) {
		this.contextpath = contextpath;
	}

	public String getContexthandler() {
		return contexthandler;
	}

	public void setContexthandler(String contexthandler) {
		this.contexthandler = contexthandler;
	}

	public Map<String, Response> getResponselist() {
		return responselist;
	}

	public Response getResponse(String command) {
		return responselist.get(command);
	}

	public void setResponse(String command, Response response) {
		this.responselist.put(command, response);
	}

	public void print() {
		LOG.debug(" name= " + name);
		LOG.debug(" contextpath= " + contextpath);
		LOG.debug(" contexthandler= " + contexthandler);

	}

}
