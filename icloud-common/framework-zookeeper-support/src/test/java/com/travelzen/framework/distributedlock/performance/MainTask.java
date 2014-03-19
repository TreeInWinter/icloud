package com.travelzen.framework.distributedlock.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.travelzen.framework.distributedLock.DistributedLock;
import com.travelzen.framework.threadpool.TaskThreadPool;

public class MainTask {
	public static Map<String, Integer> map = new ConcurrentHashMap<String, Integer>();
	static int count = 0;

	public static void incrence(DistributedLock lock) {
		if (lock != null) {
			Integer in = map.get(lock.getSource());
			if (in == null) {
				in = 0;
			}
			in += 1;
			map.put(lock.getSource(), in);
		}
	}

	public static void doJob() {
		List<LockTask> list = new ArrayList<LockTask>();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			list.add(new LockTask("name " + i, 500));
		}
		TaskThreadPool pool = new TaskThreadPool(50);
		for (LockTask task : list) {
			// task.start();
			pool.execute(task);
		}
		pool.waitFinish();
		pool.closePool();
		print();
		long end = System.currentTimeMillis();
		System.out.println("time is " + (end - start) / 1000.0f + "s, performace is " + (count * 1000) / ((end - start) * 1.0f) + "s");
	}

	public static void print() {
		System.out.println("-------------start----------------");

		for (Entry<String, Integer> entry : map.entrySet()) {
			System.out.println(entry.getKey() + "  " + entry.getValue());
			count += entry.getValue();
		}
		System.out.println("---------- end -----------");
		System.out.println("count  is " + count);
	}

	public static void main(String[] args) {
		doJob();
	}
}
