package com.icloud.framework.config.tops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icloud.framework.config.tops.TopsConfEnum.ConfLocation;
import com.icloud.framework.config.tops.TopsConfEnum.ConfScope;
import com.icloud.framework.config.tops.util.TopsPropertiesUtil;
import com.icloud.framework.config.tops.util.ZkPropertiesUtil;
import com.icloud.framework.core.util.TZUtil;

public class TopsConfReader {
	private static Logger logger = LoggerFactory.getLogger(TopsConfReader.class);
	private static String containerPort;

	public TopsConfReader(String appid, String port, String seq) {
		ApplicationMetaInfo metaInfo = new ApplicationMetaInfo(appid, port, seq);
		initContainerPort(metaInfo);
	}

	public void initContainerPort(ApplicationMetaInfo metaInfo) {
		containerPort = metaInfo.getConfirmId();
		if (TZUtil.isEmpty(containerPort)) {
			// 从zookeeper中获得appid

		}
	}

	/**
	 * 从zookeeper进行读取
	 *
	 * @param fileName
	 * @param key
	 * @param scope
	 * @return
	 */
	private String getValueFromZookeeper(String fileName, String key, ConfScope scope) {
		String appConfigPath = TopsConfPath.getAppConfigPath(fileName, scope, ConfLocation.ZK);
		String value = null;
		/**
		 * 从zookeeper中获得
		 */
		byte[] data = ZkPropertiesUtil.getValueFromZk(appConfigPath, true);
		value = TopsPropertiesUtil.getProperty(appConfigPath, data, key, true, null);
		logger.info("getValueFromZookeeper, key : {} ,value : {}", key, value);
		return value;
	}

	/**
	 * 从本地进行读取
	 *
	 * @param fileName
	 * @param key
	 * @param scope
	 * @return
	 */
	private String getValueFromLocalFS(String fileName, String key, ConfScope scope) {
		String appConfigPath = TopsConfPath.getAppConfigPath(fileName, scope, ConfLocation.LOCALHOST);
		String value = TopsPropertiesUtil.getPropertyFromLocalResource(appConfigPath, key, true, null);
		logger.info("getValueFromLocalFS, key : {} ,value : {}", key, value);
		return value;
	}

	/**
	 *
	 * 获得文件的变量
	 *
	 * @param path
	 * @param scope
	 * @return
	 */
	public String getConfContent(String fileName, String key, ConfScope scope) {
		String value = getValueFromZookeeper(fileName, key, scope);
		if (TZUtil.isEmpty(value)) {// 从本地进行读取
			value = getValueFromLocalFS(fileName, key, scope);
		}
		return value;
	}

	/**
	 * 获取全局配置文件的变量
	 *
	 * @param path
	 */
	public String getConfContent(String fileName, String key) {
		return getConfContent(fileName, key, ConfScope.G);
	}

}
