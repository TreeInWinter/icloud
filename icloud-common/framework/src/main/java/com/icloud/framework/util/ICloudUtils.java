package com.icloud.framework.util;

import java.util.Collection;

public class ICloudUtils {

	public static <T> boolean isEmpty(Collection<T> collection) {
		if (collection == null)
			return true;
		if (collection.size() == 0)
			return true;
		return false;
	}

	public static boolean isNotNull(String str) {
		if (str == null || str.trim().length() == 0)
			return false;
		return true;
	}

	public static int parseInt(String str) {
		int id = -1;
		try {
			id = Integer.parseInt(str);
		} catch (Exception e) {
			id = -1;
		}
		return id;
	}

	public static boolean isNotNull(Object object) {
		return object == null ? true : false;
	}

}
