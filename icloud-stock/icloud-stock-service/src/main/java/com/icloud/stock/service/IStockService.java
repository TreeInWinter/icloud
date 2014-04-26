package com.icloud.stock.service;

import java.util.List;

import com.icloud.framework.service.ISqlBaseService;
import com.icloud.stock.model.Stock;

public interface IStockService extends ISqlBaseService<Stock> {

	public void saveAll(List<Stock> list);
}
