package com.icloud.stock.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.icloud.framework.dao.hibernate.IHibernateBaseDao;
import com.icloud.framework.service.impl.SqlBaseService;
import com.icloud.stock.dao.ICategoryStockDao;
import com.icloud.stock.model.CategoryStock;
import com.icloud.stock.service.ICategoryStockService;

@Service("categoryStockService")
public class CategoryStockServiceImpl extends SqlBaseService<CategoryStock>
		implements ICategoryStockService {

	@Resource(name = "categoryStockDao")
	private ICategoryStockDao categoryStockDao;

	@Override
	protected IHibernateBaseDao<CategoryStock> getDao() {
		// TODO Auto-generated method stub
		return categoryStockDao;
	}

}
