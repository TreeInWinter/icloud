package com.icloud.framework.distributedLock;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icloud.framework.config.PropertiesUtil;
import com.icloud.framework.core.common.ReturnCode;
import com.icloud.framework.core.exception.BizException;
import com.icloud.framework.core.util.StringUtil;
import com.icloud.framework.dict.DataDict;
import com.icloud.framework.util.LogFileReader;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.recipes.cache.PathChildrenCache;
import com.netflix.curator.framework.recipes.cache.PathChildrenCacheEvent;
import com.netflix.curator.framework.recipes.cache.PathChildrenCacheListener;
import com.netflix.curator.framework.recipes.locks.InterProcessMutex;
import com.netflix.curator.framework.recipes.locks.InterProcessReadWriteLock;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.framework.state.ConnectionStateListener;
import com.netflix.curator.retry.ExponentialBackoffRetry;
import com.netflix.curator.retry.RetryNTimes;

public class DistributedLockFramework {
	private static Logger logger = LoggerFactory.getLogger(DistributedLockFramework.class);
	private Random random = new Random();
	private static int sourceNumber = 0;
	private static List<String> sourceLockList = new ArrayList<String>();
	private static Map<String, String> sourceMap = new ConcurrentHashMap<String, String>();
	private static final String SPECIAL_VALUE = "NIL";
	private static final String FILE_SPERATPOR = "/";
	// 资源路径
	private String resource_preffix = "/distributedlock/resource";
	// 锁路径
	private String lock_resource_preffix = "/distributedlock/lock";

	// CuratorFramework
	private CuratorFramework client = null;
	// lockClient
	private CuratorFramework lockClient = null;

	private static class InstanceHolder {
		private static DistributedLockFramework INSTANCE = new DistributedLockFramework();
		static {
			INSTANCE.init();
		}
	}

