package com.icloud.framework.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.imps.CuratorFrameworkState;
import com.netflix.curator.retry.RetryNTimes;

public class ZooKeeperUtil {
	private static Logger logger = LoggerFactory.getLogger(ZooKeeperUtil.class);

	private static final int SESSION_TIMEOUT = 5000; // 会话超时时间，设置为与系统默认时间一致

	private String serverName;

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	static private Map<String, CuratorFramework> serverInstanceMap = new ConcurrentHashMap<String, CuratorFramework>();
//	static private Map<String, CuratorFramework> clientInstanceMap = new ConcurrentHashMap<String, CuratorFramework>();

	public static CuratorFramework getClient(String connectString , String serverName) {
		CuratorFramework zookeeperClient;

		if (serverInstanceMap.containsKey(serverName)) {
			zookeeperClient = serverInstanceMap.get(serverName);

//			if (!zookeeperClient.isStarted()) {
			if (zookeeperClient.getState() != CuratorFrameworkState.STARTED) {
				zookeeperClient.start();

//				if (zookeeperClient.isStarted())
				if (zookeeperClient.getState() == CuratorFrameworkState.STARTED)
					return zookeeperClient;
			}

		}
		
		
//		try {
			zookeeperClient = CuratorFrameworkFactory.builder()
					.connectString(connectString).namespace(serverName)
					.sessionTimeoutMs(SESSION_TIMEOUT)
					.retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000))
					.connectionTimeoutMs(5000).build();

			zookeeperClient.start();

//			if (zookeeperClient.isStarted())
			if (zookeeperClient.getState() == CuratorFrameworkState.STARTED)
				return zookeeperClient;

//		} catch (IOException e) {
//			logger.error(e.getLocalizedMessage(), e);
//		}

		return null;
	}

	/**
	 * 注册服务
	 */
	public static CuratorFramework regeditZookeeper(CuratorFramework client,
			String ip) {
		try {
			client.create().creatingParentsIfNeeded()
					.withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
					.forPath(ip + "$$", new byte[0]);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		return null;
	}

	/***
	 * 得到节点列表
	 */
	public static List<String> getPathList(CuratorFramework client,String path) {
		List<String> resultList = new ArrayList<String>();
		try {
			List<String> list = client.getChildren().forPath(path);
			for (String temp : list) {
				if (temp != null)
					if (temp.indexOf("$$") == -1) {
						resultList.add(temp);
					} else {
						resultList.add(temp.substring(0, temp.indexOf("$$")));
					}
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		return resultList;
	}

	public static void main(String[] args) {

//		CuratorFramework serverConnecting = getClient("test");

//		ZooKeeperUtil.regeditZookeeper(serverConnecting, FBUtilities.getIp());

	}

}
