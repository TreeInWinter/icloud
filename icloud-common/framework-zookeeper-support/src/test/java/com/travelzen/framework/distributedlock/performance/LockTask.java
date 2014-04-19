package com.travelzen.framework.distributedlock.performance;

import com.icloud.framework.distributedLock.DistributedLock;
import com.icloud.framework.distributedLock.DistributedLockFramework;

public class LockTask extends Thread {
	private String name;
	private int num;

	public LockTask(String name, int num) {
		this.name = name;
		this.num = num;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int count = 0;
		for (int i = 0; i < num; i++) {
			doLockJob();
			count++;
			if (count % 100 == 0) {
				System.out.println("name is " + name + " count is " + count + "  running");
			}
		}
		System.out.println("name is " + name + "  ok");
	}

	private void doLockJob() {
		// TODO Auto-generated method stub
		DistributedLock lock = DistributedLockFramework.getInstance().acquire();
		if (lock != null) {
			MainTask.incrence(lock);
			// try {
			// Thread.sleep(1);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			try {
				lock.release();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
