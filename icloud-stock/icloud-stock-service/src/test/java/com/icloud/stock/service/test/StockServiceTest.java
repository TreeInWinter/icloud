package com.icloud.stock.service.test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.icloud.stock.model.Stock;
import com.icloud.stock.service.IStockService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/applicationContext-*" })
public class StockServiceTest {
	@Resource(name = "stockService")
	private IStockService stockService;

	@Test
	public void updateStockTest() {
		List<Stock> list = stockService.findAll();
		if (list != null) {
			Stock stock = list.get(0);
			System.out.println(stock.getStockCode() + stock.getStockName());
			stock.setStockCode("44444");
			stockService.update(stock);

			Stock stock2 = stockService.getById(stock.getId());
			// System.out.println(stock2.getId());
			System.out.println(stock2.getStockCode() + stock2.getStockName());
		}
	}

}
