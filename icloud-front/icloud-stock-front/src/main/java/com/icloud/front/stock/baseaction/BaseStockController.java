package com.icloud.front.stock.baseaction;

import javax.annotation.Resource;

import com.icloud.front.stock.bussiness.menu.StockCommonBussiness;

public class BaseStockController {
	@Resource(name = "stockCommonBussiness")
	protected StockCommonBussiness stockCommonBussiness;
}
