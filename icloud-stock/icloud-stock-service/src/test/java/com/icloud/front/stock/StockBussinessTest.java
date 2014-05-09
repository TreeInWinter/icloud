package com.icloud.front.stock;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.icloud.front.stock.bussiness.menu.StockCommonBussiness;
import com.icloud.front.stock.bussiness.view.StockListBussiness;

@ContextConfiguration(locations = { "classpath*:spring/applicationContext-stock.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class StockBussinessTest {
	@Resource(name = "stockCommonBussiness")
	protected StockCommonBussiness stockCommonBussiness;
	@Resource(name = "stockListBussiness")
	protected StockListBussiness stockListBussiness;
}
