package com.icloud.stock.importer.meta;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.icloud.framework.file.TextFile;
import com.icloud.stock.connector.handler.impl.XueqiuMetaInfoHandler;
import com.icloud.stock.connector.model.XueqiuMetaInfo;

public class XueQiuHangYeImporter {

	public void importHangye() {
		String filePath = "bin/data/xueqiu行业.txt";
		List<String> list = getAllHangye(filePath);
		for (String hangye : list) {
			getAllContent(hangye);
		}
	}

	public void getAllContent(String hangye) {
		String url = getHangyeUrl(hangye, 1, 60);
		System.out.println(url);
		XueqiuMetaInfo fetchData = fetchData(url);
		System.out.println(fetchData);
	}

	private XueqiuMetaInfo fetchData(String url) {
		// TODO Auto-generated method stub
		try {
			XueqiuMetaInfoHandler handler = new XueqiuMetaInfoHandler(url, null);
			XueqiuMetaInfo metaInfo = handler.getHttpData();
			return metaInfo;
		} catch (NoSuchFieldException | SecurityException
				| NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

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

	public String getHangyeUrl(String hangye, int page, int size) {
		String url = "http://xueqiu.com/stock/cata/stocklist.json?page=" + page
				+ "&size=" + size
				+ "&order=desc&orderby=percent&exchange=CN&plate="
				+ encodingGB2312(hangye);
		return url;
	}

	public List<String> getAllHangye(String filePath) {
		List<String> list = new ArrayList<String>();
		TextFile textFile = new TextFile(filePath);
		for (String text : textFile) {
			list.add(text.trim());
		}
		return list;
	}

	//
	public static void main(String[] args) throws UnsupportedEncodingException {
		XueQiuHangYeImporter importer = new XueQiuHangYeImporter();
		// importer.importHangye();
		importer.getAllContent("A股指数");
		// String a = "A股指数";
		// String string = new String(a.getBytes(), "gb2312");
		// System.out.println(string);
	}
}
