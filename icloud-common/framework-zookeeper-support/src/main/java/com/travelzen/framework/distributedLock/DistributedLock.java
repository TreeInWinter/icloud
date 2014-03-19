package com.travelzen.framework.distributedLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.curator.framework.recipes.locks.InterProcessMutex;

public class DistributedLock {
	private static Logger logger = LoggerFactory.getLogger(DistributedLock.class);

	private InterProcessMutex lock;
	private String source;

	public DistributedLock(InterProcessMutex lock, String source) {
		this.lock = lock;
		this.source = source;
	}

	public void release() throws Exception {
		if (lock != null) {
			try {
				lock.release();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		}
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
