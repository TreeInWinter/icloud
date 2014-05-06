package com.icloud.framework.net.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HttpUtils {
	public static String encodingGB2312(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		try {
			String keywords = URLEncoder.encode(str, "utf-8");
			keywords = keywords.replace("+", "%20");
			return keywords;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
