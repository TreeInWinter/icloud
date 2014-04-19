package com.icloud.framework.util;

import java.util.List;

/**
 * TZCuratorFrameworkNoProperty 的扩展，
 * 添加了对排除某些服务node的功能
 * 
 * @author sutao
 * 
 */
public class TZCuratorFrameworkNoPropertyWithExclude {
	/**
	 * 获取可用服务节点list
	 * 
	 * @param servicePath 这个路径上注册着所有可用的节点
	 * @param excludeServicePath 在这个路径上注册着需要排除的服务的节点
	 * @param YRNS_PREFIX 服务注册path的根路径
	 * @return 可用的节点list
	 */
	public static List<String> getRpcAdress(final String servicePath,
			final String excludeServicePath, String YRNS_PREFIX) {

		List<String> availableServiceNodes = TZCuratorFrameworkNoProperty
				.getRpcAdress(servicePath, YRNS_PREFIX);
		if (availableServiceNodes == null) {
			return null;
		}
		
		List<String> excludeServiceNodes = TZCuratorFrameworkNoProperty
				.getRpcAdress(excludeServicePath, YRNS_PREFIX);
		if (excludeServiceNodes != null) {
			availableServiceNodes.removeAll(excludeServiceNodes);
		}
		return availableServiceNodes;
	}
}
