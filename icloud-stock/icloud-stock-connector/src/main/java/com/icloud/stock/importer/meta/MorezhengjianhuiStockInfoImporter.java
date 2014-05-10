package com.icloud.stock.importer.meta;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.icloud.framework.file.TextFile;
import com.icloud.stock.ctx.BeansUtil;
import com.icloud.stock.model.Category;
import com.icloud.stock.model.CategoryStock;
import com.icloud.stock.model.Stock;
import com.icloud.stock.model.constant.StockConstants.BaseCategory;
import com.icloud.stock.model.constant.StockConstants.StockLocation;
import com.icloud.stock.service.ICategoryService;
import com.icloud.stock.service.ICategoryStockService;
import com.icloud.stock.service.IStockService;

/**
 *
 * @author jiangningcui
 *
 */
public class MorezhengjianhuiStockInfoImporter extends BaseServiceImporter {

	public void importzhengjianhuiInfo() {
		String filePath = "/home/jiangningcui/workspace/mygithub/icloud/icloud-data/xueqiu/processed/zhengjianhuihangye.txt";
		importFile(filePath, BaseCategory.ZHENGJIANHUI);
	}

	public void importFile(String filePath, BaseCategory baseCategory) {
		List<String> content = getContent(filePath);
		/**
		 * 类别
		 */
		Map<String, Category> categoryMap = new HashMap<String, Category>();
		for (String str : content) {
			Category category = getzhegnjianhuiCategory(str, baseCategory);
			if (categoryMap.get(category.getCategoryName()) == null) {
				Category tmpValue = categoryService.getCategory(
						category.getCategoryName(), baseCategory.getType());
				if (tmpValue == null) {
					categoryService.save(category);
					categoryMap.put(category.getCategoryName(), category);
				} else {
					categoryMap.put(category.getCategoryName(), tmpValue);
				}
			}
		}
		System.out.println("---------------------");

		int count = content.size();
		int i = 0;
		for (String str : content) {
			Stock stock = getzhegnjianhuiStockInfo(str);
			List<Stock> tmpList = stockService.findByProperies("stockCode",
					stock.getStockCode());
			if (tmpList == null || tmpList.size() == 0) {
				stockService.save(stock);
				System.out.println(stock.getStockCode());
				System.out.println("no match : " + stock.getStockCode() + " " + stock.getStockName());
			} else {
				stock = tmpList.get(0);
			}
			i++;
			if (i % 100 == 0) {
				System.out.println(i + "  " + count);
			}
			// 找一下类别
			Category category = getzhegnjianhuiCategory(str, baseCategory);
			category = categoryMap.get(category.getCategoryName());
			CategoryStock cs = new CategoryStock();
			cs.setCategory(category);
			cs.setStock(stock);
			categoryStockService.save(cs);
			System.out.println(stock.getStockCode());
		}
		System.out.println("------------------end");
	}

	public List<String> getContent(String filePath) {
		TextFile textFile = new TextFile(filePath);
		List<String> list = new ArrayList<String>();
		for (String text : textFile) {
			list.add(text);
		}
		return list;
	}

	public Category getzhegnjianhuiCategory(String pair, BaseCategory base) {
		String[] tokens = pair.trim().split(" ");
		Category category = new Category();
		category.setCategoryCategoryType(base.getType());
		category.setCategoryName(tokens[0]);
		category.setCategoryRank(1.0d);
		return category;
	}

	public Stock getzhegnjianhuiStockInfo(String pair) {
		String[] tokens = null;
		tokens = pair.trim().split(" ");
		Stock stock = new Stock();
		stock.setCreateTime(new Date());
		stock.setUpdateTime(new Date());

		stock.setStockAllCode(tokens[4]);
		stock.setStockName(tokens[3]);
		stock.setStockLocation(getLocation(tokens[4]));
		stock.setStockCode(getCode(tokens[4]));
//		System.out.println(stock.getStockName() +  " " + stock.getStockCode());
		return stock;
	}

	public static String getLocation(String code) {
		code = code.toLowerCase();
		if (code.startsWith("sz")) {
			return StockLocation.SZX.getLocation();
		}
		return StockLocation.SHA.getLocation();
	}

	public static String getCode(String code) {
		code = code.toLowerCase();
		code = code.replace("sz", "").replace("sh", "");
		return code;
	}

	public static void main(String[] args) {
		MorezhengjianhuiStockInfoImporter importer = new MorezhengjianhuiStockInfoImporter();
		importer.importzhengjianhuiInfo();
	}
}
