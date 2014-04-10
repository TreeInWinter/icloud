package com.icloud.stock.connector.model;

import java.util.Date;

public class StockCurrentInfo {
	private String stockCode;
	private String stockName; // 0：”大秦铁路”，股票名字；

	private double todayPrice; // 1：”27.55″，今日开盘价；

	private double yesterdayPrice;// 2：”27.25″，昨日收盘价；
	private double currentPrice;// 3：”26.91″，当前价格；
	private double todayTopPrice;// 4：”27.55″，今日最高价；
	private double todayLowPrice;// 5：”26.20″，今日最低价；
	private double salePrice;// 6：”26.91″，竞买价，即“买一”报价；
	private double buyPrice;// 7：”26.92″，竞卖价，即“卖一”报价；
	private long doneCount;// 8：”22114263″，成交的股票数，由于股票交易以一百股为基本单位，所以在使用时，通常把该值除以一百；
	private double doneMoney;// 9：”589824680″，成交金额，单位为“元”，为了一目了然，通常以“万元”为成交金额的单位，所以通常把该值除以一万；
	private int buyone;// 10：”4695″，“买一”申请4695股，即47手；
	private double buyonePrice;// 11：”26.91″，“买一”报价；
	private int buyTwo;// 12：”57590″，“买二”
	private double buyTwoPrice;// 13：”26.90″，“买二”
	private int buyThree;// 14：”14700″，“买三”
	private double buyThreePrice;// 15：”26.89″，“买三”
	private int buyFour;// 16：”14300″，“买四”
	private double buyFourPrice;// 17：”26.88″，“买四”
	private int buyFive;// 18：”15100″，“买五”
	private double buyFivePrice;// 19：”26.87″，“买五”
	private int saleOne;// 20：”3100″，“卖一”申报3100股，即31手；
	private double saleOnePrice;// 21：”26.92″，“卖一”报价
	private int saleTwo;
	private double saleTwoPrice;// (22, 23)

	private int saleThree;//
	private double saleThreePrice;// (24, 25)

	private int saleFour;//
	private double saleFourPrice;// (26,27)

	private int saleFive;//
	private double saleFivePrice;// (28,29)

	private Date accessSinaTime;
	// private String date; // 30：”2008-01-11″，日期；
	// private String time; // 31：”15:05:32″，时间；
	private String jsString;

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public double getTodayPrice() {
		return todayPrice;
	}

	public void setTodayPrice(double todayPrice) {
		this.todayPrice = todayPrice;
	}

	public double getYesterdayPrice() {
		return yesterdayPrice;
	}

	public void setYesterdayPrice(double yesterdayPrice) {
		this.yesterdayPrice = yesterdayPrice;
	}

	public double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public double getTodayTopPrice() {
		return todayTopPrice;
	}

	public void setTodayTopPrice(double todayTopPrice) {
		this.todayTopPrice = todayTopPrice;
	}

	public double getTodayLowPrice() {
		return todayLowPrice;
	}

