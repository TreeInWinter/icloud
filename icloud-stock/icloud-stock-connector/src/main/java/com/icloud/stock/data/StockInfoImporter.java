package com.icloud.stock.data;

import com.travelzen.framework.file.TextFile;

/**
 *
 * @author jiangningcui
 *
 */
public class StockInfoImporter {
	public void importSHAFile() {
		String filePath = "/home/jiangningcui/workspace/mygithub/icloud/icloud-stock/icloud-stock-connector/src/main/resources/data/sha.txt";
		importFile(filePath, "上海");
	}

	public void importSZXFile() {
		String filePath = "/home/jiangningcui/workspace/mygithub/icloud/icloud-stock/icloud-stock-connector/src/main/resources/data/szx.txt";
		importFile(filePath, "深圳");
	}

	public void importFile(String filePath, String stock_type) {
		String content = getContent(filePath);
		String[] pairs = content.split(" ");
		for (String pair : pairs) {
			String[] info = getStockInfo(pair);
			System.out.println(info[0] + " " + info[1]);
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

	public String[] getStockInfo(String pair) {
		String[] tokens = null;
		pair = pair.replace(")", "").trim();
		pair = pair.replace("(", "#").trim();
		tokens = pair.split("#");
		return tokens;
	}

	public static void main(String[] args) {
		StockInfoImporter importer = new StockInfoImporter();
		// importer.importSHAFile();
		importer.importSZXFile();
	}

}
