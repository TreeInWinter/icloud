package com.icloud.framework.core.util;

import java.math.BigDecimal;
import java.util.Date;

import org.joda.time.DateTime;

/**
 * 这个工具类的特点是，里面的所有的转换方法都不会因为参数的问题抛出 NullPointerException，即允许参数为空。
 * 但是如果存在其他数据错误，还是会抛出相应的异常。如 NumberFormatException 等。
 * 如果，希望任何情况下都不抛出异常，请使用 {@link com.travelzen.framework.core.util.SilentUtil}。
 * <br><br>
 * 因为null值，或者说是不抛空指针异常，可能能会导致不可预知的错误，所以，这个工具类，只允许在非常确定返回值可以是空的情况下使用。
 * 如果不确定的话，请使用其他会抛异常的工具类。
 * <br>
 * @author chengwen.li
 */
public class NullableUtil {

	/**
	 * 如果参数是"Y"，则返回true，否则一律返回false
	 */
	public static boolean toBool(String str) {
		String[] trueArray = {"y", "yes", "true"};
		for (String s : trueArray) {
			if (s.equalsIgnoreCase(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 如果参数是 'Y'，则返回true，否则一律返回false
	 */
	public static boolean toBool(Character ch) {
		return ch == null || ch != 'Y' || ch != 'y' ? false : true;
	}

	/**
	 * 如果参数非0，则返回true，否则一律返回false
	 */
	public static boolean toBool(Integer i) {
		return i == null || i == 0 ? false : true;
	}

	/**
	 * 将封装类型的布尔值转换为基本类型，如果参数是null，则返回false
	 */
	public static boolean toBool(Boolean b) {
		return b == null ? false : b.booleanValue();
	}

	/**
	 * 如果参数为null，返回0
	 */
	public static double doubleValue(Double d) {
		return d == null ? 0 : d.doubleValue();
	}

	public static DateTime toJodaTime(Date date) {
		return date == null ? null : new DateTime(date.getTime());
	}

	/**
	 * 如果参数是空，或者长度是0，则返回'\0'，否则返回第一个字符
	 */
	public static char getFirstChar(String str) {
		if (str == null || str.length() == 0) {
			return '\0';
		}
		return str.charAt(0);
	}

	/**
	 * 将金额，从“元”转换为“分”，也就是乘100。如果，参数是null，则返回null;
	 */
	public static Long toCent(BigDecimal yuan) {
		return yuan == null ? null : MoneyUtil.yuan2Cent(yuan);
	}

	/**
	 * 根据枚举的字面名称转换。举例说明，假如有两个枚举：EnumA { ASDF } 和 EnumB { ASDF }，
	 * 那么就可以把 EnumA.ASDF 转换为 EnumB.ASDF。如果转换失败则返回空。
	 * @param src 转换的源枚举
	 * @param destClass 转换的目的类型
	 */
	public static <S extends Enum<S>, D extends Enum<D>> D convertEnumByName(Enum<S> src, Class<D> destClass) {
		if (src == null) {
			return null;
		}
		try {
			return Enum.valueOf(destClass, src.name());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
