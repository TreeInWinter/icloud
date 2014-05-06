package com.icloud.stock.dao.impl;

import org.springframework.stereotype.Repository;

import com.icloud.dao.impl.StockBaseDaoImpl;
import com.icloud.stock.dao.ICategoryStockDao;
import com.icloud.stock.model.CategoryStock;

@Repository("categoryStockDao")
public class CategoryStockDaoImpl extends StockBaseDaoImpl<CategoryStock>
		implements ICategoryStockDao {

}
