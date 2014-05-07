package com.icloud.front.stock.bussiness;

import javax.annotation.Resource;

import com.icloud.stock.service.ICategoryService;
import com.icloud.stock.service.ICategoryStockService;
import com.icloud.stock.service.IStockService;

public class BaseAction {
	@Resource(name = "stockService")
	private IStockService stockService;
	@Resource(name = "categoryService")
	private ICategoryService categoryService;
	@Resource(name = "categoryStockService")
	private ICategoryStockService categoryStockService;

}
