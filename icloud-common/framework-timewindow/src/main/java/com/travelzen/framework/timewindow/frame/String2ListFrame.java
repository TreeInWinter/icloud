package com.travelzen.framework.timewindow.frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.MemoryUnit;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration.Strategy;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.op4j.Op;
import org.op4j.functions.ExecCtx;
import org.op4j.functions.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class String2ListFrame<T> {

	private static Logger logger = LoggerFactory
			.getLogger(String2ListFrame.class);

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
		return "EhcacheFrame [cacheFrame=" + getAll() + "]";
	}

	public String2ListFrame(String name, int maxMagebytes,
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
	public void add(String key, T value) {
		logger.debug("before cache kv:{}-{}",key, value);
		Element element = cacheFrame.get(key);
		if (element == null) {
			ArrayList<T> newList = new ArrayList<T>();
			newList.add(value);
			element = new Element(key, newList, false/* not eternal */,
					Integer.MAX_VALUE/* not evicted for idle */,
					Integer.MAX_VALUE);
			cacheFrame.put(element);
			 logger.debug("after cache(insert) kv:{}-{}",key, value);
		} else {
			@SuppressWarnings("unchecked")
			ArrayList<T> oldList = ((ArrayList<T>) element
					.getObjectValue());
			oldList.add(value);
			cacheFrame.put(element);
			 logger.debug("after cache(inc) kv:{}-{}",key, value);
		}
	}

	/**
	 * 一个frame时间范围内， 对应key的数字变化总值
	 * 
	 * @param obj
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, ArrayList<T>> getAll() {
		final Map<String, ArrayList<T>> ret = Maps.newHashMap();
		Op.on(cacheFrame.getKeys()).map(new IFunction<String, Void>() {
			@Override
			public Void execute(final String key, final ExecCtx ctx)
					throws Exception {
				final ArrayList<T> value = ((ArrayList<T>) cacheFrame
						.get(key).getObjectValue());
				ret.put(key, value);
				return null;
			}
		}).get();
		return ret;
	}

	public ArrayList<T> get(String key) {
		Element element = cacheFrame.get(key);
		if (element != null) {
			@SuppressWarnings("unchecked")
			ArrayList<T> value = (ArrayList<T>) element
					.getObjectValue();
			return value;
		} else {
			return new ArrayList<T>();
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
