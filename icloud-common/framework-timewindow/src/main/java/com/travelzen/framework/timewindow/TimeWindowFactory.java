package com.travelzen.framework.timewindow;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import com.travelzen.framework.timewindow.impl.LatenyCounter;
import com.travelzen.framework.timewindow.impl.LogCounter;
import com.travelzen.framework.timewindow.impl.TimeSectionAggregateCounter;


public class TimeWindowFactory {

	List<ITimeWindow>  windows = Lists.newArrayList();
	int evictMillisec;
	
	ScheduledThreadPoolExecutor expireChecker ;

	public TimeWindowFactory(int evictMillisec) {
		this.evictMillisec = evictMillisec;
		
		  expireChecker = new ScheduledThreadPoolExecutor(
				1, new ThreadFactory() {
					public Thread newThread(Runnable r) {
						Thread t = new Thread(r, "TimeWindowFactory");
						t.setDaemon(false);
						return t;
					}
				});

		expireChecker.scheduleWithFixedDelay(new EvictExpiredElementsTask(
				windows), 0, evictMillisec, TimeUnit.MILLISECONDS);
	 
		
	}

	public TimeSectionAggregateCounter createTimeSectionAggregateCounter(
			final String name, int windowLengthSec, int frameCnt,
			int maxMagebytes) {

		TimeSectionAggregateCounter window = new TimeSectionAggregateCounter(
				name, windowLengthSec, frameCnt, maxMagebytes, evictMillisec);
		windows.add(window);
		return window;
	}

	public LatenyCounter createLatenyCounter(
			final String name, int windowLengthSec, int frameCnt,
			int maxMagebytes) {

		LatenyCounter window = new LatenyCounter(
				name, windowLengthSec, frameCnt, maxMagebytes, evictMillisec);
		windows.add(window);
		return window;
	}
	public LogCounter createLogCounter(
			final String name, int windowLengthSec, int frameCnt,
			int maxMagebytes) {

		LogCounter window = new LogCounter(
				name, windowLengthSec, frameCnt, maxMagebytes, evictMillisec);
		windows.add(window);
		return window;
	}
}
