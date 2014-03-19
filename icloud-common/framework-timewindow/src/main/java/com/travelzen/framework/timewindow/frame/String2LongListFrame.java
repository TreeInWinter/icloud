package com.travelzen.framework.timewindow.frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

public class String2LongListFrame {

//	private static Logger logger = LoggerFactory
//			.getLogger(String2LongListFrame.class);

	// CacheManager is not necessary
	static CacheManager singletonCacheManager = CacheManager.create();

	private Cache cacheFrame;

	private long frameStartTimestamp;

	// normally , it's a relative big value, for example ,5000
	@SuppressWarnings("unused")
	private long frameLengthInMillsec;
	private long frameEndTimestamp;

	public long getFrameEndTimestamp() {
		return frameEndTimestamp;
	}

	@Override
	public String toString() {
		return "EhcacheFrame [cacheFrame=" + getStringLongPairs() + "]";
	}

	public String2LongListFrame(String name, int maxMagebytes,
			long frameStartTimestamp, long frameLengthInMillsec) {

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
				.memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.CLOCK));
		// .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU) ;
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
	 * 一个frame时间范围内， 对应key的数字变化总值
	 * 
	 * @param obj
	 * @throws IOException
	 */
	public void processStringLongPair(Pair<String, MutableLong> obj) {
		String key = obj.getValue0();
		// logger.debug("before cache kv:{}-{}",key, obj.getValue1());
		Element element = cacheFrame.get(key);
		if (element == null) {
			ArrayList<MutableLong> value = new ArrayList<MutableLong>();
			value.add(obj.getValue1());
			element = new Element(key, value, false/* not eternal */,
					Integer.MAX_VALUE/* not evicted for idle */,
					Integer.MAX_VALUE);
			cacheFrame.put(element);
			// logger.debug("after cache(insert) kv:{}-{}",key, value);
		} else {
			@SuppressWarnings("unchecked")
			ArrayList<MutableLong> value = ((ArrayList<MutableLong>) element
					.getObjectValue());
			value.add(obj.getValue1());
			cacheFrame.put(element);
			// logger.debug("after cache(inc) kv:{}-{}",key, value);
		}
	}

	/**
	 * 一个frame时间范围内， 对应key的数字变化总值
	 * 
	 * @param obj
	 * @throws IOException
	 */
	// public void processString(String key) {
	//
	// // String key = obj.getValue0();
	//
	// Element element = cacheFrame.get(key);
	//
	// if (element == null) {
	//
	// element = new Element(key,null, false/* not eternal */,
	// Integer.MAX_VALUE/* not evicted for idle */, new Integer(
	// (int) (frameEndTimestamp - System
	// .currentTimeMillis())));
	//
	// cacheFrame.put(element);
	// } else {
	// element.updateAccessStatistics();
	// }
	// }
	//
	@SuppressWarnings("unchecked")
	public Map<String, ArrayList<MutableLong>> getStringLongPairs() {
		final Map<String, ArrayList<MutableLong>> ret = Maps.newHashMap();
		Op.on(cacheFrame.getKeys()).map(new IFunction<String, Void>() {
			@Override
			public Void execute(final String key, final ExecCtx ctx)
					throws Exception {
				final ArrayList<MutableLong> value = ((ArrayList<MutableLong>) cacheFrame
						.get(key).getObjectValue());
				ret.put(key, value);
				return null;
			}
		}).get();
		return ret;
	}

	public long getMaxLatency(String key) {
		Element element = cacheFrame.get(key);
		if (element != null) {
			@SuppressWarnings("unchecked")
			ArrayList<MutableLong> value = (ArrayList<MutableLong>) element
					.getObjectValue();
			return Collections.max(value).longValue();
		} else {
			return 0;
		}
	}

	public ArrayList<MutableLong> getLatencyList(String key) {
		Element element = cacheFrame.get(key);
		if (element != null) {
			@SuppressWarnings("unchecked")
			ArrayList<MutableLong> value = (ArrayList<MutableLong>) element
					.getObjectValue();
			return value;
		} else {
			return null;
		}
	}

	// normally, no necessary
	public void destory() {
		cacheFrame.dispose();
	}

	public void evictExpiredElements() {
		cacheFrame.evictExpiredElements();
	}
}
