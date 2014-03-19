package com.travelzen.framework.core.util;

import java.util.ArrayList;
import java.util.List;

public class EnumUtil {
	public static <T extends Enum<T>> List<String> enum2stringList(List<T> value) {

		List<String> ret = new ArrayList<String>();
		for (T v : value) {
			ret.add(v.name());
		}

		return ret;
	}

	public static <T extends Enum<T>> List<Integer> enum2IntList(List<T> value) {

		List<Integer> ret = new ArrayList<Integer>();
		for (T v : value) {
			ret.add(v.ordinal());
		}
		return ret;
	}
}
