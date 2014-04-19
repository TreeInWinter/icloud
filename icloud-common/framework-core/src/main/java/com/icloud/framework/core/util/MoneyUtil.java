package com.icloud.framework.core.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class MoneyUtil {
	private final static RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
	public static enum SCALE{
		YUAN("元", 100),
		TEN_YUAN("十元", 1000);
		private String desc;
		private long value;
		private SCALE(String desc, int value){
			this.desc = desc;
			this.value = value;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
		public long getValue() {
			return value;
		}
		public void setValue(long value) {
			this.value = value;
		}
	}

	public static String cent2Yuan(BigDecimal amount) {
		return amount.divide(new BigDecimal(100), 2, ROUNDING_MODE).toPlainString();
	}

	private static String cent2Yuan(BigDecimal amount, int scale, RoundingMode roundingMode) {
		return amount.divide(new BigDecimal(100), scale, roundingMode).toPlainString();
	}

	/**
	 * 分转为元，只保留整数部分
	 * 
	 * @param amount
	 * @return
	 */
	public static String cent2YuanFloor(String amount) {
		return cent2Yuan(new BigDecimal(amount), 0, RoundingMode.FLOOR);
	}

	public static String cent2YuanCeiling(String amount) {
		return cent2Yuan(new BigDecimal(amount), 0, RoundingMode.CEILING);
	}

	public static String cent2Yuan(String amount) {
		return cent2Yuan(new BigDecimal(amount));
	}

	public static String cent2Yuan(long amount) {
		return cent2Yuan(new BigDecimal(amount));
	}
	
	public static String cent2Yuan(double amount) {
		return cent2Yuan(new BigDecimal(amount));
	}
	public static String cent2YuanFloor(double amount) {
		return cent2Yuan(new BigDecimal(amount),0, RoundingMode.FLOOR);
	}

	/**
	 * 分转为元，只保留整数部分,小数部分全舍
	 * 
	 * @param amount
	 * @return
	 */
	public static String cent2YuanFloor(long amount) {
		return cent2YuanFloor(String.valueOf(amount));
	}

	public static String cent2YuanCeiling(long amount) {
		return cent2YuanCeiling(String.valueOf(amount));
	}

	public static String yuan2Cent(String amount) {
		return String.valueOf(yuan2Cent(new BigDecimal(amount)));
	}

	public static String yuan2Cent(double amount) {
		return String.valueOf(yuan2Cent(new BigDecimal(amount)));
	}

	public static long yuan2Cent(BigDecimal amount) {
		return amount.multiply(new BigDecimal(100), new MathContext(16, ROUNDING_MODE)).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
	}
	/**
	 * 将以分为单位的金额入到指定的刻度
	 * @param centAmt
	 * @param scale
	 * @return 转换之后的以分为单位的金额
	 */
	public static long roundUpCent(long centAmt, MoneyUtil.SCALE scale){
		if(scale == null)
			return centAmt;
		BigDecimal origAmount = new BigDecimal(centAmt);
		BigDecimal scaleVal = new BigDecimal(scale.getValue());
		return origAmount.divide(scaleVal, 0,RoundingMode.UP).multiply(scaleVal).longValue();
	}
	/**
	 * 将以分为单位的金额舍到指定的刻度
	 * @param centAmt
	 * @param scale
	 * @return 转换之后的以分为单位的金额
	 */
	public static long roundDownCent(long centAmt, MoneyUtil.SCALE scale){
		if(scale == null)
			return centAmt;
		BigDecimal origAmount = new BigDecimal(centAmt);
		BigDecimal scaleVal = new BigDecimal(scale.getValue());
		return origAmount.divide(scaleVal, 0,RoundingMode.DOWN).multiply(scaleVal).longValue();
	}
}
