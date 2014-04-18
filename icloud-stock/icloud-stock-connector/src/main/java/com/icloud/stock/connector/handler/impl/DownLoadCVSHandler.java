package com.icloud.stock.connector.handler.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StopWatch;

import com.icloud.stock.connector.handler.BaseHandler;
import com.icloud.stock.connector.parser.Parser;
import com.icloud.stock.model.StockDateHistory;
import com.travelzen.framework.net.http.TZHttpClient;

public class DownLoadCVSHandler implements BaseHandler<StockDateHistory> {
	private String url;
	private Map<String, String> params;

	public DownLoadCVSHandler(String url, Map<String, String> params)
			throws NoSuchFieldException, SecurityException,
			NoSuchMethodException {
		this.url = url;
		this.params = params;
	}

	@Override
	public StockDateHistory getHttpData() {
		// TODO Auto-generated method stub

		StopWatch stopwatch = new StopWatch("获得cvs信息");
		stopwatch.start("调用http接口");
		Map<String, String> params = new HashMap<String, String>();
		TZHttpClient client = new TZHttpClient(url, params);
		InputStream inputStreamCVS = client.downCVSFile();


		stopwatch.stop();
		stopwatch.start("解析数据");
		// Parser<T> parser = (Parser<T>) factory.getParser(domainClass);
		// T result = parser.parse(str, null);
		// stopwatch.stop();
		// LOGGER.info("{}:{}", stopwatch.getLastTaskName(),
		// stopwatch.toString());
		// return result;
		return null;
	}

}
