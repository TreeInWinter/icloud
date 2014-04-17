package com.icloud.stock.connector.handler.impl;

import java.util.Map;

import com.icloud.stock.connector.handler.StockHandler;
import com.icloud.stock.connector.model.StockMetaInfo;

public class StockMetaInfoHandler extends StockHandler<StockMetaInfo> {

	public StockMetaInfoHandler(String url, Map<String, String> params)
			throws NoSuchFieldException, SecurityException,
			NoSuchMethodException {
		super(url, params);
		// TODO Auto-generated constructor stub
	}

	public StockMetaInfoHandler(String url) throws NoSuchFieldException,
			SecurityException, NoSuchMethodException {
		super(url);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void apply() {
		// TODO Auto-generated method stub

	}

}
