package com.travelzen.framework.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

@Deprecated
/**
 * use  TZUtil instead
 *
 */
public class YRUtil {
	

	public static boolean arrayIsEmpty(String[] strs) {
		if (strs == null || strs.length == 0) {
			return true;
		}
		return false;
	}


 
 
	 

	/**
	 * 处理Url与其参数的组合
	 * 
	 * @param url页面Url
	 * @param param被加入到该Url后的参数
	 * @return 一个完整的Url,包括参数
	 */
	public static String dealUrl(String url, String param) {
		String orgUrl = url;
		url = StringUtil.removeEnd(url, '#');
		url = StringUtil.removeEnd(url, '?');
		if (StringUtils.isBlank(url)) {
			return "";
		}

		if (url.lastIndexOf('/') == (url.length() - 1)) {
			url += "index.html";
		}

		if (StringUtils.isBlank(param)) {
			return orgUrl;
		}

		param = StringUtil.removeStart(param, '&');
		param = StringUtil.removeStart(param, '?');
		if (StringUtils.isBlank(param)
				|| (param.indexOf("=") == -1)
				|| (param.indexOf("=") > 0 && (param.indexOf("=") == (param
						.length() - 1)))) {
			return url;
		}
		if (url.indexOf("?") > 0) {
			url += "&" + param;
		} else {
			url += "?" + param;
		}
		return url;
	}
 
	/**
	 * 得到指定符号前或后的字符
	 */
	private static String getPreOrSufStr(String str, int action,
			String splitSign) {
		if (str == null || str.equals(""))
			return "";
		int index = -1;
		if ((index = str.indexOf(splitSign)) != -1) {
			if (action == 0)
				return str.substring(index + 1).trim();
			return str.substring(0, index).trim();
		}
		return str;
	}

	/**
	 * 得到指定符号前的字符
	 */
	public static String getPreStr(String str, String splitSign) {
		return getPreOrSufStr(str, 1, splitSign);
	}

	/**
	 * 得到指定符号后的字符
	 */
	public static String getSufStr(String str, String splitSign) {
		return getPreOrSufStr(str, 0, splitSign);
	}

	/**
	 * 在不足len位的数字前面自动补零
	 */
	public static String getLimitLenStr(String str, int len) {
		if (str == null) {
			return "";
		}
		while (str.length() < len) {
			str = "0" + str;
		}
		return str;
	}

	/**
	 * 字符串GBK到UTF-8码的转化
	 * 
	 * @param inStr
	 *            GBK编码的字符串
	 * @return UTF-8编码的字符串
	 */
	public static String wapGbkToUtf(String inStr) {
		char temChr;
		int ascInt;
		int i;
		String result = new String("");
		if (inStr == null) {
			inStr = "";
		}
		for (i = 0; i < inStr.length(); i++) {
			temChr = inStr.charAt(i);
			ascInt = temChr + 0;
			if (ascInt > 255) {
				result = result + "&#x" + Integer.toHexString(ascInt) + ";";
			} else {
				result = result + temChr;
			}
		}
		return result;
	}

	/**
	 * URL转码
	 */
	public static String encodeStr(String str, String en) {
		try {
			return URLEncoder.encode(str, en);
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * URL转码
	 */
	public static String encodeStr(String str) {
		try {
			if (StringUtils.isBlank(str)) {
				str = "";
			}

			return URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * URL解码
	 */
	public static String decodeStr(String str) {
		return decodeStr(str, "utf-8");
	}

	/**
	 * URL解码
	 */
	public static String decodeStr(String str, String en) {
		try {
			return URLDecoder.decode(str, en);
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * EMAIL验证
	 */
	public static boolean isMail(String str) {
		String check = "^([a-z0-9A-Z]+[-|\\._]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(str);
		boolean isMatched = matcher.matches();
		if (isMatched) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证手机号
	 */
	public static boolean isTelNo(String telno) {
		String expression = "(^13\\d{9}$|^15[8-9]{1}\\d{8}$)";
		return (isValidate(telno, expression));
	}

	/**
	 * 特殊字符替换
	 */
	public static String replaceStrHtml(String inStr) {
		String result = inStr;
		if (result != null && "".equals(result)) {
			result = result.replaceAll("\r\n", "<br>");
			result = result.replaceAll(" ", "&nbsp;");
		}
		return result;
	}
 

	/**
	 * 取字符的前几位 value,n
	 */
	public static String getSubString(String value, int i) {
		if (StringUtils.isBlank(value)) {
			return "";
		}
		if (value.length() <= i) {
			return value;
		} else {
			return value.substring(0, i) + "...";
		}
	}

	// XuePeng.Duan
	// 2008-01-25

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



	public static ClassLoader getStandardClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public static ClassLoader getFallbackClassLoader() {
		return YRUtil.class.getClassLoader();
	}

	/**
	 * 得到配置文件的URL
	 * 
	 * @param name
	 * @return 配置文件URL
	 * 
	 */
	public static URL getConfURL(String name) {
		try {
			return getStandardClassLoader().getResource(name);
		} catch (Exception e) {
			try {
				return getFallbackClassLoader().getResource(name);
			} catch (Exception ex) {
				return null;
			}
		}
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

	  
 

	/**
	 * @Description:截取字符串并以"..."结尾
	 * @param inputText
	 *            输入内容
	 * @param length
	 *            截取字节数
	 * @return 截取后的字符串
	 */
	public static String trimStr(String inputText, int length) {
		// inputText = "[转贴] 独立Wap发展应以内容为王 ";// 输入字符
		int len = 0;
		if (length < 0) {
			length = 24;
		}
		char[] charArray = inputText.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < charArray.length; i++) {
			char cn = charArray[i];
			byte[] bytes = (String.valueOf(cn)).getBytes();
			len = len + bytes.length;
			if (len > length) {
				sb.append("...");
				break;
			}
			sb.append(cn);

		}
		return sb.toString();
	}

	public static double division(double numerator, double denominator,
			int digit) {
		double result = 0;
		result = Math.round((numerator / denominator) * Math.pow(10, digit))
				/ Math.pow(10, digit);
		return result;
	}

 

	// 测试、
	public static void main(String[] args) {

		// System.out.println("=====4月份  " + (49 + 4 - 28 + 19) % 9);
		// System.out.println("=====5月份  " + (49 + 5 - 28 + 19) % 9);
		// System.out.println("=====6月份  " + (49 + 6 - 28 + 19) % 9);
		// System.out.println("=====7月份  " + (49 + 7 - 28 + 19) % 9);
		// System.out.println("=====8月份  " + (49 + 8 - 28 + 19) % 9);
		// System.out.println("=====9月份  " + (49 + 9 - 28 + 19) % 9);
		// System.out.println("=====10月份  " + (49 + 10 - 28 + 19) % 9);
		//
		// System.out.println(trimStr("即进即送现金100即进即送现金100即进即送现金100即进即送现金100",24));

	}
}
