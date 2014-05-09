package com.icloud.front.stock.baseaction;

import javax.annotation.Resource;

import com.icloud.front.stock.bussiness.detail.StockDetailBussiness;
import com.icloud.front.stock.bussiness.menu.StockCommonBussiness;
import com.icloud.front.stock.bussiness.view.StockListBussiness;

public class BaseStockController {
	@Resource(name = "stockCommonBussiness")
	protected StockCommonBussiness stockCommonBussiness;
	@Resource(name = "stockDetailBussiness")
	protected StockDetailBussiness stockDetailBussiness;
	@Resource(name = "stockListBussiness")
	protected StockListBussiness stockListBussiness;
}
