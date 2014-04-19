/**
 * Aug 29, 2012
 */
package com.icloud.framework.redis.test.dao;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * 
 * @author jiangning.cui
 * @version 1.0
 * @since 1.0
 */
public interface SpringRedisDao {
	/**
	 */
	void save(String key, Serializable object);

	/**
	 * @param key
	 * @return
	 */
	Object read(String key);

	/**
	 * @param key
	 */
	void delete(String key);

	/**
	 * save expire
	 * 其中expireTime以秒为单位
	 */
	void save(String key, Serializable object, long expireTime);
	/**
	 * 返回RedisTemplate
	 * @return
	 */
	RedisTemplate<Serializable, Serializable> getRedisTemplate();
}