	public void setTodayLowPrice(double todayLowPrice) {
		this.todayLowPrice = todayLowPrice;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public long getDoneCount() {
		return doneCount;
	}

	public void setDoneCount(long doneCount) {
		this.doneCount = doneCount;
	}

	public double getDoneMoney() {
		return doneMoney;
	}

	public void setDoneMoney(double doneMoney) {
		this.doneMoney = doneMoney;
	}

	public int getBuyone() {
		return buyone;
	}

	public void setBuyone(int buyone) {
		this.buyone = buyone;
	}

	public double getBuyonePrice() {
		return buyonePrice;
	}

	public void setBuyonePrice(double buyonePrice) {
		this.buyonePrice = buyonePrice;
	}

	public int getBuyTwo() {
		return buyTwo;
	}

	public void setBuyTwo(int buyTwo) {
		this.buyTwo = buyTwo;
	}

	public double getBuyTwoPrice() {
		return buyTwoPrice;
	}

	public void setBuyTwoPrice(double buyTwoPrice) {
		this.buyTwoPrice = buyTwoPrice;
	}

	public int getBuyThree() {
		return buyThree;
	}

	public void setBuyThree(int buyThree) {
		this.buyThree = buyThree;
	}

	public double getBuyThreePrice() {
		return buyThreePrice;
	}

	public void setBuyThreePrice(double buyThreePrice) {
		this.buyThreePrice = buyThreePrice;
	}

	public int getBuyFour() {
		return buyFour;
	}

	public void setBuyFour(int buyFour) {
		this.buyFour = buyFour;
	}

	public double getBuyFourPrice() {
		return buyFourPrice;
	}

	public void setBuyFourPrice(double buyFourPrice) {
		this.buyFourPrice = buyFourPrice;
	}

	public int getBuyFive() {
		return buyFive;
	}

	public void setBuyFive(int buyFive) {
		this.buyFive = buyFive;
	}

	public double getBuyFivePrice() {
		return buyFivePrice;
	}

	public void setBuyFivePrice(double buyFivePrice) {
		this.buyFivePrice = buyFivePrice;
	}

	public int getSaleOne() {
		return saleOne;
	}

	public void setSaleOne(int saleOne) {
		this.saleOne = saleOne;
	}

	public double getSaleOnePrice() {
		return saleOnePrice;
	}

	public void setSaleOnePrice(double saleOnePrice) {
		this.saleOnePrice = saleOnePrice;
	}

	public int getSaleTwo() {
		return saleTwo;
	}

	public void setSaleTwo(int saleTwo) {
		this.saleTwo = saleTwo;
	}

	public double getSaleTwoPrice() {
		return saleTwoPrice;
	}

	public void setSaleTwoPrice(double saleTwoPrice) {
		this.saleTwoPrice = saleTwoPrice;
	}

	public int getSaleThree() {
		return saleThree;
	}

	public void setSaleThree(int saleThree) {
		this.saleThree = saleThree;
	}

	public double getSaleThreePrice() {
		return saleThreePrice;
	}

	public void setSaleThreePrice(double saleThreePrice) {
		this.saleThreePrice = saleThreePrice;
	}

	public int getSaleFour() {
		return saleFour;
	}

	public void setSaleFour(int saleFour) {
		this.saleFour = saleFour;
	}

	public double getSaleFourPrice() {
		return saleFourPrice;
	}

	public void setSaleFourPrice(double saleFourPrice) {
		this.saleFourPrice = saleFourPrice;
	}

	public int getSaleFive() {
		return saleFive;
	}

	public void setSaleFive(int saleFive) {
		this.saleFive = saleFive;
	}

	public double getSaleFivePrice() {
		return saleFivePrice;
	}

	public void setSaleFivePrice(double saleFivePrice) {
		this.saleFivePrice = saleFivePrice;
	}

	public Date getAccessSinaTime() {
		return accessSinaTime;
	}

	public void setAccessSinaTime(Date accessSinaTime) {
		this.accessSinaTime = accessSinaTime;
	}

	public String getJsString() {
		return jsString;
	}

	public void setJsString(String jsString) {
		this.jsString = jsString;
	}

	@Override
	public String toString() {
		return "StockCurrentInfo [stockCode=" + stockCode + ", stockName="
				+ stockName + ", todayPrice=" + todayPrice
				+ ", yesterdayPrice=" + yesterdayPrice + ", currentPrice="
				+ currentPrice + ", todayTopPrice=" + todayTopPrice
				+ ", todayLowPrice=" + todayLowPrice + ", salePrice="
				+ salePrice + ", buyPrice=" + buyPrice + ", doneCount="
				+ doneCount + ", doneMoney=" + doneMoney + ", buyone=" + buyone
				+ ", buyonePrice=" + buyonePrice + ", buyTwo=" + buyTwo
				+ ", buyTwoPrice=" + buyTwoPrice + ", buyThree=" + buyThree
				+ ", buyThreePrice=" + buyThreePrice + ", buyFour=" + buyFour
				+ ", buyFourPrice=" + buyFourPrice + ", buyFive=" + buyFive
				+ ", buyFivePrice=" + buyFivePrice + ", saleOne=" + saleOne
				+ ", saleOnePrice=" + saleOnePrice + ", saleTwo=" + saleTwo
				+ ", saleTwoPrice=" + saleTwoPrice + ", saleThree=" + saleThree
				+ ", saleThreePrice=" + saleThreePrice + ", saleFour="
				+ saleFour + ", saleFourPrice=" + saleFourPrice + ", saleFive="
				+ saleFive + ", saleFivePrice=" + saleFivePrice
				+ ", accessSinaTime=" + accessSinaTime + ", jsString="
				+ jsString + "]";
	}

}
