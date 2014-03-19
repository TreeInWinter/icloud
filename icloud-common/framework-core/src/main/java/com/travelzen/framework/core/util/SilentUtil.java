package com.travelzen.framework.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 这个工具类的特点是，里面的所有的转换方法都在转换失败的时候会有特殊的处理，而不会抛出任何异常。
 * 如果，想在参数是null的情况下才做特殊处理，而对于其他的异常则正常抛出，那么请使用
 * {@link com.travelzen.framework.core.util.NullableUtil}。
 * <br><br>
 * 通常情况下，强烈不建议使用这个工具类。只允许在非常确定不需要处理异常的情况下使用。如果不确定的话，请使用其他会抛异常的工具类。
 * <br>
 * @author chengwen.li
 */
public class SilentUtil {
	private final static Logger logger = LoggerFactory.getLogger(SilentUtil.class);

	/**
	 * 将str转换为Integer，并返回。如果参数是null，或者转换失败，则返回null。
	 * 异常情况需要返回默认值时，请使用 apache NumberUtils 的 toInt 方法
	 */
	public static Integer toInteger(String str) {
		if (str == null) {
			logger.warn("str is null");
			return null;
		}
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			logger.warn(e.toString());
			return null;
		}
	}

	/**
	 * 将str转换为Long，并返回。如果参数是null，或者转换失败，则返回null。
	 * 异常情况需要返回默认值时，请使用 apache NumberUtils 的 toLong 方法
	 */
	public static Long toLong(String str) {
		if (str == null) {
			logger.warn("str is null");
			return null;
		}
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			logger.warn(e.toString());
			return null;
		}
	}

	/**
	 * 将str转换为Float，并返回。如果参数是null，或者转换失败，则返回null。
	 * 异常情况需要返回默认值时，请使用 apache NumberUtils 的 toFloat 方法
	 */
	public static Float toFloat(String str) {
		if (str == null) {
			logger.warn("str is null");
			return null;
		}
		try {
			return Float.parseFloat(str);
		} catch (Exception e) {
			logger.warn(e.toString());
			return null;
		}
	}

	/**
	 * 将str转换为Double，并返回。如果参数是null，或者转换失败，则返回null。
	 * 异常情况需要返回默认值时，请使用 apache NumberUtils 的 toDouble 方法
	 */
	public static Double toDouble(String str) {
		if (str == null) {
			logger.warn("str is null");
			return null;
		}
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			logger.warn(e.toString());
			return null;
		}
	}

	/**
	 * 将str转换为Byte，并返回。如果参数是null，或者转换失败，则返回null。
	 * 异常情况需要返回默认值时，请使用 apache NumberUtils 的 toByte 方法
	 */
	public static Byte toByte(String str) {
		if (str == null) {
			logger.warn("str is null");
			return null;
		}
		try {
			return Byte.parseByte(str);
		} catch (Exception e) {
			logger.warn(e.toString());
			return null;
		}
	}
	
	/**
	 * 将str转换为Short，并返回。如果参数是null，或者转换失败，则返回null。
	 * 异常情况需要返回默认值时，请使用 apache NumberUtils 的 toShort 方法
	 */
	public static Short toShort(String str) {
		if (str == null) {
			logger.warn("str is null");
			return null;
		}
		try {
			return Short.parseShort(str);
		} catch (Exception e) {
			logger.warn(e.toString());
			return null;
		}
	}
}
