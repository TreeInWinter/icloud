package com.travelzen.framework.util;

import java.util.List;

import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.netflix.curator.RetryPolicy;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.CuratorFrameworkFactory.Builder;
import com.netflix.curator.retry.RetryNTimes;

public class CuratorUtil {
	private final static Logger logger = LoggerFactory.getLogger(CuratorUtil.class);

	private final static RetryPolicy retryPolicy = new RetryNTimes(Integer.MAX_VALUE, 1000);

	public static CuratorFramework client(String connectString) {
		return client(connectString, retryPolicy);
	}

	public static CuratorFramework client(String connectString, RetryPolicy retryPolicy) {
		return CuratorFrameworkFactory.newClient(connectString, retryPolicy);
	}

	public static CuratorFramework client(String connectString, int sessionTimeoutMs, int connectionTimeoutMs) {
		return client(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
	}

	public static CuratorFramework client(String connectString, int sessionTimeoutMs, int connectionTimeoutMs, RetryPolicy retryPolicy) {
		return CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
	}

	public static CuratorFramework client(String connectString, String namespace) {
		return client(connectString, namespace, retryPolicy);
	}

	public static CuratorFramework client(String connectString, String namespace, RetryPolicy retryPolicy) {
		Builder builder = CuratorFrameworkFactory.builder();
		builder.connectString(connectString); // required
		builder.retryPolicy(retryPolicy); // required
		builder.namespace(namespace); // optional
		return builder.build();
	}

	public static Optional<CuratorFramework> buildClient(String connectString, String namespace, RetryPolicy retryPolicy) {
		Builder builder = CuratorFrameworkFactory.builder();
		builder.connectString(connectString); // required
		builder.retryPolicy(retryPolicy); // required
		builder.namespace(namespace); // optional
		return Optional.of(builder.build());
	}

	public static String create(CuratorFramework client, String path) {
		try {
			return client.create().forPath(path);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static String create(CuratorFramework client, String path, byte[] data) {
		try {
			return client.create().forPath(path, data);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static void delete(CuratorFramework client, String path) {
		try {
			client.delete().forPath(path);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static Stat exists(CuratorFramework client, String path) {
		try {
			return client.checkExists().forPath(path);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static Stat setData(CuratorFramework client, String path) {
		try {
			return client.setData().forPath(path);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static Stat setData(CuratorFramework client, String path, byte[] data) {
		try {
			return client.setData().forPath(path, data);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static String getData(CuratorFramework client, String path) {
		try {
			return new String(client.getData().forPath(path));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static List<String> children(CuratorFramework client) {
		return children(client, null);
	}

	public static List<String> children(CuratorFramework client, String path) {
		try {
			return client.getChildren().forPath(path);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
