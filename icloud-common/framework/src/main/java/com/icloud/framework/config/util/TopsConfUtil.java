package com.icloud.framework.config.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.icloud.framework.core.util.TZUtil;

public class TopsConfUtil {

	/**
	 * 获得tops系统的工程名字 譬如: /home/jiangningcui/workspace/tz/tz-common/framework
	 * 需要截取最后一个名字. 1) 需要特别注意.在不同的工程里执行的名字不同,特别是工程继承的时候,需要特别注意使用此方法
	 * 譬如appName继承于appName1,appName1继承appName2
	 * 同一个方法里,在appName2,appName1,appName里分别执行,得出的结果分别是appName2,
	 * appName1,appName.
	 *
	 * 还得注意: 如果是tomcat应用里面的话,可能会有不同哦
	 *
	 */
	public static String getApplicationName() {
		String projectName = System.getProperty("user.dir");
		if (TZUtil.isEmpty(projectName)) {
			throw new RuntimeException();
		}
		if (projectName.lastIndexOf("/") != -1)
			projectName = projectName.substring(projectName.lastIndexOf("/") + 1);
		return projectName;
	}

	/**
	 * 获得主机IP,一般来说,主机会
	 * 这个地方需要修改
	 * @return
	 */
	public static String getLocalIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String projectName = getApplicationName();
		System.out.println("projectName: " + projectName);
		String ip = getLocalIP();
		System.out.println("localIp: " + ip);
	}
}
