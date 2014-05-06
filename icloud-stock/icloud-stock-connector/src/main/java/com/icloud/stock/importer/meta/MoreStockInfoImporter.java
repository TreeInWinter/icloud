package com.icloud.stock.importer.meta;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.icloud.framework.file.TextFile;
import com.icloud.stock.ctx.BeansUtil;
import com.icloud.stock.model.Stock;
import com.icloud.stock.model.constant.StockConstants.StockLocation;
import com.icloud.stock.service.IStockService;

/**
 *
 * @author jiangningcui
 *
 */
public class MoreStockInfoImporter {

	private IStockService stockService;

	public MoreStockInfoImporter() {
		stockService = BeansUtil.getStockService();
	}

	public void importMetaInfo() {
		String filePath = "/home/jiangningcui/workspace/mygithub/icloud/icloud-data/xueqiu/processed/jichufenlei.txt";
		importFile(filePath);
	}

	public void importFile(String filePath) {
		List<String> content = getContent(filePath);
		System.out.println(content.size());
		Set<String> set = new HashSet<String>();
		for (String str : content) {
			Stock stock = getStockInfo(str);
			set.add(stock.getStockCode());
		}
		System.out.println("size : " + set.size());
	}

	public List<String> getContent(String filePath) {
		TextFile textFile = new TextFile(filePath);
		List<String> list = new ArrayList<String>();
		for (String text : textFile) {
			list.add(text);
		}
		return list;
	}

	public Stock getStockInfo(String pair) {
		String[] tokens = null;
		tokens = pair.trim().split(" ");

		Stock stock = new Stock();
		stock.setCreateTime(new Date());
		stock.setUpdateTime(new Date());

		stock.setStockAllCode(tokens[0]);
		stock.setStockName(tokens[1]);
		stock.setStockTypeBase(tokens[2]);
		stock.setStockLocation(getLocation(tokens[0]));
		stock.setStockCode(getCode(tokens[0]));
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
		MoreStockInfoImporter importer = new MoreStockInfoImporter();
		importer.importMetaInfo();

	}
}
