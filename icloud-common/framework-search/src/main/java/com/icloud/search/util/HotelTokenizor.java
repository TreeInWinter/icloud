package com.icloud.search.util;

import java.io.IOException;
import java.util.List;

import org.wltea.analyzer.lucene.IKAnalyzer;

public class HotelTokenizor {
	private IKAnalyzer analyzer;

	public HotelTokenizor() {
		analyzer = new IKAnalyzer(true);
	}

	public float similarity(String hotelNameA, String hotelNameB) throws IOException {
		if (hotelNameA != null && hotelNameB != null) {
			hotelNameA = SearchUtil.traditionalToSimple(hotelNameA);
			hotelNameB = SearchUtil.traditionalToSimple(hotelNameB);
			if (hotelNameA.equalsIgnoreCase(hotelNameB)) {
				return 1.0f;
			}

			if (hotelNameA.indexOf(hotelNameB) != -1 || hotelNameB.indexOf(hotelNameA) != -1) {
				int len = StringUtil.LD(hotelNameA, hotelNameB);
				int size1 = hotelNameA.length();
				int size2 = hotelNameB.length();
				int size = size1 > size2 ? size1 : size2;
				return 1 - len / (float) size;
			}

			/**
			 * 分詞后计算
			 */
			List<String> set = PDTokenizor.getTokens(analyzer, hotelNameA);
			List<String> set1 = PDTokenizor.getTokens(analyzer, hotelNameB);
			if (set != null && set1 != null) {
				StringBuffer sb1 = new StringBuffer();
				StringBuffer sb2 = new StringBuffer();
				for (String s : set) {
					sb1.append(s);
				}
				for (String s : set1) {
					sb2.append(s);
				}
				int len = StringUtil.LCS_len(sb1.toString(), sb2.toString());
				int size1 = sb1.toString().length();
				int size2 = sb2.toString().length();
				int size = (size1 > size2) ? size2 : size1;
				if ((len / (float) size) > 0.6) {
					return len / (float) size;
				}
				return 0.0f;
			}

		}
		return 0.0f;
	}

	public boolean isSameHotel(String hotelNameA, String hotelNameB) throws IOException {
		if (hotelNameA != null && hotelNameB != null) {
			hotelNameA = SearchUtil.traditionalToSimple(hotelNameA);
			hotelNameB = SearchUtil.traditionalToSimple(hotelNameB);
			if (hotelNameA.equalsIgnoreCase(hotelNameB)) {
				return true;
			}

			if (hotelNameA.indexOf(hotelNameB) != -1 || hotelNameB.indexOf(hotelNameA) != -1) {
				return true;
			}

			List<String> set = PDTokenizor.getTokens(analyzer, hotelNameA);
			List<String> set1 = PDTokenizor.getTokens(analyzer, hotelNameB);
			if (set != null && set1 != null) {
				boolean containsFlag = true;
				for (String str : set) {
					if (!set1.contains(str)) {
						containsFlag = false;
						break;
					}
				}
				if (!containsFlag) {
					for (String str : set1) {
						if (!set.contains(str)) {
							containsFlag = false;
							break;
						}
					}
				}
				if (!containsFlag) {
					StringBuffer sb1 = new StringBuffer();
					StringBuffer sb2 = new StringBuffer();
					for (String s : set) {
						sb1.append(s);
					}
					for (String s : set1) {
						sb2.append(s);
					}
					// System.out.println(sb1.toString());
					// System.out.println(sb2.toString());
					// int len = StringUtil.LD(sb1.toString(), sb2.toString());
					int len = StringUtil.LCS_len(sb1.toString(), sb2.toString());
					// System.out.println(len);
					int size1 = sb1.toString().length();
					int size2 = sb2.toString().length();
					int size = (size1 > size2) ? size2 : size1;
					if ((len / (float) size) > 0.6) {
						containsFlag = true;
					}
				}

				return containsFlag;
			}

		}
		return false;
	}

	public static void main(String args[]) throws IOException {
		String hotelName = "上海易里酒店式公寓 （原长宁贵都酒店式公寓）";
		String hotelName1 = "上海易里酒店式公寓";
//		String hotelName = "美卡商务酒店(新华路)";
//		String hotelName1 = "陕西商务酒店";
		HotelTokenizor hotelTokenizor = new HotelTokenizor();
		boolean flag = hotelTokenizor.isSameHotel(hotelName, hotelName1);
		System.out.println(flag);
		float f = hotelTokenizor.similarity(hotelName, hotelName1);
		System.out.println(f);
	}

}
