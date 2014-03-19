package com.travelzen.framework.util;

public class HashUtils {
	
	public static long murmurHash2(String text) {
		return MurmurHash2.hash64(text);
	}

}
