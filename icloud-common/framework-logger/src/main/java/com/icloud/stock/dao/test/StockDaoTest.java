package com.icloud.stock.dao.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/applicationContext-*" })
public class StockDaoTest {
	// @Resource(name = "")

	@Test
	public void getStockTest() {
		System.out.println("dasf");
	}
}
