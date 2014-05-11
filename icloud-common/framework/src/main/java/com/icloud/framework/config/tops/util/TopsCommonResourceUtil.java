package com.icloud.framework.config.tops.util;

import com.icloud.framework.config.tops.TopsConfPath;

public class TopsCommonResourceUtil {

	/**
	 * 获得zookeeper的地址
	 */
	public static String getZookeeperService() {
		return TopsPropertiesUtil.getPropertyFromLocalResource(TopsConfPath.getZKservicePath(), "connectionString");
	}

	/**
	 * 获得zookeeper目录中config的基本路径
	 */
	public static String getZookeeperConfigPath() {
		return TopsPropertiesUtil.getPropertyFromLocalResource(TopsConfPath.getZKservicePath(), "zkBasePath", TopsConfPath.getBaseZookeeperPath());
	}

	/**
	 * 获得zookeeper同步的时间
	 */
	public static String getZooKeeperUpdateTime(String timeFile, String key) {
		return TopsPropertiesUtil.getPropertyFromLocalResource(timeFile, key, "19700101000000");
	}

	/**
	 * 保存zookeeper同步时间
	 */
	public static void saveZooKeeperUpdateTime(String timeFile, String key, String value) {
		TopsPropertiesUtil.updatePropretiesFile(timeFile, key, value);
	}

	public static String getBaseLocalHostPath() {
		// TODO Auto-generated method stub
		return TopsConfPath.getBaseLocalHostPath();
	}
}
