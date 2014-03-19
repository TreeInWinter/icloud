package com.travelzen.framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.api.CuratorWatcher;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.framework.state.ConnectionStateListener;
import com.travelzen.framework.core.dict.DataDict;
import com.travelzen.framework.core.util.StringUtil;
import com.travelzen.framework.util.TZCuratorFrameworkNoProperty;

public class HAPropertiesUtil {
	
	private static Map<String,String> zkMap = new ConcurrentHashMap<String, String>();
	private static Map<String, Properties> propertyFileMap = new ConcurrentHashMap<String, Properties>();
	private static Logger logger = LoggerFactory.getLogger(HAPropertiesUtil.class);
	public static final String ZK_PATH_PREFIX_BIZ = HAPropertiesUtil.getValue("properties/zookeeper-config.properties", "zkpath_prefix_biz", null); 
	//代表该节点在zookeeper上被删除了
	private static final String SPECIAL_VALUE = "NIL";
	static{
		try{
			TZCuratorFrameworkNoProperty.addConnectionStateListener(
					new ConnectionStateListener(){
						
						@Override
						public void stateChanged(CuratorFramework client,
								ConnectionState newState) {
							if(newState == ConnectionState.RECONNECTED){
								try {
									 for(String path : zkMap.keySet()){
										 try{
											 Stat stat = TZCuratorFrameworkNoProperty.getCuratorFramework().checkExists().usingWatcher(new DataWatcher()).forPath(path);
											 if(stat != null){//节点存在，去获取数据
												 byte[] data = TZCuratorFrameworkNoProperty.getCuratorFramework().getData().forPath(path);
												 String value = new String(data, DataDict.CHARACTER_SET_ENCODING_UTF8);
												 zkMap.put(path, value);
											 }
										 }catch(Throwable thr){
											 logger.error("获取zookeeper节点数据异常", thr);
										 }
									 }
								} catch (Exception e) {
									logger.error("", e);
								}
							}
							
						}
						
					}
			);
		}catch(Throwable thr){
			logger.error("", thr);
		}
	}
	/**
	 * 获取参数值。
	 *   1. 先尝试去zookeeper上取zkPath所标识的节点的值，若值不为空，直接返回，否则执行2
	 *   2. 到propertyFilePath配置文件中获取key所对应的value，若值不为空，直接返回， 否则执行3
	 *   3. 若有默认值，返回默认值，否则执行4
	 *   4. 返回 null
	 * @param zkPath  该配置对应的zookeeper节点的路径
	 * @param propertyFilePath 本地配置文件相对路径名，该配置文件会在classpath中查找
	 * @param key 配置的键值
	 * @param defaultValue 默认值
	 * @return
	 */
	public static String getValue(String zkPath, String propertyFilePath, String key, String defaultValue){
	    String value = getValueFromZk(zkPath);
	    if(value != null)
	    	return value.trim();
	    value = getValueFromPropertyFile(propertyFilePath, key);
	    if(value != null)
	    	return value.trim();
	    return defaultValue;
	}
	
	public static String getValue(String propertyFilePath, String key, String defaultValue) {
		return getValue(null, propertyFilePath, key, defaultValue);
	}
	private static String getValueFromPropertyFile(String propertyFilePath, String key) {
		if(StringUtil.isEmpty(propertyFilePath))
			return null;
		propertyFilePath = propertyFilePath.trim();
		if(!propertyFileMap.containsKey(propertyFilePath)){//该配置文件是首次加载
			Properties properties = loadPropertiesFile(propertyFilePath);
			if(propertyFilePath != null && properties != null)
				propertyFileMap.put(propertyFilePath, properties);
		}
		Properties properties = propertyFileMap.get(propertyFilePath);
		if(properties == null)
			return null;
		else return properties.getProperty(key);
	}
	private static String getValueFromZk(String zkPath) {
		if(StringUtil.isEmpty(zkPath))
			return null;
		zkPath = zkPath.trim();
		if(zkMap.containsKey(zkPath)){
			String value = zkMap.get(zkPath);
			if(SPECIAL_VALUE.equals(value))
				return null;
			else return value;
		}else{
			try{
				 Stat stat = TZCuratorFrameworkNoProperty.getCuratorFramework().checkExists().usingWatcher(new DataWatcher()).forPath(zkPath);
				 if(stat != null){//节点存在，去获取数据
					 byte[] data = TZCuratorFrameworkNoProperty.getCuratorFramework().getData().forPath(zkPath);
					 String value = new String(data, DataDict.CHARACTER_SET_ENCODING_UTF8);
					 zkMap.put(zkPath, value);
					 return value;
				 }else {
					 zkMap.put(zkPath, SPECIAL_VALUE);
					 return null;
				 }
			 }catch(Throwable thr){
				 logger.error("获取zookeeper节点数据异常", thr);
				 return null;
			 }
		}
	}
	private static class DataWatcher implements CuratorWatcher{

		@Override
		public void process(WatchedEvent event) throws Exception {
			try {
				String path = StringUtil.trim(event.getPath());
				//重新安装数据变动watcher
				TZCuratorFrameworkNoProperty.getCuratorFramework().checkExists().usingWatcher(new DataWatcher()).forPath(path);
				if(event.getType() == EventType.NodeDataChanged || event.getType() == EventType.NodeCreated){ 
					try{
						byte[] data = TZCuratorFrameworkNoProperty.getCuratorFramework().getData().forPath(path);
						zkMap.put(path, new String(data, DataDict.CHARACTER_SET_ENCODING_UTF8));
					}catch(Throwable thr){
						logger.error("", thr);
					}
				}else if(event.getType() == EventType.NodeDeleted){ //配置不存在了
					zkMap.put(path, SPECIAL_VALUE);
				}
			} catch(Throwable thr){
				logger.error("", thr);
			}
			
		}
	}
	/**
	 * 加载不成功，返回Null
	 * @param filePath
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static Properties loadPropertiesFile(String filePath){
		InputStream in = null;
		JarFile jarFile = null;
		try {
			Properties properties = new Properties();
			URL url = HAPropertiesUtil.class.getClassLoader().getResource(filePath);
			String path = URLDecoder.decode(url.getPath());
			int index = path.indexOf("!/");
			if (index != -1) {
				path = path.substring(0, index);
				jarFile = new JarFile(path.substring(path.indexOf("/")));
				JarEntry jarEntry = jarFile.getJarEntry(filePath);
				in = jarFile.getInputStream(jarEntry);
			} else
				in = new FileInputStream(path);
			properties.load(in);
			in.close();
			return properties;
		} catch (Throwable thr) {
			logger.error("加载配置文件" + filePath + "错误", thr);
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("", e);
				}
				in = null;
			}
			if(jarFile != null){
				try {
					jarFile.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}
	}
}
