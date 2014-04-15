package com.icloud.stock.connector.parser.impl;

import java.util.Date;

import org.apache.commons.httpclient.util.DateUtil;

import com.icloud.stock.connector.model.StockCurrentInfo;
import com.icloud.stock.connector.parser.Parser;
import com.travelzen.framework.util.DateUtils;

public class StockCurrentInfoPaser implements Parser<StockCurrentInfo> {

	@Override
	public StockCurrentInfo parse(String content, Object... params) {
		// TODO Auto-generated method stub
		// System.out.println(content);
		if (content != null) {
			content = content.substring(content.indexOf("\"") + 1);
			content = content.substring(0, content.indexOf("\""));
			String[] tokens = content.trim().split(",");
			return parser(tokens);
		}
		return null;
	}

	/**
	 * 分析参数
	 *
	 * @param tokens
	 * @return
	 */
	private StockCurrentInfo parser(String[] tokens) {
		StockCurrentInfo info = null;
		if (tokens != null && tokens.length == 33) {
			info = new StockCurrentInfo();

			info.setStockName(tokens[0]);
			// info.setStockCode(tokens[]);
			info.setTodayPrice(Double.parseDouble(tokens[1]));
			info.setYesterdayPrice(Double.parseDouble(tokens[2]));
			info.setCurrentPrice(Double.parseDouble(tokens[3]));
			info.setTodayTopPrice(Double.parseDouble(tokens[4]));
			info.setTodayLowPrice(Double.parseDouble(tokens[5]));

			info.setSalePrice(Double.parseDouble(tokens[6]));
			info.setBuyPrice(Double.parseDouble(tokens[7]));
			info.setDoneCount(Long.parseLong(tokens[8]));
			info.setDoneMoney(Double.parseDouble(tokens[9]));
			info.setBuyone(Integer.parseInt(tokens[10]));
			info.setBuyonePrice(Double.parseDouble(tokens[11]));

			info.setBuyTwo(Integer.parseInt(tokens[12]));
			info.setBuyTwoPrice(Double.parseDouble(tokens[13]));

			info.setBuyThree(Integer.parseInt(tokens[14]));
			info.setBuyThreePrice(Double.parseDouble(tokens[15]));

			info.setBuyFour(Integer.parseInt(tokens[16]));
			info.setBuyFourPrice(Double.parseDouble(tokens[17]));

			info.setBuyFive(Integer.parseInt(tokens[18]));
			info.setBuyFivePrice(Double.parseDouble(tokens[19]));

			info.setSaleOne(Integer.parseInt(tokens[20]));
			info.setSaleOnePrice(Double.parseDouble(tokens[21]));

			info.setSaleTwo(Integer.parseInt(tokens[22]));
			info.setSaleTwoPrice(Double.parseDouble(tokens[23]));

			info.setSaleThree(Integer.parseInt(tokens[24]));
			info.setSaleThreePrice(Double.parseDouble(tokens[25]));

			info.setSaleFour(Integer.parseInt(tokens[26]));
			info.setSaleFourPrice(Double.parseDouble(tokens[27]));

			info.setSaleFive(Integer.parseInt(tokens[28]));
			info.setSaleFivePrice(Double.parseDouble(tokens[29]));

			Date date = DateUtils.getDate(tokens[30] + " " + tokens[31]);
			info.setAccessSinaTime(date);
		}
		return info;
	}
}
