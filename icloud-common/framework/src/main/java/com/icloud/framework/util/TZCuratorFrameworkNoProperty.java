package com.icloud.framework.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icloud.framework.config.PropertiesUtil;
import com.icloud.framework.core.util.RPIDLogger;
import com.icloud.framework.core.util.StringUtil;
import com.icloud.framework.dict.DataDict;
import com.netflix.curator.RetryPolicy;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.recipes.cache.PathChildrenCache;
import com.netflix.curator.framework.recipes.cache.PathChildrenCacheEvent;
import com.netflix.curator.framework.recipes.cache.PathChildrenCacheListener;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.framework.state.ConnectionStateListener;
import com.netflix.curator.retry.ExponentialBackoffRetry;

/**
 * 对curator做的标准化定制。该类实现了单例模式，保证一个进程只有一个zookeeper连接。
 * 
 * @author Jiangning
 * 
 */
public class TZCuratorFrameworkNoProperty {

	private CuratorFramework client = null;

	private static Map<String, Map<String, String>> rpcUrls = new HashMap<String, Map<String, String>>();

	private static Logger logger = LoggerFactory.getLogger(TZCuratorFrameworkNoProperty.class);

	private static class InstanceHolder {
		private static TZCuratorFrameworkNoProperty INSTANCE = new TZCuratorFrameworkNoProperty();
		static {
			INSTANCE.init();
		}
	}

	/**
	 * user this method instead of "YRCuratorFramework.InstanceHolder.INSTANCE"
	 * 
	 * @return
	 */
	public static TZCuratorFrameworkNoProperty getInstance() {
		return InstanceHolder.INSTANCE;
	}

	@SuppressWarnings("static-access")
	private void init() {
		try {
			TZCuratorFrameworkNoProperty.getInstance().addConnectionStateListener(new ConnectionStateListener() {

				@Override
				public void stateChanged(CuratorFramework client, ConnectionState newState) {
					if (newState == ConnectionState.CONNECTED) {
						RPIDLogger.info("zookeeper is connected");
					}
					if (newState == ConnectionState.LOST) {
						RPIDLogger.info("zookeeper is lost");
					}
					if (newState == ConnectionState.RECONNECTED) {
						RPIDLogger.info("zookeeper is reconnected");
					}
					if (newState == ConnectionState.SUSPENDED) {
						RPIDLogger.info("zookeeper is suspended");
					}

				}

			});
		} catch (Throwable thr) {
			logger.error("", thr);
		}
	}

	private TZCuratorFrameworkNoProperty() {
		// String connectionString = System.getenv("TZFS_SERVER");
		String connectionString = PropertiesUtil.getProperty("properties/zkService.properties", "connectionString");
		if (StringUtil.isEmpty(connectionString))
			return;
		connectionString = connectionString.trim();
		try {
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
			client = CuratorFrameworkFactory.newClient(connectionString, 10 * 1000, 10 * 1000, retryPolicy);
			client.start();
		} catch (Exception e) {
			logger.error("创建CuratorFramework时出错", e);
			client = null;
		}
	}

	/**
	 * 获取curatorFramework
	 * 
	 * @return
	 * @throws Exception
	 */
	public static CuratorFramework getCuratorFramework() throws Exception {
		if (InstanceHolder.INSTANCE.client == null)
			throw new IllegalStateException("没有可用的curatorFramework");
		else
			return InstanceHolder.INSTANCE.client;
	}

	/**
	 * 增加连接状态监听器
	 * 
	 * @param listener
	 * @throws Exception
	 */
	public static void addConnectionStateListener(ConnectionStateListener listener) throws Exception {
		if (InstanceHolder.INSTANCE.client == null)
			throw new IllegalStateException("没有可用的curatorFramework");
		InstanceHolder.INSTANCE.client.getConnectionStateListenable().addListener(listener);
	}

