package com.icloud.search.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MineUtil {

	public static String dealTitle(String title, int number) {
		if (title == null || title.length() == 0)
			return title;
		// fullChars
		try {
			title = dealFullChars(title);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			title = null;
		}
		// 繁体转简体
		title = SearchUtil.traditionalToSimple(title);
		title = SearchUtil.filterSpecial(title);
		// 合并多个空格
		title = uniqueSpecialChar(title, ' ');
		title = dealTitleSegment(title, " ", number);
		title = RedealTitle(title, number);
		return title;
	}

	public static String RedealTitle(String title, int number) {
		// TODO Auto-generated method stub
		if (title != null && title.length() > 0) {
			String titles[] = title.split(" ");
			// checkNumber(titles[i]
			if (titles != null && titles.length > 1) {
				if (checkNumber(titles[titles.length - 1]) < 9) {
					int len = checkNumber(title);
					int count = checkNumber(titles[titles.length - 1]);
					float factor = (float) len / (float) count;
					if (factor > 2.0) {
						StringBuffer sb = new StringBuffer();
						for (int i = 0; i < titles.length - 1; i++) {
							sb.append(titles[i] + " ");
						}
						return sb.toString().trim();
					}
				}
			}
		}
		return title;
	}

	private static String dealTitleSegment(String title, String c, int number) {
		// TODO Auto-generated method stub
		/**
		 * 将title分段，选择从左边起来的，符合number的几个段。 其中第一段如果少于3个字，而第二段超过5个汉字的话，对其丢弃
		 */
		if (title != null && title.length() > 0) {
			String titles[] = title.split(c);
			StringBuffer sb = new StringBuffer();
			int count = 0;
			int len = number * 2;
			if (titles != null && titles.length > 0) {
				for (int i = 0; i < titles.length; i++) {
					if (i == 0 && titles.length > 1) {
						if (checkNumber(titles[i]) < 7 && checkNumber(titles[i + 1]) > 10) {
							continue;
						}
					}
					sb.append(titles[i] + " ");
					count = count + checkNumber(titles[i]);
					if (count > len)
						break;
				}
				return sb.toString().trim();
			}
		}
		return title;
	}

	private static int checkNumber(String str) {
		// TODO Auto-generated method stub
		int count = 0;
		if (str != null) {
			char[] chars = str.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if ((chars[i] >= '0' && chars[i] <= '9') || (chars[i] >= 'a' && chars[i] <= 'z') || (chars[i] >= 'A' && chars[i] <= 'Z')) {
					count = count + 1;
				} else {
					count = count + 2;
				}
			}
		}
		return count;
	}

	public static String dealQuery(String query) throws UnsupportedEncodingException {
		if (query == null || query.length() == 0) {
			return query;
		}
		// %xx
		query = dealFormatString(query, "%");

		// \\xxx
		query = dealFormatString(query, "\\x");

		// $#ddddd;
		query = dealNumberString(query);
		// fullChars
		// query = dealFullChars(query);
		// System.out.println(query);
		// 繁体转简体
		query = SearchUtil.traditionalToSimple(query);
		// 特殊字符处理
		// query = dealSpecialChars(query);

		return query;
	}

	private static String dealSpecialChars(String query) throws UnsupportedEncodingException {
		if (query != null) {
			// 去掉头尾空格
			query = query.trim();
			// 小写
			query = query.toLowerCase();
			// 去掉头尾的'.'
			query = delHeadAndTailSpecialChar(query, '.');
			// 处理不同的字符段
			query = delLetterEdge(query, '+');
			// replace not letter to +替换 非ASCII为'+'
			query = replaceNotLetter(query, '+');
			// 去掉头尾'+'
			query = delHeadAndTailSpecialChar(query, '+');
			// unique + (将多个+变成一个)
			query = uniqueSpecialChar(query, '+');
			// 按+分段，从左向右去重，重组段，以+分开
			query = deupSegments(query, "[+]");
		}
		return query;
	}

	// 处理不同的字符段
	private static String delLetterEdge(String query, char ch) {
		// TODO Auto-generated method stub
		// check letter edge
		if (query != null && query.length() > 0) {
			StringBuffer sb = new StringBuffer();
			int len = query.length();
			int type = -1, type1 = -1; // 0 space, 1 cn, 2 en, 3
			// digit, 4 others
			for (int i = 0; i < len; i++) {
				char lastChar = query.charAt(i);
				if (charIsLower(lastChar)) {
					type = 2;
				} else if (charIsDEC(lastChar)) {
					type = 3;
					// 中文字
				} else if (lastChar < 0) {
					type = 1;
				} else if (lastChar == ' ') {
					type = 0;
				} else {
					type = 4;
				}
				// if(lastChar =='v')
				// {
				// System.out.println(type+" " + type1);
				// }
				if (type > 0 && type1 > 0 && type != type1) {
					sb.append(ch);
				}
				sb.append(lastChar);
				type1 = type;
			}
			return sb.toString();
		}
		return query;
	}

	private static boolean charIsDEC(char c) {
		// TODO Auto-generated method stub
		if (c >= '0' && c <= '9')
			return true;
		return false;
	}

	private static boolean charIsLower(char c) {
		// TODO Auto-generated method stub
		if (c >= 'a' && c <= 'z')
			return true;
		return false;
	}

	private static String deupSegments(String query, String c) {
		// TODO Auto-generated method stub
		if (query != null && query.length() > 0) {
			String querys[] = query.split(c);
			StringBuffer sb = new StringBuffer();
			if (querys != null && querys.length > 0) {
				int len = querys.length;
				// 加上querys[0];
				sb.append(querys[0]);
				for (int i = 1; i <= (len - 1); i++) {
					boolean flag = true;
					for (int j = 0; j < i; j++) {
						if (querys[i].equalsIgnoreCase(querys[j])) {
							flag = false;
							break;
						}
					}
					if (flag == true) {
						sb.append('+' + querys[i]);
					}
				}
				return sb.toString();
			}
		}
		return query;
	}

	public static String uniqueSpecialChar(String query, char c) {
		// TODO Auto-generated method stub
		if (query != null && query.length() > 0) {
			int len = query.length();
			StringBuffer sb = new StringBuffer(len);
			char[] chars = query.toCharArray();
			char lastChar = chars[0];
			sb.append(lastChar);
			for (int i = 1; i < len; i++) {
				if (chars[i] == c && lastChar == c) {

				} else {
					lastChar = chars[i];
					sb.append(chars[i]);
				}
			}
			return sb.toString();
		}
		return query;
	}

	// replace not letter to 'c'
	private static String replaceNotLetter(String query, char c) {
		// TODO Auto-generated method stub
		if (query != null) {
			int len = query.length();
			StringBuffer sb = new StringBuffer(len);
			char[] chars = query.toCharArray();
			for (int i = 0; i < len; i++) {
				char ch = chars[i];
				if ((ch < 0x80 && !ch_is_letter(ch)) || (ch == '.' && (i > 0) && (chars[i - 1] > 0x7f))) {
					ch = '+';
				}
				sb.append(ch);
			}
			return sb.toString();
		}
		return query;
	}

	private static boolean ch_is_letter(char c) {
		// TODO Auto-generated method stub
		boolean flag = ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '.' || c == '\'');
		return flag;
	}

	public static String delHeadAndTailSpecialChar(String query, char ch) {
		if (query != null) {
			// 去掉头尾+
			int start = 0;
			int end = query.length() - 1;
			for (start = 0; start < query.length(); start++) {
				if (query.charAt(start) != ch)
					break;
			}
			for (; end >= 0; end--) {
				if (query.charAt(end) != ch)
					break;
			}
			if (start > end) {
				return query;
			}
			query = query.substring(start, end + 1);
			return query;
		}
		return query;
	}

	public static String dealFullChars(String query) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		if (query != null) {
			int len = query.length();
			StringBuffer outStrBuf = new StringBuffer(len);
			byte[] b = null;
			String Tstr = "";
			for (int i = 0; i < len; i++) {
				Tstr = query.substring(i, i + 1);
				// 全角空格转换成半角空格
				if (Tstr.equals("　")) {
					outStrBuf.append(" ");
					continue;
				}

				b = Tstr.getBytes("unicode");
				// b = Tstr.getBytes("utf-16");
				// 得到 unicode 字节数据
				if (b[2] == -1) {
					// 表示全角？
					// System.out.println(b.length);
					b[3] = (byte) (b[3] + 32);
					b[2] = 0;
					String code = new String(b, "unicode");
					int code_len = code.length();
					for (int code_i = 0; code_i < code_len; code_i++) {
						char ch = code.charAt(code_i);
						if (ch >= 'A' && ch <= 'Z') {
							ch = (char) ('a' + (ch - 'A'));
						} else {
							if ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') || ch == '\'') {
							} else {
								ch = ' ';
							}
						}
						outStrBuf.append(ch);
					}
				} else {
					int code_len = Tstr.length();
					for (int code_i = 0; code_i < code_len; code_i++) {
						char aChar = Tstr.charAt(code_i);

						// Handle common case first, selecting largest block
						// that
						// avoids the specials below
						switch (aChar) {
						case '。':
							aChar = '.';
							break;
						case '<':
							aChar = '<';
							break;
						case '+':
							aChar = '+';
							break;
						case '-':
							aChar = '-';
							break;
						case '>':
							aChar = '>';
							break;
						case '《':
							aChar = '<';
							break;
						case '》':
							aChar = '>';
							break;
						case '’':
							aChar = '\'';
							break;
						case '—':
							aChar = '-';
							break;
						case '·':
							aChar = '-';
							break;
						case '、':
							aChar = '.';
							break;
						case '〕':
							aChar = ')';
							break;
						case '〔':
							aChar = '(';
							break;
						case '­':
							aChar = '-';
							break;
						case '.':
							aChar = '.';
							break;
						case '【':
							aChar = '[';
							break;
						case '】':
							aChar = ']';
							break;
						default:
							break;
						}
						outStrBuf.append(aChar);
					}
					// outStrBuf.append(Tstr);
				}
			}
			return outStrBuf.toString();
		}
		return query;
	}

	// $#dddd;
	private static String dealNumberString(String query) {
		// TODO Auto-generated method stub
		if (query != null && query.indexOf("&#") != -1) {

			StringBuffer sb = new StringBuffer(query.length());

			Pattern pattern = Pattern.compile("&#[0-9]+;");
			// 通过match（）创建Matcher实例
			Matcher matcher = pattern.matcher(query);
			int last = 0;
			while (matcher.find())// 查找符合pattern的字符串
			{

				String group = matcher.group();
				int start = matcher.start();
				int end = matcher.end();
				if (start > last) {
					sb.append(query.substring(last, start));
				}
				last = end;
				short u_short = desToInt(group.substring(2, group.length() - 1));
				byte[] tmp = new byte[2];
				tmp[1] = (byte) (u_short >> 8);
				tmp[0] = (byte) (u_short);
				String tmpStr = null;
				try {
					tmpStr = new String(tmp, "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					tmpStr = null;
				}
				if (tmpStr != null)
					sb.append(tmpStr);
			}
			return sb.toString();
		}
		return query;
	}

	// \xxx
	private static String dealFormatString(String query, String subQuery) {
		// TODO Auto-generated method stub
		if (query != null && subQuery != null) {
			char[] chars = query.toCharArray();
			int len = query.length();
			int i = 0;
			StringBuffer sb = new StringBuffer(len);

			char[] subChars = subQuery.toCharArray();
			int subLen = subChars.length;

			while (i < len) {
				// %xx
				if (isContains(chars, subChars, i)) {
					byte[] tmp = new byte[len - i];
					int j = 0;
					while (i < len) {
						// %xx
						if ((i + subLen + 1) < len && isContains(chars, subChars, i) && charIsHex(chars[i + subLen]) && charIsHex(chars[i + subLen + 1])) {
							byte digist = hexToInt(chars[i + subLen] + "" + chars[i + subLen + 1]);
							tmp[j++] = digist;
							i = i + subLen + 2;
							continue;
						} else {
							break;
						}
					}
					if (j > 0) {
						String tmpStr = null;
						try {
							tmpStr = new String(tmp, 0, j, "utf-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							tmpStr = null;
						}
						if (tmpStr != null) {
							sb.append(tmpStr);
						}
					}
					if (i < len) {
						sb.append(chars[i]);
						i++;
					}
					tmp = null;
				} else {
					sb.append(chars[i]);
					i++;
				}
			}
			query = sb.toString();

		}
		return query;
	}

	private static boolean isContains(char[] chars, char[] subChars, int startIndex) {
		// TODO Auto-generated method stub
		if (chars != null && subChars != null && (startIndex + subChars.length - 1) < chars.length) {
			for (int i = 0; i < subChars.length; i++) {
				if (chars[startIndex + i] != subChars[i])
					return false;
			}
			return true;
		}
		return false;
	}

	// %xx
	private static String dealGBKString(String query) {
		// TODO Auto-generated method stub
		if (query != null) {
			char[] chars = query.toCharArray();
			int len = chars.length;
			int i = 0;
			StringBuffer sb = new StringBuffer(len);
			while (i < len) {
				// %xx
				if (chars[i] == '%') {
					byte[] tmp = new byte[len - i];
					int j = 0;
					while (i < len) {
						// %xx
						if ((i + 2) < len && chars[i] == '%' && charIsHex(chars[i + 1]) && charIsHex(chars[i + 2])) {
							byte digist = hexToInt(chars[i + 1] + "" + chars[i + 2]);
							tmp[j++] = digist;
							i = i + 3;
							continue;
						} else {
							break;
						}
					}
					if (j > 0) {
						String tmpStr = null;
						try {
							tmpStr = new String(tmp, 0, j, "utf-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							tmpStr = null;
						}
						if (tmpStr != null) {
							sb.append(tmpStr);
						}
					}
					if (i < len) {
						sb.append(chars[i]);
						i++;
					}
					tmp = null;
				} else {
					sb.append(chars[i]);
					i++;
				}
			}
			query = sb.toString();

		}
		return query;
	}

	/**
	 * 处理query 去掉一些特殊字符
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static String dealQuery2(String query) throws UnsupportedEncodingException {
		if (query == null || query.length() == 0)
			return query;
		// %xx
		// 由于字符串中可能含有一些gbk的其他编码，需要将其变成正确格式的编码。
		int index = query.indexOf("%");
		if (index != -1) {
			char[] chars = query.toCharArray();
			int i = 0;
			int len = chars.length;
			byte[] tmp = new byte[len * 2];
			int j = 0;
			while (i < len) {
				if (chars[i] == '%' && i <= (len - 3)) {
					if (charIsHex(chars[i + 1]) && charIsHex(chars[i + 2])) {
						byte digist = hexToInt(chars[i + 1] + "" + chars[i + 2]);
						tmp[j++] = digist;
						i = i + 3;
						continue;
					}
				}
				tmp[j++] = (byte) chars[i];
				i++;
			}
			int tmp_len = j;
			byte[] comfirm = new byte[tmp_len];
			for (int k = 0; k < tmp_len; k++) {
				comfirm[k] = tmp[k];
			}
			query = new String(comfirm, "utf-8");
			comfirm = null;
			chars = null;
			tmp = null;
		}

		// \\xxxx
		return query;
	}

	public static boolean charIsHex(char c) {
		// TODO Auto-generated method stub
		if (c >= '0' && c <= '9')
			return true;
		if (c >= 'a' && c <= 'f')
			return true;
		if (c >= 'A' && c <= 'F')
			return true;
		return false;
	}

	public static int hexChartoInt(char c) {
		if (c >= '0' && c <= '9')
			return c - '0';
		if (c >= 'a' && c <= 'f')
			return c - 'a' + 10;
		if (c >= 'A' && c <= 'F')
			return c - 'A' + 10;
		return 0;
	}

	public static short desToInt(String des) {
		short count = 0;
		if (des != null) {
			int len = des.length();
			for (int i = 0; i < len; i++) {
				char c = des.charAt(i);
				if (c >= '0' && c <= '9') {
					count = (short) (count * 10 + (c - '0'));
				} else {
					break;
				}
			}
		}

		return count;
	}

	public static byte hexToInt(String hex) {
		int count = 0;
		if (hex != null && hex.length() > 0) {
			char[] hexs = hex.toCharArray();
			for (int i = 0; i < hexs.length; i++) {
				int hero = hexChartoInt(hexs[i]);
				count = count * 16 + hero;
			}
		}
		return (byte) count;
	}

	/**
	 * 处理电话号码和tid
	 * 
	 * @param _phone
	 * @param _uid
	 * @return
	 */
	public static String dealTid(String _phone, String _uid) {
		if (_phone == null && _uid == null)
			return null;
		boolean isPhone = false;
		boolean isUser = false;
		if (_phone != null) {
			if (_phone.equalsIgnoreCase("-")) {
				isPhone = false;
			} else {
				isPhone = true;
				int len = _phone.length();
				if (_phone.startsWith("86")) {
					_phone = _phone.substring(2);
					len = len - 2;
				}
				int i = 0;
				// 去掉前面的 0
				for (i = 0; i < len; i++) {
					char ch = _phone.charAt(i);
					if (ch != '0')
						break;
				}
				if (i < (len - 1) && i != 0) {
					_phone = _phone.substring(i);
					len = len - i;
				}
				if (len != 11) {
					isPhone = false;
				}
			}
		}
		if (_uid != null) {
			if (!_uid.equalsIgnoreCase("-")) {
				isUser = true;
			}
		}
		if (isPhone) {
			return 'p' + _phone;
		}
		if (isUser) {
			return 'u' + _uid;
		}
		return null;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String q3 = MineUtil
				.dealQuery("我的眼泪在刘---哈哈hahaha%e4%b8%8d%e8%a6%81%e5%9c%a8%e8%84%b8%e4%b8%8a%e7%95%99%e4%b8%8b%e7%9c%bc%e6%b3%aa%2dDj%e5%8a%a0%e5%bf%ab%e7%89%88adsfasf%a");
		// String q = MineUtil.dealQuery("有一種愛2012叫做放手");
		// String q = MineUtil.RedealTitle("非主流头像 男生 女生 qq闪", 20);
		q3 = "SSR INFT KL &#x1c;KK1&#x1d;PVGAMS 896 T12AUG YAN/XINYIINF 07FEB13/P1";
		System.out.println(q3);
		// System.out.println(q2);
		// String qsu = "iscontains";
		// String s = "con";
		// String q34 = MineUtil2
		// .dealQuery3("我的眼泪在刘---哈哈hahaha\\xe4\\xb8\\x8d\\xe8\\xa6\\x81\\xe5\\x9c\\xa8\\xe8\\x84\\xb8\\xe4\\xb8\\x8a\\xe7\\x95\\x99\\xe4\\xb8\\x8b\\xe7\\x9c\\xbc\\xe6\\xb3\\xaa\\x2dDj\\xe5\\x8a\\xa0\\xe5\\xbf\\xab\\xe7\\x89\\x88adsfasf\\xa");
		// System.out.println(isContains(qsu.toCharArray(), s.toCharArray(),
		// 2));
		// System.out.println(q34);

		// **
		// String qsu2 = "&&#&#&#&#20998;&#25163;&#30340;&#24651;&#29233;";
		// // System.out.println(qsu2);
		// System.out.println(dealSpecialChars(qsu2));
		// String qusu3 = "hello!！ 全角转换，ＤＡＯ ５３２３２ 1";
		// // System.out.println(qusu3.length());
		// System.out.println(dealQuery3(qusu3));
		// System.out.println("yes..........");
		//
		// String query =
		// "++++++++dddd+++++++asdfas++++++++afdsafadddddd++++++asdfas++++";
		// query = delHeadAndTailSpecialChar(query, '+');
		// System.out.println("111 : " + query);
		// query = uniqueSpecialChar(query, '+');
		// System.out.println("2222 : " + query);
		// // 按+分段，从左向右去重，重组段，以+分开
		// query = deupSegments(query, "[+]");
		// System.out.println("333 : " + query);
		// short[] sx = { 19981, 20998, 25163, 30340, 24651, 29233 };
		// for (short s : sx) {
		// // short s = 29233;
		// byte[] tmp = new byte[2];
		// tmp[1] = (byte) (s >> 8);
		// tmp[0] = (byte) (s);
		// System.out.print("tmp[0]" + tmp[0] + "===" + tmp[1] + " ======");
		// // System.out.println("+_------");
		// String tmpStr = new String(tmp, "utf-8");
		// String sss = new String(tmpStr.getBytes(), "UTF-8");
		// System.out.println(s + ": " + tmpStr + " ==" + sss);
		//
		// }
		// System.out.println(desToInt("1341"));
		// System.out.println(q2);
		// String tid = MineUtil.dealTid("86012345678912", "xxxxx");
		// String tid = MineUtil2.dealTid2("8600012345678910", "asdfasdf");
		// System.out.println(tid);
		// // Character.isDefined(ch);
		// System.out.println(Integer.toHexString(170));
		// System.out.println(hexToInt("aa"));
		// char c = 's';
		// int s = c-0;
		// System.out.println(s);
		// c=(char) s;
		// System.out.println(c);
		// // Integer.valueOf(s)
		// char ch;
		// byte[] chars = new byte[3];
		// chars[0] = (byte) -28;
		// chars[1] = (byte) -72;
		// chars[2] = (byte) -115;
		// String str = new String(chars,"utf-8");
		// System.out.println(str);
		// System.out.println(str.length());
	}

}