package com.icloud.stock.service.impl;

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

}
