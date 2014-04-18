package com.icloud.stock.connector.parser.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import com.icloud.stock.connector.parser.Parser;
import com.icloud.stock.model.StockDateHistory;
import com.travelzen.framework.file.TextFile;
import com.travelzen.framework.util.DateUtils;

public class StockDateHistoryPaser implements
		Parser<ArrayList<StockDateHistory>> {

	@Override
	public ArrayList<StockDateHistory> parse(String content, Object... params) {
		// TODO Auto-generated method stub
		// System.out.println(content);
		if (params != null) {
			InputStream inputStreamCVS = (InputStream) params[0];
			String stockCode = (String) params[1];
			Integer stockId = (Integer) params[2];

			TextFile textFile = new TextFile(inputStreamCVS);
			ArrayList<StockDateHistory> list = new ArrayList<StockDateHistory>();
			for (String str : textFile) {
				StockDateHistory stockDateHistory = parser(str, stockCode,
						stockId);
				if (stockDateHistory != null) {
					list.add(stockDateHistory);
				}
			}
			return list;
		}
		return null;
	}

	/**
	 * 分析 1991-01-15,1.51,1.51,1.51,1.51,565800,0.01 日期
	 *
	 * @param str
	 * @return
	 */
	private StockDateHistory parser(String str, String stockCode, int stockId) {
		// TODO Auto-generated method stub
		if (checkIsIllega(str)) {
			String[] tokens = str.split(",");
			if (tokens.length == 7) {
				StockDateHistory stockDateHistory = new StockDateHistory();
				stockDateHistory.setStockCode(stockCode);
				stockDateHistory.setStockId(stockId);

				Date date = DateUtils.getDate(tokens[0], "yyyy-MM-dd");
				stockDateHistory.setCreateTime(date);
				stockDateHistory.setOpenPrice(Double.parseDouble(tokens[1]));
				stockDateHistory.setHighPrice(Double.parseDouble(tokens[2]));
				stockDateHistory.setLowPrice(Double.parseDouble(tokens[3]));
				stockDateHistory.setClosePrice(Double.parseDouble(tokens[4]));
				stockDateHistory.setVolume(Integer.parseInt(tokens[5]));
				stockDateHistory.setAdjPrice(Double.parseDouble(tokens[6]));
				return stockDateHistory;
			}
		}
		return null;
	}

	/**
	 * 检查，是否合法
	 */
	private boolean checkIsIllega(String str) {
		boolean flag = true;
		if (str != null) {
			for (char ch : str.toCharArray()) {
				if (ch == '-' || ch == ',' || ch == '.'
						|| (ch >= '0' && ch <= '9')) {

				} else {
					flag = false;
				}
			}
		}
		return flag;
	}

	public static void main(String[] args) {
		StockDateHistoryPaser paser = new StockDateHistoryPaser();
		boolean checkIsIllega = paser
				.checkIsIllega("1991-01-02,1.58,1.58,1.58,1.58,2505900,0.01");
		System.out.println(checkIsIllega);
	}
}
