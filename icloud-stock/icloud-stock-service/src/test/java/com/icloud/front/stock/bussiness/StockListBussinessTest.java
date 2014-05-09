package com.icloud.front.stock.bussiness;

import java.util.List;

import org.junit.Test;

import com.icloud.framework.core.wrapper.Pagination;
import com.icloud.front.stock.StockBussinessTest;
import com.icloud.stock.model.Stock;

public class StockListBussinessTest extends StockBussinessTest {
	@Test
	public void getStockList() {
		Pagination<Stock> paingation = this.stockListBussiness.getStockList(0,
				40);
		System.out.println(paingation.getPageNo());
		System.out.println(paingation.getPageSize());
		System.out.println(paingation.getTotalPageCount());
		System.out.println(paingation.getTotalItemCount());
		List<Stock> stockList = (List<Stock>) paingation.getData();
		System.out.println(stockList.size());
		for (Stock stock : stockList) {
			// Hibernate.initialize(stock.getStockName());
			System.out.println(stock.getStockAllCode() + " "
					+ stock.getStockName());
		}
	}
}
