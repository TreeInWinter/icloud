package com.travelzen.search.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Sulid He
 * 
 */
public class DigitFilter {

	public static final String[] CHINESE_DIGITS = { "零", "一", "二", "三", "四",
			"五", "六", "七", "八", "九" };

	public static final String[] ARABIAN_DIGITS = { "0", "1", "2", "3", "4",
			"5", "6", "7", "8", "9" };

	public static final Map<String, String> REPLACE = new HashMap<String, String>();

	public static final Map<String, String> REPLACE_OTHER = new HashMap<String, String>();

	static {
		REPLACE.put("零", "0");
		REPLACE.put("一", "1");
		REPLACE.put("二", "2");
		REPLACE.put("三", "3");
		REPLACE.put("四", "4");
		REPLACE.put("五", "5");
		REPLACE.put("六", "6");
		REPLACE.put("七", "7");
		REPLACE.put("八", "8");
		REPLACE.put("九", "9");

		REPLACE_OTHER.put("0", "零");
		REPLACE_OTHER.put("1", "一");
		REPLACE_OTHER.put("2", "二");
		REPLACE_OTHER.put("3", "三");
		REPLACE_OTHER.put("4", "四");
		REPLACE_OTHER.put("5", "五");
		REPLACE_OTHER.put("6", "六");
		REPLACE_OTHER.put("7", "七");
		REPLACE_OTHER.put("8", "八");
		REPLACE_OTHER.put("9", "九");
	}

	public static final String PATTERN = "[零一二三四五六七八九]+";

	public static final String OTHER_PATTERN = "[0-9]+";

	/**
	 * 将字符串中的中文数字转换成阿拉伯数字形式查询
	 * 
	 * @param source
	 * @return
	 */
	public static String getTransferArabian(String source) {
		Pattern pattern = Pattern.compile(PATTERN);
		Matcher m = pattern.matcher(source);
		while (m.find()) {
			int start = m.start();
			int end = m.end();
			String prefix = source.substring(0, start);
			String suffix = source.substring(end);
			String replace = source.substring(start, end);
			for (String str : CHINESE_DIGITS) {
				replace = replace.replaceAll(str, REPLACE.get(str));
			}
			return prefix + replace + suffix;
		}
		return source;
	}

	/**
	 * 获取汉字样式的字符串
	 * 
	 * @param source
	 * @return
	 */
	public static String getTransferChinese(String source) {
		Pattern pattern = Pattern.compile(OTHER_PATTERN);
		Matcher m = pattern.matcher(source);
		while (m.find()) {
			int start = m.start();
			int end = m.end();
			String prefix = source.substring(0, start);
			String suffix = source.substring(end);
			String replace = source.substring(start, end);
			for (String str : ARABIAN_DIGITS) {
				replace = replace.replaceAll(str, REPLACE_OTHER.get(str));
			}
			return prefix + replace + suffix;
		}
		return source;
	}

	public static void main(String[] args) {
		System.out.println(getTransferChinese("心痛20091998"));
	}
}
