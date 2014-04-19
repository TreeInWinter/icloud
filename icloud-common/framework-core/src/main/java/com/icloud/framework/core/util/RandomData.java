package com.icloud.framework.core.util;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 随机测试数据生成工具类.
 * 
 */
public class RandomData {

	static Logger logger = LoggerFactory.getLogger(RandomData.class);
	
	private static Random random = new Random();

	private RandomData() {
	}
	
	
	public static<T>   T getRandomElementInCollection (List<T> list){
		
		if(list.size()==0){
			logger.error("size can't be zero");
			return null;
		}
		
		int idx =  new Random().nextInt( list.size());
		return list.get(idx);
	}
	
	public static <T>  T getRandomElementInCollection(T [] list){
		
		if(list.length == 0){
			logger.error("size can't be zero");
			return null;
		}
		
		int idx =  new Random().nextInt( list.length);
		return list[idx];
	}
	

	/**
	 * 返回随机ID.
	 */
	public static long randomId() {
		return random.nextLong();
	}

	/**
	 * 返回随机名称, prefix字符串+5位随机数字.
	 */
	public static String randomName(String prefix) {
		return prefix + random.nextInt(10000);
	}

	/**
	 * 从输入list中随机返回一个对象.
	 */
	public static <T> T randomOne(List<T> list) {
		return randomSome(list, 1).get(0);
	}

	/**
	 * 从输入list中随机返回n个对象.
	 */
	public static <T> List<T> randomSome(List<T> list, int n) {
		Collections.shuffle(list);
		return list.subList(0, n);
	}

	/**
	 * 从输入list中随机返回随机个对象.
	 */
	public static <T> List<T> randomSome(List<T> list) {
		int size = random.nextInt(list.size());
		if (size == 0) {
			size = 1;
		}
		return randomSome(list, size);
	}
}
