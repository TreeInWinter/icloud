package com.travelzen.framework.redis.test;

import com.travelzen.framework.redis.client.SpringRedisClient;

public class SpringRedisClientTest {
	public static void crud() {
		// -------------- Create ---------------
		String uid = "u123456";
		String address1 = "上海2";
		SpringRedisClient.getInstance().save(uid, address1);
		String object = SpringRedisClient.getInstance().get(uid);
		System.out.println(object);
	}

	public static void main(String args[]) {
		crud();
	}
}
