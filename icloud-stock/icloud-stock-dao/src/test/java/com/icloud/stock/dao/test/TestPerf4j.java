/**
 * 
 * Description: 
 * Copyright (c) 2013
 * Company:真旅网
 * @author renshui
 * @version 1.0
 * @date 2013-8-13
 */
package com.icloud.stock.dao.test;

import org.junit.Test;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestPerf4j {

	@Test
	public void test() throws Exception {
		StopWatch stopWatch = new Slf4JStopWatch();
		Thread.sleep((long) (Math.random() * 1000L));
		stopWatch.stop();
		Logger logger = LoggerFactory.getLogger(TestPerf4j.class);
		logger.info("sdafadf");
	}
}