	private void initPath(String path) {
		if (path != null) {
			String[] paths = path.split(FILE_SPERATPOR);
			String parentPath = "";
			for (int i = 0; i < paths.length; i++) {
				if (paths[i].trim().length() > 0) {
					parentPath = parentPath + FILE_SPERATPOR + paths[i];
					try {
						if (client.checkExists().forPath(parentPath) == null) {
							client.create().forPath(parentPath, paths[i].getBytes(DataDict.CHARACTER_SET_ENCODING_UTF8));
						}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						logger.error("", e);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("", e);
					}
				}
			}
			// try {
			// client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,
			// path.getBytes(DataDict.CHARACTER_SET_ENCODING_UTF8));
			// } catch (UnsupportedEncodingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
	}

	/**
	 * loadResourceToZookeeper
	 *
	 * @throws Exception
	 */
	public void uploadResourceToZookeeper() throws Exception {
		initPath(resource_preffix);
		initPath(lock_resource_preffix);
		LogFileReader reader = new LogFileReader(DistributedLockFramework.class.getResourceAsStream("/data/lock-resource.txt"));
		String line = null;
		String[] params = null;
		while ((line = reader.readLine()) != null) {
			params = line.split(" ");
			if (params.length == 2) {
				// // 检查路径是否存在
				loadResourcetoZookeeper(params[0], params[1], params[0]);
			}
		}
	}

	public void loadResourcetoZookeeper(String node, String opera, String data) throws UnsupportedEncodingException, Exception {
		if (opera.equalsIgnoreCase("add")) {
			String path = resource_preffix + FILE_SPERATPOR + node;
			if (client.checkExists().forPath(path) == null) {
				client.create().forPath(path, data.getBytes(DataDict.CHARACTER_SET_ENCODING_UTF8));
				// client.checkExists().usingWatcher(new
				// DataWatcher()).forPath(path);
				logger.info(resource_preffix + FILE_SPERATPOR + node + "  is add");
			}
		} else if (opera.equalsIgnoreCase("del")) {
			if (client.checkExists().forPath(resource_preffix + FILE_SPERATPOR + node) != null) {
				client.delete().forPath(resource_preffix + FILE_SPERATPOR + node);
				logger.info(resource_preffix + FILE_SPERATPOR + node + "  is deleted");
			}
		}
	}

	/**
	 * loadResourceFromZookeeper
	 *
	 * @throws Exception
	 */
	public void loadResourceFromZookeeper() throws Exception {
		if (client.checkExists().forPath(resource_preffix) != null) {
			List<String> pathList = client.getChildren().forPath(resource_preffix);
			if (pathList != null) {
				for (String path : pathList) {
					path = resource_preffix + FILE_SPERATPOR + path;
					byte[] data = client.getData().forPath(path);
					addResource(path, data);
				}
			}
		}
	}

	/**
	 * user this method instead of "YRCuratorFramework.InstanceHolder.INSTANCE"
	 *
	 * @return
	 */
	public static DistributedLockFramework getInstance() {
		return InstanceHolder.INSTANCE;
	}

	private void init() {
		try {
			DistributedLockFramework.getInstance().addConnectionStateListener(new ConnectionStateListener() {

				@Override
				public void stateChanged(CuratorFramework client, ConnectionState newState) {
					if (newState == ConnectionState.CONNECTED) {
						logger.info("CONNECTED");
					}
					if (newState == ConnectionState.LOST) {
						logger.info("LOST");
					}
					if (newState == ConnectionState.RECONNECTED) {
						logger.info("RECONNECTED");
					}
					if (newState == ConnectionState.SUSPENDED) {
						logger.info("SUSPENDED");
					}
				}
			});
		} catch (Throwable thr) {
			logger.error("", thr);
		}
		try {
			loadResourceFromZookeeper();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			logger.error("", e1);
		}

	}

	public static void addResource(String key, byte[] data) {
		if (key != null) {
			if (!key.startsWith(FILE_SPERATPOR)) {
				key = FILE_SPERATPOR + key;
			}
			if (data != null) {
				try {
					logger.info("add path is : " + key);
					sourceMap.put(key, new String(data, DataDict.CHARACTER_SET_ENCODING_UTF8));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					logger.error("", e);
				}
			} else {
				logger.info(" del key is " + key);
				sourceMap.put(key, SPECIAL_VALUE);
			}
			LockSourceMapToList();
		}

	}

	public synchronized String getWriteLockPath(int index) {
		if (index < 0)
			return null;
		if (index >= sourceLockList.size()) {
			index = index % sourceLockList.size();
		}
		return sourceLockList.get(index);
	}

	// 返回一个随机锁
	public DistributedLock acquire(int time) {
		int index = Math.abs(random.nextInt() % sourceNumber);
		// 循环等待一周
		for (int i = 0; i < sourceNumber; i++) {
			index = index + i;
			String readWriteLockPath = getWriteLockPath(index);
			InterProcessMutex writeLock = getInterProcessMutex(readWriteLockPath);
			try {
				if (writeLock.acquire(time, TimeUnit.SECONDS)) {
					DistributedLock lock = new DistributedLock(writeLock, readWriteLockPath);
					return lock;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			}
		}
		return null;
	}

	// 等待5秒，循环进行
	public DistributedLock acquire() {
		return acquire(5);
	}

	public InterProcessMutex getInterProcessMutex(String readWriteLockPath) {
		InterProcessReadWriteLock readWriteLock = new InterProcessReadWriteLock(lockClient, readWriteLockPath);
		InterProcessMutex writeLock = readWriteLock.writeLock();
		return writeLock;
	}

	private synchronized static void LockSourceMapToList() {
		// TODO Auto-generated method stub
		Set<Entry<String, String>> set = sourceMap.entrySet();
		List<String> tmpList = new ArrayList<String>();
		for (Entry<String, String> entry : set) {
			if (entry.getValue() != null && !entry.getValue().equalsIgnoreCase(SPECIAL_VALUE)) {
				// tmpList.add(LOCK_RESOURCE_PREFFIX + FILE_SPERATPOR +
				// entry.getValue());
				tmpList.add(FILE_SPERATPOR + entry.getValue());
			}
		}
		sourceLockList = tmpList;
		sourceNumber = sourceLockList.size();
	}

	/**
	 * 增加连接状态监听器
	 *
	 * @param listener
	 * @throws Exception
	 */
	public void addConnectionStateListener(ConnectionStateListener listener) throws Exception {
		if (client == null)
			throw new IllegalStateException("没有可用的curatorFramework");
		client.getConnectionStateListenable().addListener(listener);
	}

	private static class ResourceCacheListener implements PathChildrenCacheListener {

		@Override
		public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
			if (event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED || event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED) {
				String path = StringUtil.trim(event.getData().getPath());
				addResource(path, event.getData().getData());
			} else if (event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
				String path = StringUtil.trim(event.getData().getPath());
				addResource(path, null);
			}
		}
	}

	private void addPathChildrenCacheListener(String path) {
		PathChildrenCache shardListener = new PathChildrenCache(client, path, true);
		shardListener.getListenable().addListener(new ResourceCacheListener());
		try {
			shardListener.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}

	private DistributedLockFramework() {
		resource_preffix = PropertiesUtil.getProperty("properties/distributed-lock.properties", "rersource_preffix");
		lock_resource_preffix = PropertiesUtil.getProperty("properties/distributed-lock.properties", "lock_preffix");
		String connectionString = PropertiesUtil.getProperty("properties/distributed-lock.properties", "connectionString");
		if (resource_preffix == null || lock_resource_preffix == null || connectionString == null) {
			String retMsg = "创建CuratorFramework时出错,rersource_preffix,lock_preffix,connectionString三者不能为空";
			logger.error(retMsg);
			throw BizException.instance(ReturnCode.ERROR, retMsg);
		}
		String path = "/";
		try {
			client = CuratorFrameworkFactory.builder().connectString(connectionString).namespace(path).retryPolicy(new ExponentialBackoffRetry(1000, 3))
					.connectionTimeoutMs(10000).build();
			client.start();
		} catch (Exception e) {
			logger.error("创建CuratorFramework时出错", e);
			client = null;
			throw e;
		}
		lockClient = CuratorFrameworkFactory.builder().connectString(connectionString).namespace(lock_resource_preffix)
				.retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).connectionTimeoutMs(5000).build();
		lockClient.start();

		addPathChildrenCacheListener(resource_preffix);
		try {
			addConnectionStateListener(new ConnectionStateListener() {
				@Override
				public void stateChanged(CuratorFramework client, ConnectionState newState) {
					if (newState == ConnectionState.RECONNECTED) {
						addPathChildrenCacheListener(resource_preffix);
					}
				}

			});
		} catch (Throwable thr) {
			logger.error("", thr);
		}

		// try {
		// uploadResourceToZookeeper();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public CuratorFramework getCuratorFramework() {
		// TODO Auto-generated method stub
		if (InstanceHolder.INSTANCE.client == null)
			throw new IllegalStateException("没有可用的curatorFramework");
		else
			return InstanceHolder.INSTANCE.client;
	}

	public void displayResource() {
		// TODO Auto-generated method stub
		for (String source : sourceLockList) {
			logger.info(source);
		}
	}

	public void clearAllLock() {

		// if (client.checkExists().forPath(resource_preffix + FILE_SPERATPOR +
		// node) != null) {
		// client.delete().forPath(resource_preffix + FILE_SPERATPOR + node);
		// logger.info(resource_preffix + FILE_SPERATPOR + node +
		// "  is deleted");
		// }
		for (String lock : sourceLockList) {
			try {
				clearLock(this.lock_resource_preffix + lock);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void clearLock(String lockPath) throws Exception {
		if (client.checkExists().forPath(lockPath) != null) {
			// client.delete().forPath(lockPath);
			List<String> pathList = client.getChildren().forPath(lockPath);
			for (String path : pathList) {
				client.delete().forPath(lockPath + FILE_SPERATPOR + path);
			}
			client.delete().forPath(lockPath);
		}
	}
}
