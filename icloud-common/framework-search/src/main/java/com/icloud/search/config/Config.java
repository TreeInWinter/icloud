package com.icloud.search.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Config {

	public static final Log log = LogFactory.getLog(Config.class);
	private static HashMap<String, List<String[]>> commonProps;
	private static Properties props = null;

	static {
		try {
			commonProps = new HashMap<String, List<String[]>>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized static String getItem(String name) {
		if (props == null) {
			props = new Properties();
			InputStream is = Config.class
					.getResourceAsStream("/properties/searchconfig.properties");
			try {
				props.load(is);
			} catch (IOException e) {
				log.error(e, e);
			}
		}
		if (props != null) {
			String val = props.getProperty(name);
			if (val == null) {
				return null;
			} else {
				return val;
			}
		} else {
			return null;
		}
	}

	public static String getItem(String name, String defaultValue) {
		String value = getItem(name);
		if (value == null || value.length() == 0)
			return defaultValue;
		return value;
	}

	public static String getPathItem(String name, boolean isContantsParents) {
		String value = getItem(name);
		if (isContantsParents && value != null) {
			if (value.startsWith("./")) {
				String fatherString = getItem("dir.app");
				if (fatherString != null) {
					value = fatherString + "/" + value;
				}
			}
		}
		return value;
	}

	public static String getPathItem(String name) {
		return getPathItem(name, true);
	}
}
