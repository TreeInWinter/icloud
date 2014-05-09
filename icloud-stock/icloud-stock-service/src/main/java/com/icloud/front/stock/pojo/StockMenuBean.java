package com.icloud.front.stock.pojo;

import java.util.ArrayList;
import java.util.List;

import com.icloud.stock.model.Category;

public class StockMenuBean {
	private String name;
	private List<BaseStockMenu> menus;

	public StockMenuBean() {
		menus = new ArrayList<BaseStockMenu>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<BaseStockMenu> getMenus() {
		return menus;
	}

	public void setMenus(List<BaseStockMenu> menus) {
		this.menus = menus;
	}

	public boolean addCategory(Category category) {
		if (category == null)
			return false;
		BaseStockMenu menu = new BaseStockMenu(category.getId() + "", "/",
				category.getCategoryName());
		this.menus.add(menu);
		return true;
	}

}
