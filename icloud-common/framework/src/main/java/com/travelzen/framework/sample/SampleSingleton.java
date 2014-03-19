package com.travelzen.framework.sample;

import org.springframework.beans.factory.InitializingBean;

/**
 * support both no-spring-style "JProxyHDFSWriter. getInstance()" and
 * spring-style "<bean JProxyHDFSWriter scope="singleton">"
 * 
 * @author liangwang
 * 
 * when you need to create a new singleton, you could just copy these code
 * and change the "SampleSingleton" to a cetain name,
 * 
 * if you don't want to depend on spring , just remove the InitializingBean relevent issue
 * 
 *  if you only use it in spring-context , just remove the SingletonHolder issue
 */
public class SampleSingleton implements InitializingBean {

	// singleton
	private static class SingletonHolder {
		static final SampleSingleton INSTANCE = new SampleSingleton();
		static {
			INSTANCE.init();
		}
	}

	public static SampleSingleton getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public void init() {

	}

	public static void main(String argc[]) {

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		SingletonHolder.INSTANCE.init();
	}

}
