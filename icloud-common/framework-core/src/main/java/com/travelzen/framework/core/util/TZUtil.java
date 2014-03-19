package com.travelzen.framework.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 最常用的uti方法, 零散， 暂时不归类
 * 
 * @author liang.wang
 * 
 */
public class TZUtil {
	/**
	 * 出错的详细信息转化为字符串
	 * 
	 * @param e
	 * @return 错误调用栈详情
	 * 
	 */
	public static String stringifyException(Throwable e) {
		StringWriter stm = new StringWriter();
		PrintWriter wrt = new PrintWriter(stm);
		e.printStackTrace(wrt);
		wrt.close();
		return stm.toString();
	}

	/**
	 * 验证正则表达式
	 * 
	 * @param value
	 * @param expression
	 * @return
	 */
	public static boolean isValidate(String value, String expression) {
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(value);
		return matcher.find();
	}

	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static boolean isEmpty(Collection<?> coll) {
		return coll == null || coll.isEmpty();
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	public static boolean isEmpty(byte[] arr) {
		return arr == null || arr.length == 0;
	}

	public static boolean isEmpty(short[] arr) {
		return arr == null || arr.length == 0;
	}

	public static boolean isEmpty(int[] arr) {
		return arr == null || arr.length == 0;
	}

	public static boolean isEmpty(long[] arr) {
		return arr == null || arr.length == 0;
	}

	public static boolean isEmpty(float[] arr) {
		return arr == null || arr.length == 0;
	}

	public static boolean isEmpty(double[] arr) {
		return arr == null || arr.length == 0;
	}

	public static boolean isEmpty(Object[] arr) {
		return arr == null || arr.length == 0;
	}
}
