package com.icloud.stock.connector.parser;

import java.util.HashMap;
import java.util.Map;

import com.icloud.stock.connector.model.StockCurrentInfo;
import com.icloud.stock.connector.parser.impl.StockCurrentInfoPaser;

public class StockParseFactory implements ParserFactory {
	private Map<String, Parser> map = new HashMap<String, Parser>();

	public StockParseFactory() {
		// 扫描东西进行处理
		map.put(StockCurrentInfo.class.getName(), new StockCurrentInfoPaser());
	}

	@Override
	public <T> Parser<T> getParser(Class<T> z) {
		// TODO Auto-generated method stub
		if (z != null) {
			return map.get(z.getName());
		}
		return null;
	}

}
