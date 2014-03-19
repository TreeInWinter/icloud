package com.travelzen.tops.lock.test;

import com.travelzen.framework.distributedLock.DistributedLock;
import com.travelzen.framework.distributedLock.DistributedLockFramework;

public class DistributedLockTest {
	public void testLockResource() throws Exception {
		System.out.println("test locak");
		DistributedLockFramework tzDirDistributedLockFramework = DistributedLockFramework.getInstance();
		System.out.println("display11111:");
		tzDirDistributedLockFramework.displayResource();
		tzDirDistributedLockFramework.uploadResourceToZookeeper();
		Thread.sleep(3000);
		System.out.println("test 2");
		System.out.println("display222----------------------------:");
		tzDirDistributedLockFramework.displayResource();
		// tzDirDistributedLockFramework.loadResourcetoZookeeper("3", "del",
		// "3");
		Thread.sleep(3000);
		System.out.println("display----------------------------:");
		tzDirDistributedLockFramework.displayResource();
	}

	public void testLock() {
		DistributedLockFramework tzDirDistributedLockFramework = DistributedLockFramework.getInstance();
		// InterProcessMutex lock =
		// tzDirDistributedLockFramework.getInterProcessMutex("/8");
		//
		// try {
		// tzDirDistributedLockFramework.uploadResourceToZookeeper();
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		//
		System.out.println("start lock====");
		long startTime = System.currentTimeMillis();
		DistributedLock lock = tzDirDistributedLockFramework.acquire();
		boolean flag = true;
		String name = null;
		if (lock == null) {
			flag = false;
		} else {
			name = lock.getSource();
		}
		try {
			// lock.release();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// try {
		// System.out.println(lock.acquire(10, TimeUnit.SECONDS) +
		// "  (10, TimeUnit.SECONDS)");
		// // System.out.println(lock.acquire());
		// // lock.release();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		long endTime = System.currentTimeMillis();
		System.out.println("end lock===,souce name is " + name);
		System.out.println(flag + " lock time is " + (endTime - startTime) / 1000.f + "s");
		// tzDirDistributedLockFramework.acquire();
		// tzDirDistributedLockFramework.acquire();
		// tzDirDistributedLockFramework.acquire();
		// try {
		// Thread.sleep(50000000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public void clearallLock() {
		DistributedLockFramework.getInstance().clearAllLock();
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		DistributedLockTest test = new DistributedLockTest();
		try {
			test.testLock();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		test.testLockResource();
		// test.clearallLock();
		System.out.println("--------close-------");
	}

}
