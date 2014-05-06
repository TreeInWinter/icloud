package com.icloud.front.pojo;

import java.util.List;

public class StockMenuBean {
	private String name;
	private List<BaseStockMenu> menus;

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

}
