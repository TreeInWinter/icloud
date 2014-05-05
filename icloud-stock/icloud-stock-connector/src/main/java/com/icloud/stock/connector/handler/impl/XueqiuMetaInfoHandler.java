package com.icloud.stock.connector.handler.impl;

import java.util.Map;

import com.icloud.stock.connector.handler.StockHandler;
import com.icloud.stock.connector.model.XueqiuMetaInfo;

public class XueqiuMetaInfoHandler extends StockHandler<XueqiuMetaInfo> {

	public XueqiuMetaInfoHandler(String url, Map<String, String> params)
			throws NoSuchFieldException, SecurityException,
			NoSuchMethodException {
		super(url, params, true);
	}

	@Override
	public void apply() {
		// TODO Auto-generated method stub

	}

}
