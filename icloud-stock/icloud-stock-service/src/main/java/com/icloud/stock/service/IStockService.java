package com.icloud.stock.service;

import java.util.List;

import com.icloud.service.IBaseService;
import com.icloud.stock.model.Stock;

public interface IStockService extends IBaseService<Stock> {

	public void saveAll(List<Stock> list);
}
