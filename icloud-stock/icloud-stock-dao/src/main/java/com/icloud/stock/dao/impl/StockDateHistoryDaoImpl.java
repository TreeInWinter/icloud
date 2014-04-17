package com.icloud.stock.dao.impl;

import org.springframework.stereotype.Repository;

import com.icloud.dao.impl.StockBaseDaoImpl;
import com.icloud.stock.dao.IStockDateHistoryDao;
import com.icloud.stock.model.StockDateHistory;

@Repository("stockDateHistoryDao")
public class StockDateHistoryDaoImpl extends StockBaseDaoImpl<StockDateHistory>
		implements IStockDateHistoryDao {

}
