package com.icloud.stock.connector.parser;

import java.util.ArrayList;

import org.junit.Test;

import com.icloud.stock.connector.handler.impl.DownLoadCVSHandler;
import com.icloud.stock.connector.handler.impl.StockCurrentInfoHandler;
import com.icloud.stock.connector.handler.impl.StockMetaInfoHandler;
import com.icloud.stock.connector.model.StockCurrentInfo;
import com.icloud.stock.connector.model.StockMetaInfo;
import com.icloud.stock.model.StockDateHistory;
import com.icloud.stock.model.constant.StockConstants.StockLocation;

public class StockTest {
	@Test
	public void parserStockCurrentInfo() throws NoSuchFieldException,
			SecurityException, NoSuchMethodException {
		StockCurrentInfoHandler handler = new StockCurrentInfoHandler("601006",
				StockLocation.SHA);
		StockCurrentInfo httpData = handler.getHttpData();
		System.out.println(httpData);
	}

	@Test
	public void parserStockMetaInfo() throws NoSuchFieldException,
			SecurityException, NoSuchMethodException {
		String url = "";
		StockMetaInfoHandler handler = new StockMetaInfoHandler(url);
		StockMetaInfo httpData = handler.getHttpData();
		System.out.println(httpData);
	}

	@Test
	public void parserStockDateHistoryInfo() {
		// String url = "http://table.finance.yahoo.com/table.csv?s=000001.sz";
		// url = "http://table.finance.yahoo.com/table.csv?s=600000.ss";
		String code = "000001";
		int stock_id = 1;
		DownLoadCVSHandler handler = null;
		handler = new DownLoadCVSHandler(code, StockLocation.SZX, stock_id);

		if (handler != null) {
			ArrayList<StockDateHistory> httpData = handler.getHttpData();
			for (StockDateHistory stockDateHistory : httpData) {
				System.out.println(stockDateHistory);
			}
		}
	}
}
