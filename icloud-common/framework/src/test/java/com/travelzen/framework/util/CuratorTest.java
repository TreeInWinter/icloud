package com.travelzen.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.curator.RetryPolicy;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.CuratorFrameworkFactory.Builder;
import com.netflix.curator.framework.imps.CuratorFrameworkState;
import com.netflix.curator.retry.RetryNTimes;

public class CuratorTest {
	private static Logger logger = LoggerFactory.getLogger(CuratorTest.class);

	public static void main(String[] args) {
		RetryPolicy retryPolicy = new RetryNTimes(Integer.MAX_VALUE, 1000);
		String connectString = "10.180.8.17:2181";
//		int connectionTimeoutMs = 5000;
//		int sessionTimeoutMs = 5000;
		String path = "/test";
		try {
			// CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
			// CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
			Builder builder = CuratorFrameworkFactory.builder();
			builder.connectString(connectString); // required
			builder.retryPolicy(retryPolicy); // required
			builder.namespace("curator"); // optional
			CuratorFramework client = builder.build();
			client.start();
			if (client.getState() != CuratorFrameworkState.STARTED) {
				client.create().forPath(path); // create(): 发起一个create操作. 可以组合其他方法 (比如mode 或background) 最后以forPath()方法结尾
				if (client.checkExists().forPath(path) != null) { // checkExists(): 发起一个检查ZNode 是否存在的操作. 可以组合其他方法(watch 或background) 最后以forPath()方法结尾
					client.setData().forPath(path, "setData".getBytes()); // setData(): 发起一个设置ZNode数据的操作. 可以组合其他方法(version 或background) 最后以forPath()方法结尾
					logger.info(new String(client.getData().forPath(path))); // getData(): 发起一个获取ZNode数据的操作. 可以组合其他方法(watch, background 或get stat) 最后以forPath()方法结尾
					for (String p : client.getChildren().forPath(path)) { // getChildren(): 发起一个获取ZNode子节点的操作. 可以组合其他方法(watch, background 或get stat) 最后以forPath()方法结尾
						logger.info(p); // getData(): 发起一个获取ZNode数据的操作. 可以组合其他方法(watch, background 或get stat) 最后以forPath()方法结尾
					}
					client.delete().forPath(path); // delete(): 发起一个删除操作. 可以组合其他方法(version 或background) 最后以forPath()方法结尾
				}
				client.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
