package com.travelzen.framework.core.util;

import org.apache.commons.lang3.BooleanUtils;

public class BooleanUtil {

	/**
	 *  if the given string is not    true/false,  on/off,     return defaultValue
	 *  if the given string is null  ,  return defaultValue
	 * @param s
	 * @param defaultValue
	 * @return
	 */
	static public boolean parseBooleanDefaultIfNull(String s, boolean defaultValue) {

		Boolean ret = BooleanUtils.toBooleanObject(s);

		if (ret == null) {
			return defaultValue;
		}

		return ret.booleanValue();

	}
}
