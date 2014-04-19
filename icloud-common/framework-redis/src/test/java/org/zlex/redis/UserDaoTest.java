package org.zlex.redis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icloud.framework.redis.test.dao.SpringRedisDao;

public class UserDaoTest {
	private ApplicationContext app;
	private SpringRedisDao springRedisDao;

	@Before
	public void before() throws Exception {
		app = new ClassPathXmlApplicationContext("redisapplicationContext.xml");
		springRedisDao = (SpringRedisDao) app.getBean("springRedisDao");
	}

	@Test
	public void crud() {
		// -------------- Create ---------------
		String uid = "u123456";
		String address1 = "上海";
		User user = new User();
		user.setAddress(address1);
		user.setUid(uid);
		springRedisDao.save(uid, user);

		// ---------------Read ---------------
		user = (User) springRedisDao.read(uid);

		assertEquals(address1, user.getAddress());

		// --------------Update ------------
		String address2 = "北京";
		user.setAddress(address2);
		springRedisDao.save(uid, user);

		user = (User) springRedisDao.read(uid);
		System.out.println(user.getAddress());
		assertEquals(address2, user.getAddress());

		// --------------Delete ------------
		springRedisDao.delete(uid);
		user = (User) springRedisDao.read(uid);
		assertNull(user);
	}

	@Test
	public void testExpire() {
		String uid = "aaaa";
		String address1 = "上海";
		User user = new User();
		user.setAddress(address1);
		user.setUid(uid);
		springRedisDao.save(uid, user, 10);
		springRedisDao.save(uid, user);
		user = (User) springRedisDao.read(uid);
		if (user != null) {
			System.out.println(user.getAddress());
		} else {
			
			System.out.println("it is null");
		}
	}
}
