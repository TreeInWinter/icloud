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
		list.add(new BaseStockMenu(BaseCategory.BASE.getType(), url,
				BaseCategory.BASE.getName(), null));
		list.add(new BaseStockMenu(BaseCategory.XUEQIU.getType(), url,
				BaseCategory.XUEQIU.getName(), null));
		list.add(new BaseStockMenu(BaseCategory.ZHENGJIANHUI.getType(), url,
				BaseCategory.ZHENGJIANHUI.getName(), null));
		return list;
	}

	public List<StockMenuBean> getStockMenuBean(String type) {
		String fatherName = null;
		if (!ICloudUtils.isNotNull(type)) {
			type = BaseCategory.BASE.getType();
			fatherName = BaseCategory.BASE.getName();
		}
		type = type.trim();
		BaseCategory baseCategory = BaseCategory.getBaseCategory(type);

		type = baseCategory.getType();
		fatherName = baseCategory.getName();

		List<Category> categorys = this.categoryService.getCategoryByType(type);
		return buildStockMenuBean(categorys, fatherName);
	}

	/**
	 * 每个东西超过7之后,并且后面的首字母相同,便进行合并
	 * 
	 * @param categorys
	 * @return
	 */
	public List<StockMenuBean> buildStockMenuBean(List<Category> categorys,
			String fatherName) {
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
				bean.addCategory(category, fatherName);
			} else if (lastChar == currentChar) {
				bean.addCategory(category, fatherName);
			} else { // 不相同
				lastChar = currentChar;// firstChar不改变
				bean.addCategory(category, fatherName);
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

	public BaseStockMenu getBaseStockMenu(String cateId) {
		int id = ICloudUtils.parseInt(cateId);
		if (id != -1) {
			Category category = this.categoryService.getById(id);
			if (ICloudUtils.isNotNull(category)) {
				String categoryCategoryType = category
						.getCategoryCategoryType();
				BaseCategory baseCategory = BaseCategory
						.getBaseCategory(categoryCategoryType);

				String name = category.getCategoryName();
				String fatcherName = baseCategory.getName();

				BaseStockMenu menu = new BaseStockMenu(cateId, "/", name,
						fatcherName);
				return menu;
			}
		}
		return BaseStockMenu.getDefaultStockMenu();
	}
}
