package com.icloud.stock.importer.meta;

import com.icloud.stock.ctx.BeansUtil;
import com.icloud.stock.service.ICategoryService;
import com.icloud.stock.service.ICategoryStockService;
import com.icloud.stock.service.IStockService;

public class BaseServiceImporter {

	protected IStockService stockService;
	protected ICategoryService categoryService;
	protected ICategoryStockService categoryStockService;

	public BaseServiceImporter() {
		stockService = BeansUtil.getStockService();
		categoryService = BeansUtil.getCategoryService();
		categoryStockService = BeansUtil.getCategoryStockService();
	}
}
