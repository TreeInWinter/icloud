/**
 * 
 * Description: 
 * Copyright (c) 2013
 * Company:真旅网
 * @author renshui
 * @version 1.0
 * @date 2013-5-29
 */
package com.icloud.tops.framework.redis.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

public class CacheableStubDao {
    private static Logger logger = LoggerFactory.getLogger(CacheableStubDao.class);
    @Cacheable(value="cache2", key="#id")
    public String getValue(String id){
	logger.info("execute method getValue");
	return id;
    }
}
