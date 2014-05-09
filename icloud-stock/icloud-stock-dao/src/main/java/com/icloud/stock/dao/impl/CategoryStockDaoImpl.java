package com.icloud.stock.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.icloud.dao.impl.StockBaseDaoImpl;
import com.icloud.stock.dao.ICategoryStockDao;
import com.icloud.stock.model.CategoryStock;

@Repository("categoryStockDao")
public class CategoryStockDaoImpl extends StockBaseDaoImpl<CategoryStock>
		implements ICategoryStockDao {
}
