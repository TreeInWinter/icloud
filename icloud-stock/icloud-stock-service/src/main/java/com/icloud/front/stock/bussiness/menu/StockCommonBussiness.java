package com.icloud.front.stock.bussiness.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.icloud.framework.util.ICloudUtils;
import com.icloud.front.stock.bussiness.BaseAction;
import com.icloud.front.stock.pojo.BaseStockMenu;
import com.icloud.front.stock.pojo.StockMenuBean;
import com.icloud.search.util.PinyinAutoCompletetor;
import com.icloud.stock.model.Category;
import com.icloud.stock.model.constant.StockConstants.BaseCategory;

@Service("stockCommonBussiness")
public class StockCommonBussiness extends BaseAction {
	public List<BaseStockMenu> getBaseMenu() {
		String url = "/stock";
		List<BaseStockMenu> list = new ArrayList<BaseStockMenu>();
		list.add(new BaseStockMenu(BaseCategory.BASE.getType(), url, "基础分类"));
		list.add(new BaseStockMenu(BaseCategory.XUEQIU.getType(), url, "必有分类"));
		list.add(new BaseStockMenu(BaseCategory.ZHENGJIANHUI.getType(), url,
				"精细分类"));
		return list;
	}

	public List<StockMenuBean> getStockMenuBean(String type) {
		if (!ICloudUtils.isNotNull(type)) {
			type = BaseCategory.BASE.getType();
		}
		type = type.trim();
		if (!BaseCategory.XUEQIU.getType().equalsIgnoreCase(type)
				&& !BaseCategory.ZHENGJIANHUI.getType().equalsIgnoreCase(type)) {
			type = BaseCategory.BASE.getType();
		}
		List<Category> categorys = this.categoryService.getCategoryByType(type);
		// for (Category category : categorys) {
		// System.out.println(category.getCategoryCategoryType() + "  "
		// + category.getCategoryName() + "  " + category.getId());
		// }
		return buildStockMenuBean(categorys);
	}

	/**
	 * 每个东西超过7之后,并且后面的首字母相同,便进行合并
	 *
	 * @param categorys
	 * @return
	 */
	public List<StockMenuBean> buildStockMenuBean(List<Category> categorys) {
		if (ICloudUtils.isEmpty(categorys)) {
			return null;
		}
		Collections.sort(categorys, new Comparator<Category>() {

			@Override
			public int compare(Category o1, Category o2) {
				// TODO Auto-generated method stub
				char ch = PinyinAutoCompletetor.getFirstPinyinChar(o1
						.getCategoryName());
				char ch2 = PinyinAutoCompletetor.getFirstPinyinChar(o2
						.getCategoryName());
				return ch > ch2 ? 1 : -1;
			}
		});

		List<StockMenuBean> list = new ArrayList<StockMenuBean>();
		StockMenuBean bean = new StockMenuBean();
		char firstChar = 0;
		char lastChar = 0;
		for (Category category : categorys) {
			char currentChar = PinyinAutoCompletetor
					.getFirstPinyinChar(category.getCategoryName());
			if (lastChar == 0) {
				lastChar = currentChar;
				firstChar = currentChar;
				bean.addCategory(category);
			} else if (lastChar == currentChar) {
				bean.addCategory(category);
			} else { // 不相同
				lastChar = currentChar;// firstChar不改变
				bean.addCategory(category);
				if (bean.getMenus().size() > 6) {// 修改
					bean.setName(getMenuName(firstChar, lastChar));
					list.add(bean);
					bean = new StockMenuBean();
					firstChar = 0;
					lastChar = 0;
				}
			}
		}
		if (!ICloudUtils.isEmpty(bean.getMenus())) {
			bean.setName(getMenuName(firstChar, lastChar));
			list.add(bean);
		}
		return list;
	}

	public String getMenuName(char firstChar, char lastChar) {
		String name = firstChar + "";
		if (firstChar != lastChar) {
			name = name + "-" + lastChar;
		}
		return name;
	}
}
