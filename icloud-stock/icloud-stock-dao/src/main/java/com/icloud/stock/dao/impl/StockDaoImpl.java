package com.icloud.stock.dao.impl;

import org.springframework.stereotype.Repository;

import com.icloud.dao.impl.StockBaseDaoImpl;
import com.icloud.stock.dao.IStockDao;
import com.icloud.stock.model.Stock;

@Repository("stockDao")
public class StockDaoImpl extends StockBaseDaoImpl<Stock> implements IStockDao {

}
