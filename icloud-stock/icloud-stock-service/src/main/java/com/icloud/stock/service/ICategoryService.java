package com.icloud.stock.service;

import com.icloud.framework.service.ISqlBaseService;
import com.icloud.stock.model.Category;

public interface ICategoryService extends ISqlBaseService<Category> {

	public Category getCategory(String categoryName, String type);

}
