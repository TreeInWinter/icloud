package com.icloud.stock.connector.parser.impl;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.google.gson.Gson;
import com.icloud.stock.connector.model.XueqiuMetaInfo;
import com.icloud.stock.connector.parser.Parser;
import com.icloud.stock.http.HtmlUnit;

public class XueqiuMetaInfoPaser implements Parser<XueqiuMetaInfo> {

	@Override
	public XueqiuMetaInfo parse(String content, Object... params) {
		System.out.println(content);
		Gson gson = new Gson();
		XueqiuMetaInfo metaInfo = gson.fromJson(content, XueqiuMetaInfo.class);
		return metaInfo;
	}

	public static void main(String[] args)
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
		XueqiuMetaInfoPaser paser = new XueqiuMetaInfoPaser();
		// String url =
		// "http://xueqiu.com/stock/cata/stocklist.json?page=1&size=60&order=desc&orderby=percent&exchange=CN&plate=A%E8%82%A1%E6%8C%87%E6%95%B0";
		String url = "http://xueqiu.com/industry/quote_order.json?page=1&size=60&order=desc&exchange=CN&plate=%E4%BB%93%E5%82%A8%E4%B8%9A&orderBy=percent&level2code=G59&access_token=CSr4RehgUt3wohdtqUTp9E&_=1399368774171";
		// http://xueqiu.com/stock/cata/quote_order.json?page=1&size=60&order=desc&orderby=percent&level2code=G59&exchange=CN&plate=%E4%BF%9D%E9%99%A9%E4%B8%9A&access_token=CSr4RehgUt3wohdtqUTp9E&_=1399368774171
		HtmlUnit htmlUnit = new HtmlUnit();
		XueqiuMetaInfo metaInfo = paser.parse(htmlUnit.getContentAsString(url),
				null);
		System.out.println(metaInfo.getCount());
		System.out.println(metaInfo.getData());
		int ceil = (int) Math.ceil(metaInfo.getCount() / 60.0);
		System.out.println(ceil);
	}
}
