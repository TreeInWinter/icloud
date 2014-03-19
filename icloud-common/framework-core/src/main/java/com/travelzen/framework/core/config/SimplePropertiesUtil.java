package com.travelzen.framework.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelzen.framework.core.util.TZUtil;

public class SimplePropertiesUtil {
	private static Logger logger = LoggerFactory
			.getLogger(SimplePropertiesUtil.class);

	static Map<String, Properties> propertiesCache = new HashMap<String, Properties>();

	/**
	 * don't strongly support get resource from  jar file
	 * @param filePath
	 * @param key
	 * @return
	 */
	public static String getProperty(String filePath, String key) {

		if (propertiesCache.containsKey(filePath)) {

			String value = propertiesCache.get(filePath).getProperty(key);
			if (null == value) {
				logger.error("no key:{}", value);
				return null;
			} else {
				return value.trim();
			}

		} else {

			InputStream input = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream( filePath);

			if (input != null) {
				try {
					Properties properties = new Properties();
					properties.load(input);

					propertiesCache.put(filePath, properties);

					String value = properties.getProperty(key);
					if (null == value) {
						logger.error("no key:{}", value);
						return null;
					} else {
						return value.trim();
					}

				} catch (IOException e) {
					logger.error(TZUtil.stringifyException(e));
				}
			} else {
				logger.error("invalid path:{}", filePath);
			}
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, String> mapProperties(String filePath) {

		InputStream input = Object.class.getResourceAsStream("/" + filePath);

		Map<String, String> map = new HashMap<String, String>();
		if (input != null)
			try {
				Properties properties = new Properties();
				properties.load(input);
				Enumeration enumeration = properties.propertyNames();
				while (enumeration.hasMoreElements()) {
					String key = (String) enumeration.nextElement();
					map.put(key, properties.getProperty(key));
				}
			} catch (IOException e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		return map;
	}
}
