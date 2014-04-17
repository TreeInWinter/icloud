package com.icloud.stock.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.icloud.framework.dao.hibernate.IHibernateBaseDao;
import com.icloud.service.impl.BaseService;
import com.icloud.stock.dao.IStockDateHistoryDao;
import com.icloud.stock.model.StockDateHistory;
import com.icloud.stock.service.IStockDateHistoryService;

@Service("stockDateHistoryService")
public class StockDateHistoryServiceImpl extends BaseService<StockDateHistory> implements
		IStockDateHistoryService {

	@Resource(name = "stockHistoryDao")
	private IStockDateHistoryDao stockHistoryDao;

	@Override
	protected IHibernateBaseDao<StockDateHistory> getDao() {
		// TODO Auto-generated method stub
		return stockHistoryDao;
	}

}
