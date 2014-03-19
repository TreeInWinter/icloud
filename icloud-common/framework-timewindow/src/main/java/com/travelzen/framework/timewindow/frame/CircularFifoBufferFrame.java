package com.travelzen.framework.timewindow.frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections15.buffer.CircularFifoBuffer;

public class CircularFifoBufferFrame<T> {

	// CacheManager is not necessary
	// static CacheManager singletonManager = CacheManager.create();

	private CircularFifoBuffer<T> buffer;
	 //Buffer fifo = BufferUtils.synchronizedBuffer(new CircularFifoBuffer());

	@SuppressWarnings("unused")
	private long frameStartTimestamp;

	// normally , it's a relative big value, for example ,5000
	@SuppressWarnings("unused")
	private long frameLengthInMillsec;

	public long getFrameEndTimestamp() {
		return frameEndTimestamp;
	}


	private long frameEndTimestamp ;

	public CircularFifoBufferFrame(String name, int maxElementSize , long frameStartTimestamp,long frameLengthInMillsec ) {

		this.frameStartTimestamp = frameStartTimestamp;
		this.frameLengthInMillsec = frameLengthInMillsec;
		this.frameEndTimestamp = frameStartTimestamp + frameLengthInMillsec;
		 
		buffer = new CircularFifoBuffer<T>(maxElementSize);

	}

	/**
	 *   一个frame时间范围内，  对应key的数字变化总值
	 * @param obj
	 * @throws IOException
	 */
	public void processEntry(T entry)   {
		buffer.add(entry);
	}
	
	
 
	
	
	public List<T> getEntries(){
		
		return new ArrayList<T>(buffer);
 
		
	}
	
	// normally,  no necessary
	public void destory(){
		buffer.clear();
	}
	
	
	

	public void evictExpiredElements() {
	}
}
