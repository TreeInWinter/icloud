package com.travelzen.framework.core.dict;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayOrderSourceTest {
	
	private static Logger logger = LoggerFactory.getLogger(PayOrderSourceTest.class);
	
	@Test
	public void test() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		for(PayOrderSource source : PayOrderSource.values())
			logger.info(source.getValue());
	}

}
