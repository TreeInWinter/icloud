package com.icloud.stock.data;

import java.util.Date;

import net.sf.ehcache.hibernate.management.impl.BeanUtils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.icloud.stock.model.Stock;
import com.icloud.stock.model.constant.StockConstants.StockLocation;
import com.icloud.stock.service.IStockService;
import com.travelzen.framework.file.TextFile;

/**
 * 
 * @author jiangningcui
 * 
 */
public class StockInfoImporter {
	private ApplicationContext app;
	private IStockService stockService;

	public StockInfoImporter() {
		app = new ClassPathXmlApplicationContext(
				"spring/applicationContext-stock.xml");
		stockService = (IStockService) app.getBean("stockService");
	}

	public void importSHAFile() {
		String filePath = "bin/data/sha.txt";
		importFile(filePath, StockLocation.SHA.getLocation());
	}

	public void importSZXFile() {
		String filePath = "bin/data/szx.txt";
		importFile(filePath, StockLocation.SZX.getLocation());
	}

	public void importFile(String filePath, String stock_loation) {
		String content = getContent(filePath);
		String[] pairs = content.split(" ");
		for (String pair : pairs) {
			Stock stock = getStockInfo(pair, stock_loation);
			stockService.save(stock);
			System.out.println("save stock: " + stock.getStockCode() + "  "
					+ stock.getStockName());
		}
	}

	public String getContent(String filePath) {
		TextFile textFile = new TextFile(filePath);
		StringBuffer sb = new StringBuffer();
		for (String text : textFile) {
			sb.append(text);
			break;
		}
		return sb.toString();
	}

	public Stock getStockInfo(String pair, String stock_loation) {
		String[] tokens = null;
		pair = pair.replace(")", "").trim();
		pair = pair.replace("(", "#").trim();
		tokens = pair.split("#");

		Stock stock = new Stock();
		stock.setCreateTime(new Date());
		stock.setUpdateTime(new Date());
		stock.setStockCode(tokens[1]);
		stock.setStockLocation(stock_loation);
		stock.setStockName(tokens[0]);

		return stock;
	}

	public static void main(String[] args) {
		StockInfoImporter importer = new StockInfoImporter();
		importer.importSHAFile();
		importer.importSZXFile();

	}
}
