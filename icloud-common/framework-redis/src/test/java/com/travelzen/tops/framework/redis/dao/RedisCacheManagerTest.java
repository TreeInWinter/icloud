/**
 * 
 * Description: 
 * Copyright (c) 2013
 * Company:真旅网
 * @author renshui
 * @version 1.0
 * @date 2013-5-29
 */
package com.travelzen.tops.framework.redis.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath*:test_spring.xml",
	"classpath*:spring/redis.xml"
	 })
public class RedisCacheManagerTest {
    private static Logger logger = LoggerFactory.getLogger(RedisCacheManagerTest.class);
    @Resource
    private CacheableStubDao dao;
    @Test
    public void test() {
	for(int i = 10; i< 20; i++){
	    try {
		Thread.sleep(3 * 1000);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    logger.info(dao.getValue("test_id_" + i));
	}
    }

}
