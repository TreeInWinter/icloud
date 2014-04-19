package com.icloud.framework.core.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtil {
	static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

	private StringUtil() {
	}

	/**
	 * a.png -> png a-> ""
	 *
	 * @param urlStr
	 * @return
	 */
	public static String getSuffixOfURL(String urlStr) {
		int dotIdx = urlStr.lastIndexOf(".");
		if (dotIdx > 0 && dotIdx < urlStr.length()) {
			String suffix = urlStr.substring(dotIdx + 1, urlStr.length());
			return suffix;
		} else {
			return "";
		}
	}

	/**
	 * 格式化double类型值，使得其末尾保留两位小数
	 *
	 * @param value
	 * @return
	 */
	public static String formatDouble(double value) {
		DecimalFormat df = new DecimalFormat("######0.00");
		return df.format(value);
	}

	/**
	 * 生成制定位随机数字
	 */
	public static String randomNumeric(int i) {
		return RandomStringUtils.randomNumeric(i);
	}

	/**
	 * 生成制定位随机字母和数字
	 */
	public static String randomAlphanumeric(int i) {
		return RandomStringUtils.randomAlphanumeric(i);
	}

	/**
	 * 将字符串数字转化为int型数字
	 *
	 * @param str被转化字符串
	 * @param defValue转化失败后的默认值
	 * @return int
	 */
	public static int parseInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return defValue;
		}
	}

	/**
	 * 将字符串数字转化为double型数字
	 *
	 * @param str被转化字符串
	 * @param defValue转化失败后的默认值
	 * @return double
	 */
	public static double parseDouble(String str, double defValue) {
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return defValue;
		}
	}

	/**
	 * 检测字符串是否为空
	 */
	public static boolean strIsNull(String str) {
		return ((str == null) || "".equals(str));
	}

	public static String getString(byte[] bytes) {

		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static byte[] getBytes(String str) {

		try {
			byte[] bytes = str.getBytes("UTF-8");
			return bytes;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return new byte[0];
	}

	public static String getMD5(String sValue) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(sValue.getBytes());

			return (new BigInteger(1, messageDigest.digest())).toString(16);
		} catch (Exception e) {
			e.printStackTrace();

			return "";
		}
	}

	/**
	 * 循环删除最后的某个字符，至不是以该字符结尾
	 *
	 * @param value
	 * @return
	 */
	public static String removeEnd(String value, char c) {

		if (StringUtils.isBlank(value)) {
			return "";
		}
		String ret = value;
		while (StringUtils.isNotBlank(ret) && (StringUtils.lastIndexOf(ret, c) == (ret.length() - 1))) {
			ret = StringUtils.removeEnd(ret, String.valueOf(c));
		}
		return ret;
	}

	public static String removeStart(String value, char c) {
		if (StringUtils.isBlank(value)) {
			return "";
		}

		String ret = value;
		while (StringUtils.isNotBlank(ret) && (StringUtils.indexOf(ret, String.valueOf(c)) == 0)) {
			ret = StringUtils.removeStart(ret, String.valueOf(c));
		}
		return ret;
	}

	public static String removeFirstAndEnd(String value, char c) {
		String ret = removeEnd(value, c);
		return removeStart(ret, c);
	}

	/**
	 * use org.apache.commons.lang3.StringUtils.trimToEmpty(str);
	 *
	 * @param sValue
	 * @return
	 */
	// public static String filterNull(String sValue) {
	// return isNull(sValue) ? "" : sValue;
	// }

	// public static String filterNull(BigDecimal sValue) {
	// return isNull(sValue) ? "" : sValue.toString();
	// }
	//
	// public static String filterNull(Long sValue) {
	// return isNull(sValue) ? "" : sValue.toString();
	// }
	//
	// public static String filterNull(Double sValue) {
	// return isNull(sValue) ? "" : sValue.toString();
	// }

	public static String filterNull(Object sValue) {
		return isNull(sValue) ? "" : sValue.toString();
	}

	public static boolean isNull(Object object) {
		return object == null;
	}

	public static boolean isEmpty(String sValue) {
		return isNull(sValue) || "".equals(sValue);
	}

	public static boolean isTrimEmpty(String sValue) {
		return isEmpty(sValue) || "".equals(sValue.trim());
	}

	public static String subStringDot(String sValue, int iLength, String sDot, String sEncoding) {
		byte[] bytes = filterNull(sValue).getBytes();
		if (bytes.length <= iLength) {
			return sValue;
		}

		int iLastLen = iLength - sDot.length();
		if (iLastLen < 1) {
			iLastLen = 1;
		}

		return subString(sValue, iLastLen, sEncoding) + sDot;
	}

	public static String subString(String sValue, int iLength, String sEncoding) {
		try {
			byte[] bytes = filterNull(sValue).getBytes(sEncoding);
			if (bytes.length <= iLength) {
				return sValue;
			}

			if (iLength < 1) {
				return "";
			}

			int length = sValue.length();
			for (int i = 0; i < length; i++) {
				String sTestStr = sValue.substring(0, i + 1);
				int iDestLength = length(sTestStr, sEncoding);

				if (iDestLength > iLength) {
					if (i == 0) {
						return "";
					} else {
						return sValue.substring(0, i);
					}
				}
			}

			return sValue;
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();

			return "";
		}
	}

	public static int length(String sValue, String sEncoding) {
		try {
			return filterNull(sValue).getBytes(sEncoding).length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

			return filterNull(sValue).getBytes().length;
		}
	}

	public static String formatArrayToString(String[] sArrays, String sSeparator) {
		String value = "";
		if (sArrays != null && !isTrimEmpty(sSeparator)) {
			for (String array : sArrays) {
				if ("".equals(value)) {
					value = array;
				} else {
					value += sSeparator + array;
				}
			}
		}

		return value;
	}

	static public String formatHtml(String sHtml) {
		sHtml = sHtml.replace("&", "&amp;");
		sHtml = sHtml.replace("\"", "&quot;");
		sHtml = sHtml.replace("<", "&lt;");
		sHtml = sHtml.replace(">", "&gt;");

		return sHtml.replace("'", "&#39;");
	}

	static public InputStream toInStream(String str) {

		InputStream inputStream = null;
		try {

			inputStream = new ByteArrayInputStream(str.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return inputStream;

	}

//	public static String compress(String sValue) {
//		String result = "";
//
//		try {
//			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(sValue.getBytes("UTF-8"));
//			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
//			GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
//
//			byte[] buf = new byte[1024];
//			int number;
//			while (-1 != (number = byteArrayInputStream.read(buf))) {
//				gzipOutputStream.write(buf, 0, number);
//			}
//
//			gzipOutputStream.close();
//			byteArrayInputStream.close();
//
//			result = Base64.encodeBase64String(byteArrayOutputStream.toByteArray());
//
//			byteArrayOutputStream.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return result;
//	}

	//
	// public static String decompress(String sValue) {
	// String result = "";
	//
	// try {
	// ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
	// 1024);
	// ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
	// Base64.decodeBase64(sValue));
	//
	// GZIPInputStream gzipInputStream = new GZIPInputStream(
	// byteArrayInputStream);
	//
	// byte[] buf = new byte[1024];
	// int number;
	// while (-1 != (number = gzipInputStream.read(buf))) {
	// byteArrayOutputStream.write(buf, 0, number);
	// }
	//
	// gzipInputStream.close();
	// byteArrayInputStream.close();
	//
	// result = byteArrayOutputStream.toString("UTF-8");
	// byteArrayOutputStream.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return result;
	// }

	/**
	 *
	 * description: 去掉obj中string类型字段值的空格，若obj为String,则将obj去空格后返回
	 *
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object trimObjectFields(Object obj) {
		if (obj == null)
			return "";
		Class cls = obj.getClass();
		if (obj instanceof String)
			return trim(obj.toString());
		List<Field> allFields = new ArrayList<Field>();
		while (cls != null && cls != Object.class) {
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields)
				allFields.add(field);
			cls = cls.getSuperclass();
		}
		try {
			for (Field field : allFields) {
				if (Modifier.isFinal(field.getModifiers()))
					continue;
				field.setAccessible(true);
				Object value = field.get(obj);
				if (value instanceof String)
					field.set(obj, trim((String) value));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 *
	 * description: 去除字符串前后的空格，若<code>str</code>为<code>null</code>,返回空串
	 *
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		return str == null ? "" : str.trim();
	}

	// begin add taobao stringutil
	public static boolean containsIgnoreCase(String originalStr, CharSequence targetStr) {
		if (null == originalStr) {
			return false;
		}

		String originalStrCaps = originalStr.toUpperCase();
		String targetStrCaps = targetStr.toString().toUpperCase();
		return originalStrCaps.contains(targetStrCaps);
	}

	public static String convertToCamelCaseString(String inputString, boolean firstCharacterUppercase) {
		if (null == inputString) {
			return null;
		}
		StringBuilder sb = new StringBuilder();

		boolean nextUpperCase = false;
		for (int i = 0; i < inputString.length(); ++i) {
			char c = inputString.charAt(i);

			switch (c) {
			case ' ':

			case '#':

			case '$':

			case '&':

			case '-':

			case '/':

			case '@':

			case '_':
				if (sb.length() > 0)
					nextUpperCase = true;
				break;
			default:
				if (nextUpperCase) {
					sb.append(Character.toUpperCase(c));
					nextUpperCase = false;
				} else {
					sb.append(c);
				}
			}

		}

		if (firstCharacterUppercase)
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		else {
			sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		}

		return sb.toString();
	}

	public static String defaultIfBlank(String originalStr, String defaultStr) {
		if (StringUtils.isBlank(originalStr)) {
			return defaultStr;
		}
		Collections.emptyList();
		return originalStr;
	}

	public static boolean equalsIgnoreCaseAll(String targetStr, String[] compareStrArray) {
		if ((StringUtils.isBlank(targetStr)) || (null == compareStrArray) || (0 == compareStrArray.length)) {
			return false;
		}
		for (int i = 0; i < compareStrArray.length; ++i) {
			if (!(targetStr.equalsIgnoreCase(compareStrArray[i]))) {
				return false;
			}
		}
		return true;
	}

	public static boolean equalsIgnoreCaseOne(String targetStr, String[] compareStrArray) {
		if ((StringUtils.isBlank(targetStr)) || (null == compareStrArray) || (0 == compareStrArray.length)) {
			return false;
		}
		for (int i = 0; i < compareStrArray.length; ++i) {
			if (targetStr.equalsIgnoreCase(compareStrArray[i])) {
				return true;
			}
		}
		return false;
	}

	public static String replaceSequenced(String originalStr, Object[] replacementParams) {
		if (StringUtils.isBlank(originalStr))
			return "";
		if ((null == replacementParams) || (0 == replacementParams.length)) {
			return originalStr;
		}
		for (int i = 0; i < replacementParams.length; ++i) {
			String elementOfParams = new StringBuilder().append(replacementParams[i]).append("").toString();
			if (StringUtils.trimToEmpty(elementOfParams).equalsIgnoreCase("null"))
				elementOfParams = "";
			originalStr = originalStr.replace(new StringBuilder().append("{").append(i).append("}").toString(), StringUtils.trimToEmpty(elementOfParams));
		}

		return originalStr;
	}

	/**
	 * 判断是否为合法的java浮点数
	 *
	 * @param s
	 * @return
	 */
	public boolean isNumeric(String s) {
		/*
		 * 使用try-catch判断是否为数字.
		 */
		boolean find = true;
		try {
			Double.parseDouble(s);
		} catch (Exception e) {
			// TODO: handle exception
			find = false;
		}
		return find;
	}

	// "^\\d+$" //非负整数（正整数 + 0）
	// "^[0-9]*[1-9][0-9]*$" //正整数
	// "^((-\\d+)|(0+))$" //非正整数（负整数 + 0）
	// "^-[0-9]*[1-9][0-9]*$" //负整数
	// "^-?\\d+$" //整数
	// "^\\d+(\\.\\d+)?$" //非负浮点数（正浮点数 + 0）
	// "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$"
	// //正浮点数
	// "^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$" //非正浮点数（负浮点数 + 0）
	// "^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))$"
	// //负浮点数
	// "^(-?\\d+)(\\.\\d+)?$" //浮点数
	// "^[A-Za-z]+$" //由26个英文字母组成的字符串
	// "^[A-Z]+$" //由26个英文字母的大写组成的字符串
	// "^[a-z]+$" //由26个英文字母的小写组成的字符串
	// "^[A-Za-z0-9]+$" //由数字和26个英文字母组成的字符串
	// "^\\w+$" //由数字、26个英文字母或者下划线组成的字符串
	// "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$" //email地址
	// "^[a-zA-z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$" //url

}