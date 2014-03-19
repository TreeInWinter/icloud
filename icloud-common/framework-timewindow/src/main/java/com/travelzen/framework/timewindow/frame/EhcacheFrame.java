package com.travelzen.framework.timewindow.frame;

import java.io.IOException;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.MemoryUnit;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration.Strategy;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.apache.commons.lang3.mutable.MutableLong;
import org.javatuples.Pair;
import org.op4j.Op;
import org.op4j.functions.ExecCtx;
import org.op4j.functions.IFunction;

import com.google.common.collect.Maps;

public class EhcacheFrame {
	
//	private static Logger logger = LoggerFactory
//			.getLogger(EhcacheFrame.class);

	// CacheManager is not necessary
	static CacheManager singletonCacheManager = CacheManager.create();

	private Cache cacheFrame;

	private long frameStartTimestamp;

	// normally , it's a relative big value, for example ,5000
	@SuppressWarnings("unused")
	private long frameLengthInMillsec;

	public long getFrameEndTimestamp() {
		return frameEndTimestamp;
	}


	private long frameEndTimestamp ;
	

	@Override
	public String toString() {
		return "EhcacheFrame [cacheFrame=" + getStringLongPairs() + "]";
	}

	public EhcacheFrame(String name, int maxMagebytes , long frameStartTimestamp,long frameLengthInMillsec ) {

		this.frameStartTimestamp = frameStartTimestamp;
		this.frameLengthInMillsec = frameLengthInMillsec;
		this.frameEndTimestamp = frameStartTimestamp + frameLengthInMillsec;
		 
		cacheFrame = new net.sf.ehcache.Cache(new CacheConfiguration("name", 0)
				.eternal(false)
				.persistence(
						(new PersistenceConfiguration())
								.strategy(Strategy.LOCALTEMPSWAP))
				.statistics(false)
				.maxBytesLocalHeap(maxMagebytes, MemoryUnit.MEGABYTES)
				// evicted when "maxMagebytes" reached
				.memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.CLOCK)

		);

		// .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
		;
		
		cacheFrame.setCacheManager(singletonCacheManager);

		cacheFrame.initialise();

	}

	public long getFrameStartTimestamp() {
		return frameStartTimestamp;
	}

	public void setFrameStartTimestamp(long frameStartTimestamp) {
		this.frameStartTimestamp = frameStartTimestamp;
	}

	/**
	 *   一个frame时间范围内，  对应key的数字变化总值
	 * @param obj
	 * @throws IOException
	 */
	public void processStringLongPair(Pair<String, MutableLong> obj)   {

		String key = obj.getValue0();

		//logger.debug("kv:{}-{}",key, obj.getValue1());
		
		Element element = cacheFrame.get(key);

		if (element == null) {
			element = new Element(key, obj.getValue1(), false/* not eternal */,
					Integer.MAX_VALUE/* not evicted for idle */, Integer.MAX_VALUE);
			cacheFrame.put(element);
		} else {
			
			MutableLong value = ((MutableLong) element.getObjectValue());
			value.add(obj.getValue1());
			
			//logger.debug("inc:{}-{}",key, value.get());
 
		}
	}
	
	

	/**
	 *   一个frame时间范围内，  对应key的数字变化总值
	 * @param obj
	 * @throws IOException
	 */
	public void processString(String key)   {

//		String key = obj.getValue0();

		Element element = cacheFrame.get(key);

		if (element == null) {
			
			element = new Element(key,null, false/* not eternal */,
					Integer.MAX_VALUE/* not evicted for idle */, new Integer(
							(int) (frameEndTimestamp - System
									.currentTimeMillis())));
			
			cacheFrame.put(element);
		} else {
			element.updateAccessStatistics();
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	public Map<String,Long> getStringLongPairs(){
		
		final Map<String,Long> ret = Maps.newHashMap();
		
		Op.on(cacheFrame.getKeys()).map(new IFunction<String,Void>(){
			@Override
			public Void execute(final String key, final ExecCtx ctx) throws Exception {
				
				final long value = ((MutableLong)  cacheFrame.get(key).getObjectValue()).longValue();
				ret.put(key, value);
				
				return null;
			}
		}).get();
		
		return ret;
		
	}
	
	// normally,  no necessary
	public void destory(){
		cacheFrame.dispose();
	}
	
	
	

	public void evictExpiredElements() {
		cacheFrame.evictExpiredElements();
	}
}
