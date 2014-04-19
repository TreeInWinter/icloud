package org.zlex.redis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.Serializable;
import java.util.List;

import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.icloud.framework.redis.test.client.SpringRedisClient;

public class SpringRedisClientTest {
	@Test
	public void crud() {
		// -------------- Create ---------------
		String uid = "u123456";
		String address1 = "上海";
		User user = new User();
		user.setAddress(address1);
		user.setUid(uid);
		SpringRedisClient.getInstance().save(uid, user);

		// ---------------Read ---------------
		user = (User) SpringRedisClient.getInstance().read(uid);
		
		assertEquals(address1, user.getAddress());

		// --------------Update ------------
		String address2 = "北京";
		user.setAddress(address2);
		SpringRedisClient.getInstance().save(uid, user);

		user = (User) SpringRedisClient.getInstance().read(uid);
		System.out.println(user.getAddress());
		assertEquals(address2, user.getAddress());

		// --------------Delete ------------
		SpringRedisClient.getInstance().delete(uid);
		user = (User) SpringRedisClient.getInstance().read(uid);
		assertNull(user);
		final RedisTemplate<Serializable, Serializable> redisTemplate = SpringRedisClient.getInstance().getSpringRedisDao().getRedisTemplate();
		final String key = "ETERM:QTE:testTrace";
		List<String> logs = redisTemplate.execute(new RedisCallback<List<String>>() {
			@Override
			public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
				try {
					redisTemplate.setValueSerializer(new StringRedisSerializer());
					Object obj = redisTemplate.opsForList().range(key, 0, -1);
					return (List<String>)obj;
				} catch (Exception e) {
				}
				return null;
			}
		});
		System.out.println(logs);
	}
}
