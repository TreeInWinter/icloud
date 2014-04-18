package com.icloud.front.common.utils;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebEnv {

	private static final Logger LOG = LoggerFactory.getLogger(WebEnv.class);

	private static Properties pptConf = null;

	static {
		String path = null;
		try {
			// load default properties
			path = "properties/web-env-default.properties";
			pptConf = new Properties();
			pptConf.load(WebEnv.class.getClassLoader().getResourceAsStream(path));
		} catch (Exception e) {
			LOG.error("load properties(" + path + ") failed." ,e);
		}

		// load properties in current project
		try {
			path = "properties/web-env.properties";
			Properties ppts = new Properties();
			ppts.load(WebEnv.class.getClassLoader().getResourceAsStream(path));
			for (Object oKey : ppts.keySet()) {
				String key = oKey.toString();
				pptConf.setProperty(key, ppts.getProperty(key));
			}
		} catch (Exception e) {
			LOG.warn("load web-env.properties failed, all default env will be kept.");
		}
	}

	public static String get(String key) {
		return pptConf.getProperty(key);
	}

	public static String get(String key, String defaultValue) {
		return pptConf.getProperty(key, defaultValue);
	}

}
