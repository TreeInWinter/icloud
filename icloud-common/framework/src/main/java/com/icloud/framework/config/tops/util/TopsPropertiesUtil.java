package com.icloud.framework.config.tops.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icloud.framework.core.util.TZUtil;

/**
 * properties的配置文件缓存模型
 *
 * @author jiangningcui
 *
 */
public class TopsPropertiesUtil {
	private static Logger logger = LoggerFactory.getLogger(TopsPropertiesUtil.class);
	private static Map<String, Properties> mapProperties = new ConcurrentHashMap<String, Properties>();

	/**
	 * 更新缓存
	 *
	 * @param resourcePath
	 * @param properties
	 */
	private static void saveOrUpdateProperties(String resourcePath, Properties properties) {
		if (!TZUtil.isEmpty(resourcePath) && !TZUtil.isEmpty(properties)) {
			mapProperties.put(resourcePath, properties);
		}
	}

	/**
	 * 从inpustrema中获得数据
	 */
	public static String getProperty(String resourcePath, InputStream inputStream, String key, boolean needToCached, String defaultValue) {
		Properties properties = getProperties(resourcePath, inputStream, needToCached);
		return getValue(properties, key, defaultValue);
	}

	/**
	 * 从inpustrema中获得数据
	 */
	public static String getProperty(String resourcePath, byte[] data, String key, boolean needToCached, String defaultValue) {
		if (TZUtil.isEmpty(data))
			return null;
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		Properties properties = getProperties(resourcePath, inputStream, needToCached);
		return getValue(properties, key, defaultValue);
	}

	/**
	 * 从资源文件中获得制定的数据 如果是needToCached的话,需要将其cached
	 *
	 * @param resourcePath
	 * @param key
	 * @param needToCached
	 * @return
	 */
	public static String getPropertyFromLocalResource(String resourcePath, String key, boolean needToCached, String defaultValue) {
		Properties properties = getProperties(resourcePath, needToCached);
		return getValue(properties, key, defaultValue);
	}

	/**
	 * 从资源文件中获得制定的数据 如果是needToCached的话,需要将其cached 不走缓存
	 *
	 * @param resourcePath
	 * @param needToCached
	 * @return
	 */
	public static String getPropertyFromLocalResource(String resourcePath, String key, String defaultValue) {
		return getPropertyFromLocalResource(resourcePath, key, false, defaultValue);
	}

	/**
	 * 从资源文件中获得制定的数据 不走缓存
	 *
	 * @param resourcePath
	 * @param needToCached
	 * @return
	 */
	public static String getPropertyFromLocalResource(String resourcePath, String key) {
		return getPropertyFromLocalResource(resourcePath, key, false, null);
	}

	/**
	 * 保存某个数据到配置文件中
	 */
	public static void updatePropretiesFile(String filePath, String key, String value) {
		if (filePath == null || key == null)
			return;
		try {
			Properties dateProps = getProperties(filePath, false);
			if (dateProps != null) {
				FileOutputStream fos = new FileOutputStream(filePath);
				dateProps.setProperty(key, value);
				dateProps.store(fos, "");
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * loadProperties 注意,获得的不是Java的资源文件,而是外部的文件系统的文件
	 */
	public static Properties getProperties(String resourcePath, boolean needToCached) {
		if (needToCached && mapProperties.containsKey(resourcePath)) {
			return mapProperties.get(resourcePath);
		}
		InputStream input = null;
		try {
			input = new FileInputStream(new File(resourcePath));
		} catch (IOException e1) {
			logger.error(e1.getLocalizedMessage());
			e1.printStackTrace();
			return null;
		}
		if (input != null) {
			try {
				Properties properties = new Properties();
				properties.load(input);
				if (needToCached) {
					saveOrUpdateProperties(resourcePath, properties);
				}
				return properties;
			} catch (IOException e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		}
		return null;
	}

	/**
	 * loadProperties 注意,获得的不是Java的资源文件,而是外部的文件系统的文件
	 */
	public static Properties getProperties(String resourcePath, InputStream input, boolean needToCached) {
		if (input != null) {
			try {
				Properties properties = new Properties();
				properties.load(input);
				if (needToCached) {
					saveOrUpdateProperties(resourcePath, properties);
				}
				return properties;
			} catch (IOException e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		}
		return null;
	}

	/**
	 * 从properties获得制定的值
	 *
	 * @param properties
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getValue(Properties properties, String key, String defaultValue) {
		if (!TZUtil.isEmpty(properties)) {
			String value = properties.getProperty(key);
			if (TZUtil.isEmpty(value))
				value = defaultValue;
			return value;
		}
		return null;
	}

}
