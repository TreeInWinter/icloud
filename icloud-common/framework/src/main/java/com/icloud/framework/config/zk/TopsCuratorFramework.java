package com.icloud.framework.config.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.icloud.framework.config.tops.util.TopsCommonResourceUtil;
import com.icloud.framework.core.util.RPIDLogger;
import com.icloud.framework.core.util.StringUtil;
import com.icloud.framework.core.util.TZUtil;
import com.icloud.framework.dict.DataDict;
import com.netflix.curator.RetryPolicy;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.api.CuratorWatcher;
import com.netflix.curator.framework.recipes.cache.PathChildrenCache;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.framework.state.ConnectionStateListener;
import com.netflix.curator.retry.ExponentialBackoffRetry;

/**
 * 对curator做的标准化定制。该类实现了单例模式，保证一个进程只有一个zookeeper连接。
 *
 * @author Jiangning
 *
 */
public class TopsCuratorFramework {

	private CuratorFramework client = null;

	private static class InstanceHolder {
		private static TopsCuratorFramework INSTANCE = new TopsCuratorFramework();
		static {
			INSTANCE.init();
		}
	}

	/**
	 * user this method instead of "YRCuratorFramework.InstanceHolder.INSTANCE"
	 *
	 * @return
	 */
	public static TopsCuratorFramework getInstance() {
		return InstanceHolder.INSTANCE;
	}

	@SuppressWarnings("static-access")
	private void init() {
		try {
			TopsCuratorFramework.getInstance().addConnectionStateListener(new ConnectionStateListener() {

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
			RPIDLogger.error("", thr);
		}
	}

	private TopsCuratorFramework() {
		String connectionString = TopsCommonResourceUtil.getZookeeperService();
		if (StringUtil.isEmpty(connectionString))
			return;
		connectionString = connectionString.trim();
		try {
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
			client = CuratorFrameworkFactory.newClient(connectionString, 10 * 1000, 10 * 1000, retryPolicy);
			client.start();
		} catch (Exception e) {
			RPIDLogger.error("创建CuratorFramework时出错", e);
			client = null;
		}
	}

	/**
	 * 获取curatorFramework
	 *
	 * @return
	 * @throws Exception
	 */
	private CuratorFramework getCuratorFramework() throws Exception {
		if (client == null)
			throw new IllegalStateException("没有可用的curatorFramework");
		else
			return InstanceHolder.INSTANCE.client;
	}

	/**
	 * 检查client
	 */
	public void assertClient() {
		if (TZUtil.isEmpty(client))
			throw new IllegalStateException("没有可用的curatorFramework");
	}

	/**
	 * 增加连接状态监听器
	 *
	 * @param listener
	 * @throws Exception
	 */
	public void addConnectionStateListener(ConnectionStateListener listener) throws Exception {
		assertClient();
		if (TZUtil.isEmpty(listener))
			throw new IllegalStateException("listener is null");
		client.getConnectionStateListenable().addListener(listener);
		RPIDLogger.info("addConnectionStateListener");
	}

	public PathChildrenCache addPathChildrenCache(String nodePath) {
		assertClient();
		if (TZUtil.isEmpty(nodePath))
			throw new IllegalStateException("nodePath is null");
		PathChildrenCache pathChildrenCache = new PathChildrenCache(client, nodePath, true);
		return pathChildrenCache;
	}

	/**
	 * 删除某个节点
	 *
	 * @param monitorNodePath
	 * @throws Exception
	 */
	public void deleteNode(String monitorNodePath) throws Exception {
		assertClient();
		if (TZUtil.isEmpty(monitorNodePath))
			throw new IllegalStateException("monitorNodePath is null");
		Stat stat = checkNodePath(monitorNodePath,null);
		if (stat != null) {
			client.delete().forPath(monitorNodePath);
		}
	}

	/**
	 * 创建某个节点 path: 路径 data: 数据 mode: 创建模式 PERSISTENT：创建后只要不删就永久存在
	 *
	 * EPHEMERAL：会话结束年结点自动被删除
	 *
	 * SEQUENTIAL：节点名末尾会自动追加一个10位数的单调递增的序号，同一个节点的所有子节点序号是单调递增的
	 *
	 * PERSISTENT_SEQUENTIAL：结合PERSISTENT和SEQUENTIAL
	 *
	 * EPHEMERAL_SEQUENTIAL：结合EPHEMERAL和SEQUENTIAL
	 *
	 * @param monitorNodePath
	 * @param data
	 * @param mode
	 * @throws Exception
	 */
	public void createNode(String monitorNodePath, byte[] data, CreateMode mode) throws Exception {
		assertClient();
		if (TZUtil.isEmpty(monitorNodePath))
			throw new IllegalStateException("monitorNodePath is null");
		if (TZUtil.isEmpty(mode))
			throw new IllegalStateException("mode is null");
		client.create().creatingParentsIfNeeded().withMode(mode).forPath(monitorNodePath, data);
	}

	/**
	 * 创建一个临时路径的数据
	 *
	 * @param nodepath
	 * @param dataStr
	 */
	public void createEphemeralDataNode(String nodepath, String dataStr) throws Exception {
		if (TZUtil.isEmpty(dataStr))
			throw new IllegalStateException("dataStr is null");
		createNode(nodepath, dataStr.getBytes(DataDict.CHARACTER_SET_ENCODING_UTF8), CreateMode.EPHEMERAL);
	}

	/**
	 * 创建一个永久路径的数据
	 *
	 * @param moitorNodepath
	 * @param dataStr
	 */
	public void createPersistentDataNode(String nodepath, String dataStr) throws Exception {
		if (TZUtil.isEmpty(dataStr))
			throw new IllegalStateException("dataStr is null");
		createNode(nodepath, dataStr.getBytes(DataDict.CHARACTER_SET_ENCODING_UTF8), CreateMode.PERSISTENT);
	}

	/**
	 * 获得数据
	 *
	 * @throws Exception
	 */
	public byte[] getData(String nodepath) throws Exception {
		assertClient();
		if (TZUtil.isEmpty(nodepath))
			throw new IllegalStateException("nodepath is null");
		return client.getData().forPath(nodepath);
	}

	/**
	 * 获得字符串类型的数据
	 *
	 * @throws Exception
	 */
	public String getDataForString(String nodepath) throws Exception {
		byte[] data = getData(nodepath);
		if (!TZUtil.isEmpty(data)) {
			return new String(data, DataDict.CHARACTER_SET_ENCODING_UTF8);
		}
		return null;
	}

	/**
	 * 检查节点 采用一个watcher
	 *
	 * @param zkPath
	 * @param dataWatcher
	 * @return
	 * @throws Exception
	 */
	public Stat checkNodePath(String zkPath, CuratorWatcher curatorWatcher) throws Exception {
		assertClient();
		if (TZUtil.isEmpty(zkPath))
			throw new IllegalStateException("zkPath is null");
		if (TZUtil.isEmpty(curatorWatcher)) {
			return client.checkExists().forPath(zkPath);

		} else {
			return client.checkExists().usingWatcher(curatorWatcher).forPath(zkPath);
		}

	}
}
