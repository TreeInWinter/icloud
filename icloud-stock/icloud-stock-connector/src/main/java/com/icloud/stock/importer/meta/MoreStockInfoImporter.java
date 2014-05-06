package com.icloud.stock.importer.meta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.icloud.framework.file.TextFile;
import com.icloud.stock.ctx.BeansUtil;
import com.icloud.stock.model.Stock;
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
		for (String str : content) {
			Stock stock = getStockInfo(str);
			// stockService.save(stock);
			// System.out.println("save stock: " + stock.getStockCode() + "  "
			// + stock.getStockName());
		}
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
		stock.setStockCode(tokens[1]);
		stock.setStockLocation(stock_loation);
		stock.setStockName(tokens[0]);

		return stock;
	}

	public static void main(String[] args) {
		MoreStockInfoImporter importer = new MoreStockInfoImporter();
		importer.importMetaInfo();

	}
}
