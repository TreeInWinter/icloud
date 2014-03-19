package com.travelzen.framework.timewindow;

import java.util.List;

import com.travelzen.framework.timewindow.actor.String2LongCounterActor;

public class EvictExpiredElementsTask implements Runnable {

	List<ITimeWindow> windows;
	
	String2LongCounterActor mapTimeWindowActor = new String2LongCounterActor();

	public EvictExpiredElementsTask(List<ITimeWindow> windows) {
		this.windows = windows;
	}

	@Override
	public void run() {
		
		for(ITimeWindow w: windows){
			w.evictFrames();
		}


	}
}
