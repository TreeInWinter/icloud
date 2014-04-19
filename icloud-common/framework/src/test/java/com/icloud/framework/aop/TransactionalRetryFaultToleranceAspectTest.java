package com.icloud.framework.aop;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
@ContextConfiguration(locations = {
		"classpath*:spring/framework-bean.xml",
		"classpath*:spring/test-framework-bean.xml"
		 })
public class TransactionalRetryFaultToleranceAspectTest extends AbstractJUnit4SpringContextTests{
	@Resource
	private TransactionalBean transactionalBean;
	@Test
	public void test() throws Exception{
		transactionalBean.transactionalOp();
	}

}
