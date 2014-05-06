package com.icloud.stock.connector.parser.impl;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.icloud.stock.http.HtmlUnit;

public class baseCategoryStock {
	public static void fetching() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		HtmlUnit htmlUnit = new HtmlUnit();
		String url = "http://xueqiu.com/industry/quote_order.json?page=1&size=60&order=desc&exchange=CN&plate=%E4%BB%93%E5%82%A8%E4%B8%9A&orderBy=percent&level2code=G59&access_token=CSr4RehgUt3wohdtqUTp9E&_=1399368774171";
		String content = htmlUnit.getContentAsString(url);
		System.out.println(content);
	}

	public static void main(String[] args) {
		try {
			fetching();
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
