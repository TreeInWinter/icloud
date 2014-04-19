package com.icloud.framework.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanLoader {
	// private static final String CONTEXT_PATH = "spring/spring.xml";
	private static final String CONTEXT_PATH = "classpath*:spring/applicationContext-*.xml";
	private static ApplicationContext svContext = null;

	private static class SingletonHolder {
		private static final BeanLoader INSTANCE = new BeanLoader();
		static {
			INSTANCE.init();
		}
	}

	public static BeanLoader getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public Object getBean(String pBeanName) {
		Object lvObj = svContext.getBean(pBeanName);
		return lvObj;
	}

	public void init() {
		// TODO Auto-generated method stub
		svContext = new ClassPathXmlApplicationContext(CONTEXT_PATH);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object getBean(String pBeanName, Class pBeanClass) {
		Object lvObj = svContext.getBean(pBeanName, pBeanClass);
		return lvObj;
	}

}
