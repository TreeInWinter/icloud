package com.icloud.stock.service.test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.icloud.stock.business.PersonService;
import com.icloud.stock.model.Stock;
import com.icloud.stock.service.IStockService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/applicationContext-stock.xml" })
public class StockServiceTest {
	@Resource(name = "stockService")
	private IStockService stockService;

	@Resource(name = "personService")
	private PersonService personService;

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

	@Test
	public void testJoinCut() {
		personService.getPersonName(11);
	}

	@Test
	public void testTd() {
		List<Stock> stocks = new ArrayList<Stock>();
		for (int i = 0; i < 5; i++) {
			Stock stock = new Stock();
			stock.setStockCode("xxxx" + i);
			stock.setStockName("NNNNN" + i);
			stocks.add(stock);
		}
		try {
			stockService.saveAll(stocks);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
