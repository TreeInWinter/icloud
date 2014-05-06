package com.icloud.stock.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.icloud.dao.impl.StockBaseDaoImpl;
import com.icloud.framework.util.ICloudUtils;
import com.icloud.stock.dao.ICategoryDao;
import com.icloud.stock.model.Category;

@Repository("categoryDao")
public class CategoryDaoImpl extends StockBaseDaoImpl<Category> implements
		ICategoryDao {

	@Override
	public Category getCategory(String categoryName, String type) {
		// TODO Auto-generated method stub
		List<Category> list = getHibernateTemplate()
				.find("from "
						+ domainClass.getName()
						+ " as model where model.categoryName = ? and model.categoryCategoryType= ? ",
						categoryName, type);
		if (ICloudUtils.isEmpty(list)) {
			return null;
		}
		return list.get(0);
	}
}
