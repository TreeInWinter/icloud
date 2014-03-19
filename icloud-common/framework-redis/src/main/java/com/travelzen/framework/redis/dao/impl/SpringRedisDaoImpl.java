/**
 * Aug 29, 2012
 */
package com.travelzen.framework.redis.dao.impl;

import java.io.Serializable;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import com.travelzen.framework.redis.dao.SpringRedisDao;

/**
 * 
 * @author jiangning.cui
 * @version 1.0
 * @since 1.0
 */
@Repository("springRedisDao")
public class SpringRedisDaoImpl implements SpringRedisDao {
	public final static int DEFAULTEXPIRETIME = 1800;// 半小时

	@Resource
	private RedisTemplate<Serializable, Serializable> redisTemplate;

	@Override
	public void save(final String key, final Serializable object) {
		try {
			save(key, object, DEFAULTEXPIRETIME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// redisTemplate.execute(new RedisCallback<Serializable>() {
		// @Override
		// public Serializable doInRedis(RedisConnection connection) throws
		// DataAccessException {
		// @SuppressWarnings("unchecked")
		// RedisSerializer<Serializable> valueSerializer =
		// (RedisSerializer<Serializable>) redisTemplate.getValueSerializer();
		// connection.set(redisTemplate.getStringSerializer().serialize(key),
		// valueSerializer.serialize(object));
		// return null;
		// }
		// });
	}

	@Override
	public Object read(final String key) {
		try {
			return redisTemplate.execute(new RedisCallback<Object>() {
				@Override
				public Object doInRedis(RedisConnection connection) throws DataAccessException {
					try {
						byte[] keybyte = redisTemplate.getStringSerializer().serialize(key);
						if (connection.exists(keybyte)) {
							byte[] value = connection.get(keybyte);
							Object object = redisTemplate.getValueSerializer().deserialize(value);
							return object;
						}
					} catch (Exception e) {
					}
					return null;
				}
			});
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void delete(final String key) {
		try {
			redisTemplate.execute(new RedisCallback<Object>() {
				public Object doInRedis(RedisConnection connection) {
					try {
						return connection.del(redisTemplate.getStringSerializer().serialize(key));
					} catch (Exception e) {
						return null;
					}
				}
			});
		} catch (Exception e) {

		}
	}

	@Override
	public void save(final String key, final Serializable object, final long expireTime) {
		// TODO Auto-generated method stub
		try {
			redisTemplate.execute(new RedisCallback<Serializable>() {
				@Override
				public Serializable doInRedis(RedisConnection connection) throws DataAccessException {
					try {
						@SuppressWarnings("unchecked")
						RedisSerializer<Serializable> valueSerializer = (RedisSerializer<Serializable>) redisTemplate.getValueSerializer();
						connection.set(redisTemplate.getStringSerializer().serialize(key), valueSerializer.serialize(object));
						connection.expire(redisTemplate.getStringSerializer().serialize(key), expireTime);
					} catch (Exception e) {

					}
					return null;
				}
			});
		} catch (Exception e) {

		}
	}

	@Override
	public RedisTemplate<Serializable, Serializable> getRedisTemplate() {
		// TODO Auto-generated method stub
		return redisTemplate;
	}
	
}
