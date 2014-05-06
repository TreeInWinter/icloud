package com.icloud.stock.dao.impl;

import org.springframework.stereotype.Repository;

import com.icloud.dao.impl.StockBaseDaoImpl;
import com.icloud.stock.dao.ICategoryDao;
import com.icloud.stock.model.Category;

@Repository("categoryDao")
public class CategoryDaoImpl extends StockBaseDaoImpl<Category> implements
		ICategoryDao {

}
