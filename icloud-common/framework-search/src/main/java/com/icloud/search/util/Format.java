package com.icloud.search.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Format {
	public static SimpleDateFormat f = new SimpleDateFormat("yyyy年MM月dd日");

	public static String timeFormat(Date date) {
		return f.format(date);
	}

	public static String fileSizeFommat(Integer size) {
		String str = null;
		if (size < 0) {
			str = "0KB";
			return str;
		}
		double result = 0.0;
		String suffix = null;
		if (size < 1024 * 1024) {
			suffix = "KB";
			result = (double) size / (double) (1024);
		} else if (size < 1024 * 1024 * 1024) {
			suffix = "MB";
			result = (double) size / (double) (1024 * 1024);
		} else {
			suffix = "GB";
			result = (double) size / (double) (1024 * 1024 * 1024);
		}
		String resultStr = Double.toString(result);
		int i = resultStr.indexOf(".");
		int len = resultStr.length();
		if ((i + 4) < len)
			resultStr = resultStr.substring(0, i + 2);
		return resultStr + suffix;
	}

	public static String subChineseOrEnglish(String str, int n) {
		if (str == null)
			return null;
		if (n < 1)
			return str;
		int len = str.length();
		// int m = len * 7;
		// // if (m < n)
		// return str;
		StringBuffer tmpStr = new StringBuffer();
		int i = 0;
		int s;
		String utemp = "";
		int l;
		int j = 0;
		char[] strBuffer = str.toCharArray();
		while (i < len) {
			s = (int) strBuffer[i];
			utemp = Integer.toHexString(s).toUpperCase();
			l = utemp.length();
			if (l <= 2) {
				utemp = "00" + utemp;
				tmpStr.append(str.substring(i, i + 1));
				j += 4;
			} else {
				tmpStr.append(str.substring(i, i + 1));
				j += 9;
			}
			if (j >= n) {
				break;
			}
			i++;
		}
		return tmpStr.toString();
	}
	public static int getNumberOfChinese(String str) {
		if (str == null)
			return 0;
		int len = str.length();
		char[] strBuffer = str.toCharArray();
		int i = 0;
		int number = 0;
		while (i < len) {
			char s = strBuffer[i];
			String utemp = Integer.toHexString(s).toUpperCase();
			int l = utemp.length();
			if (l <= 2) {
			} else {
				number++;
			}
			i++;
		}
		return number;
	}
}
