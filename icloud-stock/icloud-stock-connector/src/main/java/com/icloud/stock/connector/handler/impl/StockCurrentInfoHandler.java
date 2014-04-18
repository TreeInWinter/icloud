package com.icloud.stock.connector.handler.impl;

import java.util.Map;

import com.icloud.stock.connector.handler.StockHandler;
import com.icloud.stock.connector.model.StockCurrentInfo;
import com.icloud.stock.model.constant.StockConstants.StockLocation;

public class StockCurrentInfoHandler extends StockHandler<StockCurrentInfo> {
	private String code;

	private StockCurrentInfoHandler(String url, Map<String, String> params)
			throws NoSuchFieldException, SecurityException,
			NoSuchMethodException {
		super(url, params);
		// TODO Auto-generated constructor stub
	}

	public StockCurrentInfoHandler(String code, StockLocation location) {
		String type = "sz";
		if (location == StockLocation.SZX) {
			type = "sz";
		} else if (location == StockLocation.SHA) {
			type = "sh";
		}
		this.code = code;
		this.url = "http://hq.sinajs.cn/list=" + type + code;
	}

	@Override
	public void apply() {
		// TODO Auto-generated method stub

	}

	@Override
	public StockCurrentInfo getHttpData() {
		StockCurrentInfo httpData = super.getHttpData();
		if (httpData != null) {
			httpData.setStockCode(code);
		}
		return httpData;
	}

}
