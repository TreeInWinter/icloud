package com.travelzen.framework.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.curator.RetryPolicy;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.recipes.cache.PathChildrenCache;
import com.netflix.curator.framework.recipes.cache.PathChildrenCacheEvent;
import com.netflix.curator.framework.recipes.cache.PathChildrenCacheListener;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.framework.state.ConnectionStateListener;
import com.netflix.curator.retry.ExponentialBackoffRetry;
import com.travelzen.framework.config.PropertiesUtil;
import com.travelzen.framework.core.util.RPIDLogger;
import com.travelzen.framework.core.util.StringUtil;
import com.travelzen.framework.dict.DataDict;

/**
 * 对curator做的标准化定制。该类实现了单例模式，保证一个进程只有一个zookeeper连接。
 *
 * @author shuiren
 *
 */
public class TZCuratorFramework {

	private CuratorFramework client = null;

	private static Map<String, Map<String, String>> rpcUrls = new HashMap<String, Map<String, String>>();

	private static Logger logger = LoggerFactory
			.getLogger(TZCuratorFramework.class);

	private String serviceName;

	private String shardId;

	private String replicaId;

	public static String service_shard_replica;

	public static String YRNS_PREFIX = "/TZNS/TZ";

	private static class InstanceHolder {
		private static TZCuratorFramework INSTANCE = new TZCuratorFramework();
		static {
			INSTANCE.init();
		}
	}

	/**
	 * user this method instead of "YRCuratorFramework.InstanceHolder.INSTANCE"
	 *
	 * @return
	 */
	public static TZCuratorFramework getInstance() {
		return InstanceHolder.INSTANCE;
	}

