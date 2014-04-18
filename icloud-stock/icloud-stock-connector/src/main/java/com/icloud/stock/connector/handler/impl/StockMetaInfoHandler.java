package com.icloud.stock.connector.handler.impl;

import java.util.Map;

import com.icloud.stock.connector.handler.StockHandler;
import com.icloud.stock.connector.model.StockMetaInfo;

public class StockMetaInfoHandler extends StockHandler<StockMetaInfo> {

	public StockMetaInfoHandler(String url, Map<String, String> params)
			throws NoSuchFieldException, SecurityException,
			NoSuchMethodException {
		super(url, params);
	}

	public StockMetaInfoHandler(String url) throws NoSuchFieldException,
			SecurityException, NoSuchMethodException {
		super(url);
	}

	@Override
	public void apply() {
		// TODO Auto-generated method stub

	}


}
