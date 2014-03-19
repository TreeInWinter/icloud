package com.travelzen.search.util;

import java.util.HashSet;
import java.util.Set;

public class PinyinAutoCompletetor {
	public static String DEAFAULT_SEPARATOR = "###";

	/**
	 * 获取汉字的可能的输入情况 譬如：query 崔江宁;separator ### 可能输入的情况如下： 　　　 cjn cuijiangning
	 * cuijiannin 崔江宁 输出： 崔江宁###cjn###cuijiangning###cuijiannin
	 *
	 * @param text
	 *            表示输入的字段
	 * @param SEPARATOR
	 *            　分割符
	 * @return
	 */
	public static Set<String> getAllPossiblePinyin(String query) {
		HashSet<String> set = new HashSet<String>();
		if (query != null) {
			String separator = null;
			if (separator == null) {
				separator = DEAFAULT_SEPARATOR;
			}
			boolean isEnglish = SearchUtil.isEnglish(query);

			if (isEnglish) {
				set.add(query);
				return set;
			} else {
				String[] pinyins = Constants.getPinyinAndSim(query);

				set.add(query);

				if (pinyins != null) {
					for (String pinyin : pinyins) {
						/**
						 * 获取拼音首字母
						 */
						String simplePinyin = Constants.getSimplePinyin(pinyin);
						if (simplePinyin != null) {
							set.add(simplePinyin);
						}
						set.add(pinyin.replace(" ", ""));
					}
				}
				return set;
				// StringBuffer sb = new StringBuffer();
				// for(String s:set){
				// sb.append(s).append(DEAFAULT_SEPARATOR);
				// }
				// if(sb.lastIndexOf(DEAFAULT_SEPARATOR)!=-1){
				// return
				// sb.toString().substring(0,sb.lastIndexOf(DEAFAULT_SEPARATOR));
				// }
				// return query;
			}

		}
		return set;
	}

	/**
	 * 获取汉字的搜索词 譬如：query 崔江宁 输出： cuijiangning
	 *
	 * @return
	 */
	public static String getPinYinSearchWord(String query) {
		if (query != null) {
			String separator = null;
			if (separator == null) {
				separator = DEAFAULT_SEPARATOR;
			}
			boolean isEnglish = SearchUtil.isEnglish(query);
			// 如果是英文，直接输出英文
			if (isEnglish) {
				return query;
			} else {
				// 如果是中文，转化为中文
				String[] pinyins = Constants.getPinyinAndSim(query);
				if (pinyins != null && pinyins.length == 2) {
					pinyins[1] = pinyins[1].replace(" ", "");
					return pinyins[1];
				}
			}
		}
		return query;
	}

	public static char getFirstPinyinChar(String query) {
		query = getPinYinSearchWord(query);
		if (query != null && query.length() > 0) {
			query = query.toUpperCase();
			return query.charAt(0);
		}
		return 'A';
	}

	/**
	 * 全拼
	 */
	public static String getAllPinYin(String query) {
		if (query != null) {
			String[] pinyins = Constants.getPinyinAndSim(query);
			if (pinyins != null && pinyins.length == 2)
				return pinyins[0].replace(" ", "");
			// return Constants.getSimPinyinWord(query);
		}
		return query;
	}

	/**
	 * 简拼
	 */
	public static String getSimplePinyin(String query) {
		if (query != null) {
			String[] pinyins = Constants.getPinyinAndSim(query);
			if (pinyins != null && pinyins.length == 2) {
				String simplePinyin = Constants.getSimplePinyin(pinyins[1]);
				return simplePinyin;
			}
		}
		return query;
	}

	public static void main(String args[]) {
		String a = getAllPinYin("什么什么股份有限公司");
//		a = getSimplePinyin("什么什么股份有限公司");
		System.out.println("-----------");
		System.out.println(a);
	}
}
