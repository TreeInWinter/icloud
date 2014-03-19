package com.travelzen.framework.service;

import java.net.URL;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

import org.springframework.beans.factory.InitializingBean;

import net.sf.ehcache.Cache;

public class CacheServiceImpl   implements InitializingBean  {
	
	

	public CacheServiceImpl(Cache cache) {
	}

	CacheManager manager ;

	Ehcache memoryOnlyCache;

	Cache cache;
	

	@Override
	public void afterPropertiesSet() throws Exception {
		
		URL url = getClass().getResource("/ehcache.xml");
		
		CacheManager manager = CacheManager.create(url);
		
		
		memoryOnlyCache = manager.getEhcache("memoryOnlyCache");
//		memoryOnlyCache = new net.sf.ehcache.Cache("testCache", 500000, false,
//				false, Integer.MAX_VALUE, Integer.MAX_VALUE);
		memoryOnlyCache.initialise();
		

//		this.s
//		this = new JCache(memoryOnlyCache);

	}

}
