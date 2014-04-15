package com.icloud.stock.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.icloud.framework.dao.hibernate.IHibernateBaseDao;
import com.icloud.service.impl.BaseService;
import com.icloud.stock.dao.IStockDao;
import com.icloud.stock.model.Stock;
import com.icloud.stock.service.IStockService;

@Service("stockService")
public class StockServiceImpl extends BaseService<Stock> implements
		IStockService {

	@Resource(name = "stockDao")
	private IStockDao stockDao;

	@Override
	protected IHibernateBaseDao<Stock> getDao() {
		// TODO Auto-generated method stub
		return stockDao;
	}

	@Override
	public void saveAll(List<Stock> list) {
		// TODO Auto-generated method stub
		if (list != null) {
			int i = 0;
			for (Stock stock : list) {
				// this.stockDao.save(stock);
				this.save(stock);
				System.out.println("ok...." + stock.getId());
				i++;
				if (i > 1) {
					int j = 0;
					j = 3 / j;
				}
			}
		}
	}
}
