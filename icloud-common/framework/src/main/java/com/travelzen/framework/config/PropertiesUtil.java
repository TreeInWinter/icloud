package com.travelzen.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import com.travelzen.framework.core.util.StringUtil;

public class PropertiesUtil {
	private static Logger logger = LoggerFactory
			.getLogger(PropertiesUtil.class);
	private static Properties properties = new Properties();

	/*
	 * 从消息定义文件中取出code所对应的消息
	 */
	public synchronized static String getValue(String key) {
		return StringUtil.filterNull(properties.getProperty(key)).trim();
	}

	/**
	 * 
	 * description: 从消息定义文件中取出code所对应的消息, args为占位符的实际值
	 * 
	 * @param code
	 * @param args
	 * @return 消息
	 */
	public synchronized static String getValue(String key, Object[] args) {
		return StringUtil.filterNull(MessageFormat.format(getValue(key), args))
				.trim();
	}

	public static String getPropertyFromResource(String resourcePath, String key) {

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();

		Resource rc = ctx.getResource(resourcePath);

		InputStream input = null;
		try {
			input = rc.getInputStream();
		} catch (IOException e1) {
			logger.error(e1.getLocalizedMessage());
			e1.printStackTrace();
			return null;
		}
		if (input != null)
			try {
				Properties properties = new Properties();
				properties.load(input);
				return properties.getProperty(key).trim();
			} catch (IOException e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		return null;
	}

	public static String getProperty(String filePath, String key) {

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();

		Resource rc = ctx.getResource("/" + filePath);

		InputStream input = null;
		try {
			input = rc.getInputStream();
		} catch (IOException e1) {
			logger.error(e1.getLocalizedMessage());
			e1.printStackTrace();
			return null;
		}
		if (input != null)
			try {
				Properties properties = new Properties();
				properties.load(input);
				return StringUtils.trim(properties.getProperty(key));
			} catch (IOException e) {
				logger.error(e.getLocalizedMessage(), e);
			}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, String> mapProperties(String filePath) {

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();

		Resource rc = ctx.getResource("/" + filePath);

		InputStream input = null;
		try {
			input = rc.getInputStream();
		} catch (IOException e1) {
			logger.error(e1.getLocalizedMessage());
			e1.printStackTrace();
			return null;
		}

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
