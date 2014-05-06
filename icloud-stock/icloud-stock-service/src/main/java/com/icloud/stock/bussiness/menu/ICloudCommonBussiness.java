package com.icloud.stock.bussiness.menu;

import java.util.ArrayList;
import java.util.List;

import com.icloud.stock.bussiness.BaseAction;
import com.icloud.stock.model.constant.StockConstants.BaseCategory;

public class StockCommonBussiness extends BaseAction {
	public static List<BaseStockMenu> getBaseMenu() {
		String url = "/stock";
		List<BaseStockMenu> list = new ArrayList<BaseStockMenu>();
		list.add(new BaseStockMenu(BaseCategory.BASE.getType(), url, "基础分类"));
		list.add(new BaseStockMenu(BaseCategory.XUEQIU.getType(), url, "必有分类"));
		list.add(new BaseStockMenu(BaseCategory.ZHENGJIANHUI.getType(), url,
				"精细分类"));
		return list;
	}

}
