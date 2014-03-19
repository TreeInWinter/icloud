package com.travelzen.framework.util;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class ZooKeeperTest {
	private static final int SESSION_TIMEOUT = 30000; // 会话超时时间，设置为与系统默认时间一致
	private ZooKeeper zk; // 创建 ZooKeeper 实例
	private Watcher wh = new Watcher() { // 创建 Watcher 实例
		public void process(WatchedEvent event) {
			System.out.println(event);
		}
	};

	private void createZKInstance() throws IOException { // 初始化 ZooKeeper 实例
		zk = new ZooKeeper("zk1:2181,zk2:2181,zk3:2181,zk4:2181,zk5:2181", SESSION_TIMEOUT, this.wh);
	}

	private void ZKOperations() throws IOException, InterruptedException, KeeperException {
		System.out.println("/n1. 创建 ZooKeeper 节点 (znode ： zoo2, 数据： myData2 ，权限： OPEN_ACL_UNSAFE ，节点类型： Persistent");
		zk.create("/zoo2", "myData2".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

		System.out.println("查看根节点下的所有节点： ");
		for (String path : zk.getChildren("/", false))
			System.out.println(path);

		System.out.println("/n2. 查看是否创建成功： ");
		System.out.println(new String(zk.getData("/zoo2", false, null)));

		System.out.println("/n3. 修改节点数据 ");
		zk.setData("/zoo2", "shenlan211314".getBytes(), -1);

		System.out.println("/n4. 查看是否修改成功： ");
		System.out.println(new String(zk.getData("/zoo2", false, null)));

		System.out.println("/n5. 删除节点 ");
		zk.delete("/zoo2", -1);

		System.out.println("/n6. 查看节点是否被删除： ");
		System.out.println(" 节点状态： [" + zk.exists("/zoo2", false) + "]");
	}

	private void ZKClose() throws InterruptedException {
		zk.close();
	}

	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		ZooKeeperTest test = new ZooKeeperTest();
		test.createZKInstance();
		test.ZKOperations();
		test.ZKClose();
	}
}
