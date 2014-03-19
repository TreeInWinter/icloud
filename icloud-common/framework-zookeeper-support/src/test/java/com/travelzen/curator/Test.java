package com.travelzen.curator;

import java.util.concurrent.TimeUnit;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.recipes.locks.InterProcessLock;
import com.netflix.curator.framework.recipes.locks.InterProcessMutex;
import com.netflix.curator.framework.recipes.locks.InterProcessReadWriteLock;
import com.netflix.curator.retry.RetryNTimes;

public class Test {
	public static void readWriteLock() throws Exception {
		String lockName = "/lock5";
		CuratorFramework curator = CuratorFrameworkFactory.builder().connectString("192.168.163.54:21818").namespace("/")
				.retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).connectionTimeoutMs(5000).build();
		curator.start();

		final CuratorFramework client = curator.usingNamespace("/MyNamespace");

		// final InterProcessMutex mutex = new InterProcessMutex(client,
		// "/MyMutex");

		String readWriteLockPath = "/RWLock";
		InterProcessReadWriteLock readWriteLock1 = new InterProcessReadWriteLock(client, readWriteLockPath);
		InterProcessMutex writeLock1 = readWriteLock1.writeLock();
		// InterProcessMutex readLock1 = readWriteLock1.readLock();

		// InterProcessReadWriteLock readWriteLock2 = new
		// InterProcessReadWriteLock(curator, readWriteLockPath);
		// InterProcessMutex writeLock2 = readWriteLock2.writeLock();
		// InterProcessMutex readLock2 = readWriteLock2.readLock();

		System.out.println("111111111111");
		writeLock1.acquire();
		System.out.println("222222222222");
		// same with WriteLock, can read
		// System.out.println(readLock1.acquire(1, TimeUnit.SECONDS));

		// different lock, can't read while writting
		// System.out.println(readLock2.acquire(1, TimeUnit.SECONDS));
		//
		// // different write lock, can't write
		// System.out.println(writeLock2.acquire(1, TimeUnit.SECONDS));
		//
		// // release the write lock
		// writeLock1.release();
		//
		// // both read lock can read
		// System.out.println(readLock1.acquire(1, TimeUnit.SECONDS));
		// System.out.println(readLock2.acquire(1, TimeUnit.SECONDS));
	}

	public static void readLock2() {

		// CuratorFramework rootClient =
		// CuratorFrameworkFactory.builder().connectString("localhost:21818").connectionTimeoutMs(100).sessionTimeoutMs(100)
		// .retryPolicy(new RetryNTimes(3, 100)).build();
		CuratorFramework rootClient = CuratorFrameworkFactory.builder().connectString("192.168.163.54:21818").namespace("/")
				.retryPolicy(new RetryNTimes(Integer.MAX_VALUE, 1000)).connectionTimeoutMs(5000).build();
		rootClient.start();

		try {
			final CuratorFramework client = rootClient.usingNamespace("/MyNamespace");

			final InterProcessMutex mutex = new InterProcessMutex(client, "/MyMutex");

			try {
				// mutex.acquire(1L, TimeUnit.SECONDS);
				System.out.println("1111111111111");
				mutex.acquire();
				// mutex.release();
				System.out.println("222222222222");
				// Thread.sleep(5000000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			rootClient.close();
		}

	}

	public static void main(String args[]) throws Exception {
		// readLock2();
		readWriteLock();
	}
}
