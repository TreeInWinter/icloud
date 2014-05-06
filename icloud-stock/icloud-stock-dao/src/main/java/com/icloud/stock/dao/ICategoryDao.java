package com.icloud.stock.dao;

import com.icloud.dao.StockBaseDao;
import com.icloud.stock.model.Category;

public interface ICategoryDao extends StockBaseDao<Category> {

	Category getCategory(String categoryName, String type);

}
