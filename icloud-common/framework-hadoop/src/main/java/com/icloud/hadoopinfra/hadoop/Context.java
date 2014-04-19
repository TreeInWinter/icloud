package com.icloud.hadoopinfra.hadoop;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.mapred.Reporter;

public class Context {
	
	public static final String PROPERTIES = "mapred.context.properties";
	
	protected Reporter reporter;
	
	private Map<String, String> properties = new HashMap<String, String>();

	public Map<String, String> getProperties() {
		return new HashMap<String, String>(properties);
	}
	
	public String getProperty(String name) {
		return properties.get(name);
	}

	public void setProperty(String name, String value) {
		this.properties.put(name, value);
	}

	public Reporter getReporter() {
		return reporter;
	}

	public void setReporter(Reporter reporter) {
		this.reporter = reporter;
	}
		

}
