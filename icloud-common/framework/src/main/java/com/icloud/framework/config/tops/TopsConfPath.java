package com.icloud.framework.config.tops;

import java.io.File;

import com.icloud.framework.config.tops.TopsConfEnum.ConfLocation;
import com.icloud.framework.config.tops.TopsConfEnum.ConfScope;
import com.icloud.framework.config.tops.util.TopsConfUtil;
import com.icloud.framework.core.util.TZUtil;

public class TopsConfPath {
	public static final String BASE_LOCALHOST_PATH = "/opt/conf/tz-data/";
	public static final String ZKSERVICE_FILENAME = "properties/zkService.properties";

	public static final String ZK_BASE_PATH = "/tops/dev/"; // 这个需要修改啦

	public static String getBaseLocalHostPath() {
		return BASE_LOCALHOST_PATH;
	}

	public static String getBaseZookeeperPath() {
		return ZK_BASE_PATH;
	}

	public static String getBaseHostPath(ConfLocation location) {
		if (TZUtil.isEmpty(location)) {
			throw new RuntimeException();
		}
		if (location == ConfLocation.ZK)
			return getBaseZookeeperPath();
		if (location == ConfLocation.LOCALHOST)
			return getBaseLocalHostPath();
		return getBaseLocalHostPath();
	}

	/**
	 * 必须在文件系统中
	 *
	 * @return
	 */
	public static String getZKservicePath() {
		return getBaseLocalHostPath() + ZKSERVICE_FILENAME;
	}

	public static String getAppConfigPath(String fileName, ConfScope confScope, ConfLocation location) {
		if (TZUtil.isEmpty(fileName) || TZUtil.isEmpty(confScope)) {
			throw new RuntimeException();
		}
		String path = null;
		String appName = TopsConfUtil.getApplicationName();
		switch (confScope) {
		case G: // 全局
				// /opt/conf/tz-data/${appname}/sampleconf.xml
			path = getBaseHostPath(location) + fileName;
			break;
		case M: // /opt/conf/tz-data/${appname}/${ip}/sampleconf.xml
			path = getBaseHostPath(location) + appName + File.separator + TopsConfUtil.getLocalIP() + File.separator + fileName;
			break;
		case GA:

			break;
		case MA:
			break;
		case U:
			break;
		default: // 默认为G
			path = getBaseLocalHostPath() + appName + File.separator + fileName;
			break;
		}
		return path;
	}
}
