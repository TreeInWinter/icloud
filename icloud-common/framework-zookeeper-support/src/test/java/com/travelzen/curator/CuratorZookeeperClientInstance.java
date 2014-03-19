package com.travelzen.curator;

import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.netflix.curator.CuratorZookeeperClient;
import com.netflix.curator.RetryLoop;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.recipes.locks.InterProcessMutex;
import com.netflix.curator.framework.recipes.locks.InterProcessReadWriteLock;
import com.netflix.curator.retry.RetryNTimes;

public class CuratorZookeeperClientInstance {
//	String connectionString = "192.168.160.89:2181,192.168.160.91:2181,192.168.160.92:2181";
	String connectionString = "192.168.130.77:2181,192.168.130.78:2181,192.168.130.79:2181";
//	String connectionString = "192.168.130.78:2181";
	// String connectionString = "192.168.160.89:2181";

	public void testlock() throws Exception {
		String readWriteLockPath = "/RWLock";

		CuratorFramework curator = CuratorFrameworkFactory.builder().connectString(connectionString).namespace("/local4")
				.retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).connectionTimeoutMs(5000).build();
		curator.start();

		InterProcessReadWriteLock readWriteLock1 = new InterProcessReadWriteLock(curator, readWriteLockPath);

		InterProcessMutex writeLock1 = readWriteLock1.writeLock();

		InterProcessMutex readLock1 = readWriteLock1.readLock();
		InterProcessReadWriteLock readWriteLock2 = new InterProcessReadWriteLock(curator, readWriteLockPath);

		InterProcessMutex writeLock2 = readWriteLock2.writeLock();
		InterProcessMutex readLock2 = readWriteLock2.readLock();
		System.out.println("---- sss s ");
		writeLock2.acquire();
		System.out.println("start to lock222222");
		// Thread.sleep(3333333);
		// System.out.println("22222");
		// // same with WriteLock, can read
		// writeLock2.acquire();
		// System.out.println("33333");
		// System.out.println(readLock1.acquire(1, TimeUnit.SECONDS));
		//
		// // different lock, can't read while writting
		//
		// System.out.println(readLock2.acquire(1, TimeUnit.SECONDS));
		//
		// // different write lock, can't write
		//
		// System.out.println(writeLock2.acquire(1, TimeUnit.SECONDS));
		//
		// // release the write lock
		//
		// writeLock1.release();
		//
		// // both read lock can read
		//
		// System.out.println(readLock1.acquire(1, TimeUnit.SECONDS));
		//
		// System.out.println(readLock2.acquire(1, TimeUnit.SECONDS));
	}

	public void init() throws Exception {
		String path = "/test_path";
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString(connectionString).namespace("/")
				.retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).connectionTimeoutMs(8000).build();
		// 启动 上面的namespace会作为一个最根的节点在使用时自动创建
		client.start();

		// 异步地删除一个节点
		client.delete().inBackground().forPath("/head");
//		client.delete().forPath("/head");

		// 创建一个节点
		client.create().forPath("/head", new byte[0]);

		// 创建一个临时节点
		client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/head/child", new byte[0]);

		// 取数据
		client.getData().watched().inBackground().forPath("/test");

		// 检查路径是否存在
		client.checkExists().forPath(path);

		// 异步删除
		client.delete().inBackground().forPath("/head");

		// 注册观察者，当节点变动时触发
		client.getData().usingWatcher(new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println("node is changed");
			}
		}).inBackground().forPath("/test");

		// 结束使用
		// client.close();

	}

	public static void main(String args[]) {
		CuratorZookeeperClientInstance instance = new CuratorZookeeperClientInstance();
		System.out.println("-------------");
		try {
			// instance.testlock();
			instance.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
