package com.travelzen.search.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.travelzen.search.config.Config;

/**
 * 
 * @author Jiangning Cui
 * @date 2012-04-10
 * 
 */
public class IndexTimeManagerUtil {

	private static final Log log = LogFactory.getLog(IndexTimeManagerUtil.class);

	public static String getLastMergeDate(String filePath, String key, String defaultValue) {
		if (filePath == null || key == null)
			return null;
		Properties dateProps = new Properties();
		InputStream is;
		try {
			is = new FileInputStream(filePath);
			dateProps.load(is);
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			log.info("该文件不存在");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String value = dateProps.getProperty(key);
		if (value == null)
			value = defaultValue;
		return value;
	}

	public static Properties getProperties(String filePath) {
		Properties dateProps = new Properties();
		if (filePath == null)
			return dateProps;
		InputStream is;
		try {
			is = new FileInputStream(filePath);
			dateProps.load(is);
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateProps;
	}

	public static void storeLastMergeDate(String filePath, String key, String value) {
		if (filePath == null || key == null)
			return;
		try {
			Properties dateProps = getProperties(filePath);
			FileOutputStream fos = new FileOutputStream(filePath);
			dateProps.setProperty(key, value);
			dateProps.store(fos, "");
			fos.flush();
			fos.close();
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	public static void main(String args[]) {
		String date = getLastMergeDate("/data/time.properties", "time", "jiangning");
		System.out.println("it is " + date);
		String path = Config.getItem("dir.time.properties.path", ".");
		storeLastMergeDate("/data/time.properties", "time", "33333");
		date = getLastMergeDate("/data/time.properties", "time", "jiangning");
		System.out.println(date);
	}
}