	@SuppressWarnings("static-access")
	private void init() {
		try {
			TZCuratorFramework.getInstance().addConnectionStateListener(
					new ConnectionStateListener() {

						@Override
						public void stateChanged(CuratorFramework client,
								ConnectionState newState) {
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
			// 抽取部署结构
			initDeploymentInfo();
		} catch (Throwable thr) {
			logger.error("", thr);
		}
	}

	private TZCuratorFramework() {
		// String connectionString = System.getenv("TZFS_SERVER");
		String connectionString = PropertiesUtil.getProperty(
				"properties/zkService.properties", "connectionString");
		if (StringUtil.isEmpty(connectionString))
			return;
		connectionString = connectionString.trim();
		try {
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
			client = CuratorFrameworkFactory.newClient(connectionString,
					10 * 1000, 10 * 1000, retryPolicy);
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
	public static void addConnectionStateListener(
			ConnectionStateListener listener) throws Exception {
		if (InstanceHolder.INSTANCE.client == null)
			throw new IllegalStateException("没有可用的curatorFramework");
		InstanceHolder.INSTANCE.client.getConnectionStateListenable()
				.addListener(listener);
	}

	/**
	 * 返回服务的rpc地址，若没有取到则返回null
	 *
	 * @param rpcServiceName
	 * @return
	 */
	public static List<String> getRpcAdress(final String rpcServiceName) {
		if (InstanceHolder.INSTANCE.client == null)
			return null;
		if (rpcUrls.containsKey(rpcServiceName)) {
			if (rpcUrls.get(rpcServiceName) != null)
				return new ArrayList<String>(rpcUrls.get(rpcServiceName)
						.values());
			else
				return null;
		}
		synchronized (TZCuratorFramework.class) {
			if (rpcUrls.containsKey(rpcServiceName)) {
				if (rpcUrls.get(rpcServiceName) != null)
					return new ArrayList<String>(rpcUrls.get(rpcServiceName)
							.values());
				else
					return null;
			}
			try {
				rpcUrls.put(rpcServiceName,
						new ConcurrentHashMap<String, String>());
				PathChildrenCache shardListener = new PathChildrenCache(
						InstanceHolder.INSTANCE.client, YRNS_PREFIX
								+  "/" + rpcServiceName, true);
//				System.out.println(YRNS_PREFIX +  "/" + rpcServiceName);
				shardListener.getListenable().addListener(
						new PathChildrenCacheListener() {
							@Override
							public void childEvent(CuratorFramework client,
									PathChildrenCacheEvent event)
									throws Exception {
								if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
									String shardNodePath = StringUtil
											.trim(event.getData().getPath());
									PathChildrenCache replicaListener = new PathChildrenCache(
											InstanceHolder.INSTANCE.client,
											shardNodePath + "/rpc", true);
									replicaListener
											.getListenable()
											.addListener(
													new PathChildrenCacheListener() {
														@Override
														public void childEvent(
																CuratorFramework client,
																PathChildrenCacheEvent event)
																throws Exception {
															if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED
																	|| event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED) {
																String replicaNodePath = StringUtil
																		.trim(event
																				.getData()
																				.getPath());
																String replicaRpcUrl = StringUtil
																		.trim(new String(
																				event.getData()
																						.getData(),
																				DataDict.CHARACTER_SET_ENCODING_UTF8));
																rpcUrls.get(
																		rpcServiceName)
																		.put(replicaNodePath,
																				replicaRpcUrl);
															} else if (event
																	.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
																String replicaNodePath = StringUtil
																		.trim(event
																				.getData()
																				.getPath());
																rpcUrls.get(
																		rpcServiceName)
																		.remove(replicaNodePath);
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
				return getRpcAdress(rpcServiceName);
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
	public static void registerRpc(final String rpcUrl) throws Exception {
		try {
			if (StringUtils
					.isBlank(TZCuratorFramework.InstanceHolder.INSTANCE.serviceName)
					|| StringUtils
							.isBlank(TZCuratorFramework.InstanceHolder.INSTANCE.shardId)
					|| StringUtils
							.isBlank(TZCuratorFramework.InstanceHolder.INSTANCE.replicaId))
				throw new Exception(
						"cannot identify (serviceName, shardId, replicaId)");
			createRpcNodeAtZK(rpcUrl);
			addConnectionStateListener(new ConnectionStateListener() {

				@Override
				public void stateChanged(CuratorFramework client,
						ConnectionState newState) {
					if (newState == ConnectionState.RECONNECTED) {
						try {
							createRpcNodeAtZK(rpcUrl);
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

	private static void createRpcNodeAtZK(String rpcUrl) throws Exception {
		String rpcNodePath = YRNS_PREFIX + "/"
				+ TZCuratorFramework.InstanceHolder.INSTANCE.serviceName + "/"
				+ TZCuratorFramework.InstanceHolder.INSTANCE.shardId + "/rpc/"
				+ TZCuratorFramework.InstanceHolder.INSTANCE.replicaId;
		// 在前一个session超时前，创建一个临时节点可能会失败，因为前一个session创建的同一个节点可能还存在。保险起见，先删除，后创建。
		try {
			getCuratorFramework().delete().forPath(rpcNodePath);
		} catch (Throwable thr) {

		}
		getCuratorFramework()
				.create()
				.creatingParentsIfNeeded()
				.withMode(CreateMode.EPHEMERAL)
				.forPath(rpcNodePath,
						rpcUrl.getBytes(DataDict.CHARACTER_SET_ENCODING_UTF8));
		logger.info("注册rpc地址:" + rpcUrl + "到zookeepre路径:" + rpcNodePath + "成功");
		System.out.println("注册rpc地址:" + rpcUrl + "到zookeepre路径:" + rpcNodePath
				+ "成功");
	}

	/**
	 * 注册监控服务的url
	 *
	 * @param monitorUrl
	 */
	public static void registerMonitor(final String monitorUrl)
			throws Exception {
		try {
			if (StringUtils
					.isBlank(TZCuratorFramework.InstanceHolder.INSTANCE.serviceName)
					|| StringUtils
							.isBlank(TZCuratorFramework.InstanceHolder.INSTANCE.shardId)
					|| StringUtils
							.isBlank(TZCuratorFramework.InstanceHolder.INSTANCE.replicaId))
				throw new Exception(
						"cannot identify (serviceName, shardId, replicaId)");
			createMonitorNodeAtZK(monitorUrl);
			addConnectionStateListener(new ConnectionStateListener() {

				@Override
				public void stateChanged(CuratorFramework client,
						ConnectionState newState) {
					if (newState == ConnectionState.RECONNECTED) {
						try {
							createMonitorNodeAtZK(monitorUrl);
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

	private static void createMonitorNodeAtZK(String monitorUrl)
			throws Exception {
		String monitorNodePath = YRNS_PREFIX + "/"
				+ TZCuratorFramework.InstanceHolder.INSTANCE.serviceName + "/"
				+ TZCuratorFramework.InstanceHolder.INSTANCE.shardId
				+ "/monitor/"
				+ TZCuratorFramework.InstanceHolder.INSTANCE.replicaId;
		// 在前一个session超时前，创建一个临时节点可能会失败，因为前一个session创建的同一个节点可能还存在。保险起见，先删除，后创建。
		try {
			getCuratorFramework().delete().forPath(monitorNodePath);
		} catch (Throwable thr) {

		}
		getCuratorFramework()
				.create()
				.creatingParentsIfNeeded()
				.withMode(CreateMode.EPHEMERAL)
				.forPath(
						monitorNodePath,
						monitorUrl
								.getBytes(DataDict.CHARACTER_SET_ENCODING_UTF8));
		logger.info("注册monitor地址:" + monitorUrl + "到zookeepre路径:"
				+ monitorNodePath + "成功");
	}

	private static void initDeploymentInfo() {
		// String userDir = StringUtil.trim(System.getProperty("user.dir"));
		String userDir = PropertiesUtil.getProperty(
				"properties/zkService.properties", "user.dir");
		if (StringUtils.isBlank(userDir)) {
			logger.error(String
					.format("the value of java 'user.dir' property(%s) is empty, cannot identify (serviceName, shardId, replicaId)",
							userDir));
			return;
		}
		String[] userDirConstituent = userDir.split("/");
		if (userDirConstituent.length < 4) {
			logger.error(String
					.format("the value of java 'user.dir' property(%s) is not format '/var/tz/serviceName_shardId_replicaId/...', cannot identify (serviceName, shardId, replicaId)",
							userDir));
			return;
		}
		YRNS_PREFIX = userDirConstituent[0] + "/" + userDirConstituent[1] + "/" + userDirConstituent[2];
		YRNS_PREFIX = YRNS_PREFIX.trim().toUpperCase();
		service_shard_replica = userDirConstituent[3];
		if (service_shard_replica.split("_").length < 3) {
			logger.error(String
					.format("the value of java 'user.dir' property(%s) is not format '/var/tz/serviceName_shardId_replicaId/...', cannot identify (serviceName, shardId, replicaId)",
							userDir));
			return;
		}
		int replicaIndex = service_shard_replica.lastIndexOf("_");
		TZCuratorFramework.InstanceHolder.INSTANCE.replicaId = service_shard_replica
				.substring(replicaIndex + 1);
		int shardIndex = service_shard_replica.substring(0, replicaIndex)
				.lastIndexOf("_");
		TZCuratorFramework.InstanceHolder.INSTANCE.shardId = service_shard_replica
				.substring(shardIndex + 1, replicaIndex);
		TZCuratorFramework.InstanceHolder.INSTANCE.serviceName = service_shard_replica
				.substring(0, shardIndex);
	}

}
