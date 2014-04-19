package com.icloud.framework.redis.test.client;

import java.io.Serializable;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icloud.framework.redis.test.dao.SpringRedisDao;

public class SpringRedisClient {
	private ApplicationContext app;
	private SpringRedisDao springRedisDao;

	// singleton
	private static class SingletonHolder {
		static final SpringRedisClient INSTANCE = new SpringRedisClient();
		static {
			INSTANCE.init();
		}
	}

	public static SpringRedisClient getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public void init() {
		app = new ClassPathXmlApplicationContext("spring/redis.xml");
		springRedisDao = (SpringRedisDao) app.getBean("springRedisDao");
	}

	public void save(String key, Serializable object) {
		// System.out.println("保存reids:"+key);
		springRedisDao.save(key, object);
	}

	public void save(String key, Serializable object, long expireTime) {
		// System.out.println("保存reids:"+key);
		springRedisDao.save(key, object, expireTime);
	}

	public String getKey(int type, String key) {
		if (key != null) {
			switch (type) {
			case RedisServiceKey.HOTEL_CREME_HOTELINFO_TYPE:
				key = RedisServiceKey.HOTEL_CREME_HOTELINFO + key;
				break;
			case RedisServiceKey.FLIGHT_AIRLINE_AUTOCOMPLETE_TYPE:
				key = RedisServiceKey.FLIGHT_AIRLINE_AUTOCOMPLETE + key;
				break;
			case RedisServiceKey.FLIGHT_CITYCODE_AUTOCOMPLETE_TYPE:
				key = RedisServiceKey.FLIGHT_CITYCODE_AUTOCOMPLETE + key;
			default:
				break;
			}
		}
		return key;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) springRedisDao.read(key);
	}

	public Object read(String key) {
		return springRedisDao.read(key);
	}

	public void delete(String key) {
		springRedisDao.delete(key);
	}
	public SpringRedisDao getSpringRedisDao(){
		return springRedisDao;
	}
}
