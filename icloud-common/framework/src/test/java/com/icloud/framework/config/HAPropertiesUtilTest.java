package com.icloud.framework.config;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icloud.framework.config.HAPropertiesUtil;

public class HAPropertiesUtilTest {
	private static Logger logger = LoggerFactory.getLogger(HAPropertiesUtilTest.class);
	@Test
	public void test_getValue() throws Exception{
		while(true){
			logger.info("value:" + HAPropertiesUtil.getValue("/ads/test/1", "test.properties", "key", "default"));
			Thread.sleep(1000 * 5);
		}
	}

}
