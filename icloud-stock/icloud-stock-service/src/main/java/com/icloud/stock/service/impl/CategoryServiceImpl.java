package com.icloud.stock.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.icloud.framework.dao.hibernate.IHibernateBaseDao;
import com.icloud.framework.service.impl.SqlBaseService;
import com.icloud.stock.dao.ICategoryDao;
import com.icloud.stock.model.Category;
import com.icloud.stock.service.ICategoryService;

@Service("categoryService")
public class CategoryServiceImpl extends SqlBaseService<Category> implements
		ICategoryService {

	@Resource(name = "categoryDao")
	private ICategoryDao categoryDao;

	@Override
	protected IHibernateBaseDao<Category> getDao() {
		// TODO Auto-generated method stub
		return categoryDao;
	}

	@Override
	public Category getCategory(String categoryName, String type) {
		// TODO Auto-generated method stub
		return categoryDao.getCategory(categoryName, type);
	}

}
