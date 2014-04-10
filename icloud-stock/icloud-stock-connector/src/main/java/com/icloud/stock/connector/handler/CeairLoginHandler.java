package com.icloud.stock.connector.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import com.icloud.stock.connector.model.StockCurrentInfo;
import com.icloud.stock.connector.parser.Parser;
import com.icloud.stock.connector.parser.ParserFactory;
import com.icloud.stock.connector.parser.StockParseFactory;
import com.travelzen.framework.net.http.TZHttpClient;

public class CeairLoginHandler {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(CeairLoginHandler.class);
	private static ParserFactory factory = new StockParseFactory();

	public static void login() {
		StopWatch stopwatch = new StopWatch();
		String lvResponse = null;

		Map<String, String> params = new HashMap<String, String>();
		TZHttpClient client = new TZHttpClient(
				"http://hq.sinajs.cn/list=sh601006", params);
		client.sendGetRequest();

		String str = client.sendPostRequest();
		System.out.println(str);
		Parser<StockCurrentInfo> parser = factory
				.getParser(StockCurrentInfo.class);
		StockCurrentInfo info = parser.parse(str, null);
		// try{
		//
		// }catch(Exception e){
		// e.
		// }
		// } catch (Exception e) {
		// System.out.println(response);
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// LOGGER.error("CEAIR_LOGIN_URL", e);
		// return null;
		// }
		// LOGGER.info("login method:请求参数-->" + pParams + "返回结果-->" + lvResponse
		// + "\n请求开始时间：" + sdf.format(new Date(begin)) + "\n请求结束时间："
		// + sdf.format(new Date(end)) + "\n耗时：" + last / 1000 + "秒"
		// + last % 1000 + "毫秒");
		// return convertLoginEntity;
	}

	public static void main(String[] args) {
		CeairLoginHandler.login();
		// StopWatch watch = new StopWatch("搜索酒店");
		//
		// // step1: 查找索引
		// watch.start("搜索索引");
		// try {
		// Thread.sleep(300);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// watch.stop();
		// LOGGER.info("搜索酒店{}{}", watch.getTotalTimeMillis(), watch);
	}
}