	/**
	 * 返回服务的rpc地址，若没有取到则返回null
	 * 
	 * @param rpcServiceName
	 * @return
	 */
	public static List<String> getRpcAdress(final String rpcServiceName, String YRNS_PREFIX) {
		if (StringUtils.isBlank(YRNS_PREFIX) || StringUtils.isBlank(rpcServiceName)) {
			return null;
		}
		List<String> availableServiceNodes = getRpcAdressFromZk(rpcServiceName, YRNS_PREFIX);
		if (availableServiceNodes == null) {
			return null;
		}
		String OFFLINE_YRNS_PREFIX = "/OFFLINE" + YRNS_PREFIX;
		String offline_rpcSErviceName = rpcServiceName + "_offline";
		
		List<String> excludeServiceNodes = getRpcAdressFromZk(offline_rpcSErviceName, OFFLINE_YRNS_PREFIX);
		if (excludeServiceNodes != null) {
			availableServiceNodes.removeAll(excludeServiceNodes);
		}
		return availableServiceNodes;
	}

	/**
	 * 
	 * @param rpcServiceName
	 * @param YRNS_PREFIX
	 * @return
	 */
	private static List<String> getRpcAdressFromZk(final String rpcServiceName, String YRNS_PREFIX) {
		if (StringUtils.isBlank(YRNS_PREFIX)) {
			return null;
		}
		YRNS_PREFIX = YRNS_PREFIX.toUpperCase();
		if (InstanceHolder.INSTANCE.client == null)
			return null;
		if (rpcUrls.containsKey(rpcServiceName)) {
			if (rpcUrls.get(rpcServiceName) != null)
				return new ArrayList<String>(rpcUrls.get(rpcServiceName).values());
			else
				return null;
		}
		synchronized (TZCuratorFrameworkNoProperty.class) {
			if (rpcUrls.containsKey(rpcServiceName)) {
				if (rpcUrls.get(rpcServiceName) != null)
					return new ArrayList<String>(rpcUrls.get(rpcServiceName).values());
				else
					return null;
			}
			try {
				rpcUrls.put(rpcServiceName, new ConcurrentHashMap<String, String>());
				PathChildrenCache shardListener = new PathChildrenCache(InstanceHolder.INSTANCE.client, YRNS_PREFIX + "/" + rpcServiceName, true);
				// System.out.println(YRNS_PREFIX + "/" + rpcServiceName);
				shardListener.getListenable().addListener(new PathChildrenCacheListener() {
					@Override
					public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
						if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
							String shardNodePath = StringUtil.trim(event.getData().getPath());
							PathChildrenCache replicaListener = new PathChildrenCache(InstanceHolder.INSTANCE.client, shardNodePath + "/rpc", true);
							replicaListener.getListenable().addListener(new PathChildrenCacheListener() {
								@Override
								public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
									if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED
											|| event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED) {
										String replicaNodePath = StringUtil.trim(event.getData().getPath());
										String replicaRpcUrl = StringUtil.trim(new String(event.getData().getData(), DataDict.CHARACTER_SET_ENCODING_UTF8));
										rpcUrls.get(rpcServiceName).put(replicaNodePath, replicaRpcUrl);
									} else if (event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
										String replicaNodePath = StringUtil.trim(event.getData().getPath());
										rpcUrls.get(rpcServiceName).remove(replicaNodePath);
									}
								}
							});
							replicaListener.start();
						}
					}

				});
				shardListener.start();
				// 首次访问这个服务，等待3秒钟
				Thread.sleep(8000);
				return getRpcAdressFromZk(rpcServiceName, YRNS_PREFIX);
			} catch (Throwable thr) {
				logger.error("", thr);
				rpcUrls.put(rpcServiceName, null);
				return null;
			}

		}
	}

	/**
	 * 注册rpc服务的url
	 * 
	 * @param rpcUrl
	 */
	public static void registerRpc(final String rpcUrl, final String YRNS_PREFIX, final String serviceName, final String shardId, final String replicaId)
			throws Exception {
		try {
			if (StringUtils.isBlank(YRNS_PREFIX) || StringUtils.isBlank(serviceName) || StringUtils.isBlank(shardId) || StringUtils.isBlank(replicaId))
				throw new Exception("cannot identify (serviceName, shardId, replicaId)");
			createRpcNodeAtZK(rpcUrl, YRNS_PREFIX.toUpperCase(), serviceName, shardId, replicaId);
			addConnectionStateListener(new ConnectionStateListener() {

				@Override
				public void stateChanged(CuratorFramework client, ConnectionState newState) {
					if (newState == ConnectionState.RECONNECTED) {
						try {
							createRpcNodeAtZK(rpcUrl, YRNS_PREFIX.toUpperCase(), serviceName, shardId, replicaId);
						} catch (Exception e) {
							logger.error("", e);
						}
					}

				}

			});
		} catch (Exception e) {
			throw e;
		}

	}

	private static void createRpcNodeAtZK(String rpcUrl, final String YRNS_PREFIX, final String serviceName, final String shardId, final String replicaId)
			throws Exception {
		String rpcNodePath = YRNS_PREFIX + "/" + serviceName + "/" + shardId + "/rpc/" + replicaId;
		// 在前一个session超时前，创建一个临时节点可能会失败，因为前一个session创建的同一个节点可能还存在。保险起见，先删除，后创建。
		try {
			getCuratorFramework().delete().forPath(rpcNodePath);
		} catch (Throwable thr) {

		}
		getCuratorFramework().create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
				.forPath(rpcNodePath, rpcUrl.getBytes(DataDict.CHARACTER_SET_ENCODING_UTF8));
		logger.info("注册rpc地址:" + rpcUrl + "到zookeeper路径:" + rpcNodePath + "成功");
		System.out.println("注册rpc地址:" + rpcUrl + "到zookeeper路径:" + rpcNodePath + "成功");
	}

	/**
	 * 注册监控服务的url
	 * 
	 * @param monitorUrl
	 */
	public static void registerMonitor(final String monitorUrl, final String YRNS_PREFIX, final String serviceName, final String shardId, final String replicaId)
			throws Exception {
		try {
			if (StringUtils.isBlank(YRNS_PREFIX) || StringUtils.isBlank(serviceName) || StringUtils.isBlank(shardId) || StringUtils.isBlank(replicaId))
				throw new Exception("cannot identify (serviceName, shardId, replicaId)");
			createMonitorNodeAtZK(monitorUrl, YRNS_PREFIX, serviceName, shardId, replicaId);
			addConnectionStateListener(new ConnectionStateListener() {

				@Override
				public void stateChanged(CuratorFramework client, ConnectionState newState) {
					if (newState == ConnectionState.RECONNECTED) {
						try {
							createMonitorNodeAtZK(monitorUrl, YRNS_PREFIX, serviceName, shardId, replicaId);
						} catch (Exception e) {
							logger.error("", e);
						}
					}

				}

			});
		} catch (Exception e) {
			throw e;
		}

	}

	private static void createMonitorNodeAtZK(String monitorUrl, String YRNS_PREFIX, String serviceName, String shardId, String replicaId) throws Exception {
		if (StringUtils.isBlank(YRNS_PREFIX) || StringUtils.isBlank(serviceName) || StringUtils.isBlank(shardId) || StringUtils.isBlank(replicaId))
			throw new Exception("cannot identify (serviceName, shardId, replicaId)");
		String monitorNodePath = YRNS_PREFIX + "/" + serviceName + "/" + shardId + "/monitor/" + replicaId;
		// 在前一个session超时前，创建一个临时节点可能会失败，因为前一个session创建的同一个节点可能还存在。保险起见，先删除，后创建。
		try {
			getCuratorFramework().delete().forPath(monitorNodePath);
		} catch (Throwable thr) {

		}
		getCuratorFramework().create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
				.forPath(monitorNodePath, monitorUrl.getBytes(DataDict.CHARACTER_SET_ENCODING_UTF8));
		logger.info("注册monitor地址:" + monitorUrl + "到zookeeper路径:" + monitorNodePath + "成功");
	}

}
