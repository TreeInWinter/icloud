package com.icloud.stock.connector.parser.impl;

import com.icloud.stock.connector.model.StockCurrentInfo;
import com.icloud.stock.connector.parser.Parser;

public class StockCurrentInfoPaser implements Parser<StockCurrentInfo> {

	@Override
	public StockCurrentInfo parse(String content, Object... params) {
		// TODO Auto-generated method stub
		System.out.println(content);
		return null;
	}

}
