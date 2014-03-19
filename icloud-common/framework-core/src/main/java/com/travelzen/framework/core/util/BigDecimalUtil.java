package com.travelzen.framework.core.util;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * 
 * @author guobinwu
 * BigDecimal类型数据 统计、显示工具类
 * 
 * 用double作为参数的构造函数，无法精确构造一个BigDecimal对象，需要自己指定一个上下文的环境，也就是指定精确位。
 * 而利用String对象作为参数传入的构造函数能精确的构造出一个BigDecimal对象。
 *
 */
public class BigDecimalUtil {
	
	private static final int DEFAULT_DIV_SCALE = 2;
	
/*	ROUND_CEILING: 舍位时往正无穷方向移动   1.1-> 2   1.5-> 2   1.8-> 2   -1.1-> -1   -1.5-> -1   -1.8-> -1 
	ROUND_DOWN:	向0的方向移动1.1-> 1   1.5-> 1   1.8-> 1   -1.1-> -1   -1.5-> -1   -1.8> -1 
	ROUND_FLOOR: 与CEILING相反，往负无穷   1.1-> 1   1.5-> 1   1.8-> 1   -1.1-> -2   -1.5-> -2   -1.8-> -2 
	ROUND_HALF_DOWN: 以5为分界线，或曰五舍六入1.5-> 1   1.6-> 1   -1.5-> -1   -1.6-> -2   
	ROUND_HALF_EVEN: 同样以5为分界线，如果是5，则前一位变偶数1.15-> 1.2   1.16-> 1.2   1.25-> 1.2   1.26-> 1.3 
	ROUND_HALF_UP: 最常见的四舍五入 
	ROUND_UNNECESSARY: 无需舍位 
	ROUND_UP: 与ROUND_DOWN，远离0的方向1.1-> 2   1.5-> 2   1.8-> 2   -1.1-> -2   -1.5-> -2   -1.8-> -2 */
		 
	/**
	 * double类型的加法
	 */
	 public static double add(double v1, double v2, MathContext scale) {
	       BigDecimal b1 = new BigDecimal(Double.toString(v1));
	       BigDecimal b2 = new BigDecimal(Double.toString(v2));
//	       return b1.add(b2).doubleValue();
	       return b1.add(b2, scale).doubleValue();
	 }
	  
	 public static String add(String v1, String v2){
	       BigDecimal b1 = new BigDecimal(v1);
	       BigDecimal b2 = new BigDecimal(v2);
	       return b1.add(b2).toString();
	 }
	 /**
	  * 
	  * @param v1
	  * @param v2
	  * @return   v1-v2
	  * 精确的double 减法
	  */
	 public static double subtract(double v1, double v2){
	       BigDecimal b1 = new BigDecimal(Double.toString(v1));
	       BigDecimal b2 = new BigDecimal(Double.toString(v2));
	       return b1.subtract(b2).doubleValue();
	 }
	  
	 public static String subtract(String v1, String v2){
	       BigDecimal b1 = new BigDecimal(v1);
	       BigDecimal b2 = new BigDecimal(v2);
	       return b1.subtract(b2).toString();
	 }
	 
	 /**
	  * 
	  * @param v1
	  * @param v2
	  * @return  v1*v2
	  * 乘法计算
	  */
	 public static double multiply(double v1, double v2){
	       BigDecimal b1 = new BigDecimal(Double.toString(v1));
	       BigDecimal b2 = new BigDecimal(Double.toString(v2));
	       return b1.multiply(b2).doubleValue();
	 }
	  
	 public static String multiply(String v1, String v2){
	       BigDecimal b1 = new BigDecimal(v1);
	       BigDecimal b2 = new BigDecimal(v2);
	       return b1.multiply(b2).toString();
	 }

	 /**
	  * 提供精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。舍入模式进行指定
	  * @param v1
	  * @param v2
	  * @param scale  表示需要精确到小数点以后几位
	  * @param round_mode  表示用户指定的舍入模式 
	  * @return 两个参数的商
	  */

	 public static double divide(double v1, double v2, int scale, int round_mode) {
		 
	  if (scale < 0) {
		  throw new IllegalArgumentException("The scale must be a positive integer or zero");
	  }
	  if (v2 == 0)
			return 0f;
	  BigDecimal b1 = new BigDecimal(Double.toString(v1));
	  BigDecimal b2 = new BigDecimal(Double.toString(v2));
	  return b1.divide(b2, scale, round_mode).doubleValue();

	 }
	 
	 /**
	  * 
	  * @param v1
	  * @param v2
	  * @return
	  * 项目中大部分用到的简单方法 返回float  保留两位小数  四舍五入
	  */
	 public static float divide(double v1, double v2) {
		  if (v2 == 0)
				return 0f;
		  BigDecimal b1 = new BigDecimal(Double.toString(v1));
		  BigDecimal b2 = new BigDecimal(Double.toString(v2));
		  return b1.divide(b2, DEFAULT_DIV_SCALE , BigDecimal.ROUND_HALF_UP).floatValue();

		 }
	 
//	 public static String divide(long v1, long v2, int scale, int round_mode) {
//		 
//		  if (scale < 0) {
//			  throw new IllegalArgumentException("The scale must be a positive integer or zero");
//		  }
//
//		  BigDecimal b1 = new BigDecimal(Long.toString(v1));
//		  BigDecimal b2 = new BigDecimal(Long.toString(v2));
//		  return b1.divide(b2, scale, round_mode).toString();
//
//		 }
	
	 /**
	  * 提供精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。舍入模式进行指定
	  * @param v1
	  * @param v2
	  * @param scale  表示需要精确到小数点以后几位
	  * @param round_mode  表示用户指定的舍入模式 
	  * @return 两个参数的商
	  */
	 public static String divide(String v1, String v2, int scale, int round_mode){
	   if(scale < 0){
	         throw new IllegalArgumentException("The scale must be a positive integer or zero");
	       }
	   if("0".equals(v2.trim())) return "0";
//	   if (StringUtils.isNumeric(v1) && StringUtils.isNumeric(v2)){//加了这个判断之后 float、long类型全部失效
		   BigDecimal b1 = new BigDecimal(v1);
	       BigDecimal b2 = new BigDecimal(v2);
	       return b1.divide(b2, scale, round_mode).toString();
//	   } else{
//		   throw new IllegalArgumentException("The Parameter is not a numeric type");
//	   }
	 }

	public static float dividePercent(String v1, String v2, int scale,
			int round_mode) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		if ("0".equals(v2.trim()))
			return 0f;
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		BigDecimal m = new BigDecimal(100);
		return b1.multiply(m).divide(b2, scale, round_mode).floatValue();

	}
	public static float dividePercent(long v1, long v2, int scale,
			int round_mode) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		if (v2 == 0)
			return 0f;
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		BigDecimal m = new BigDecimal(100);
		return b1.multiply(m).divide(b2, scale, round_mode).floatValue();

	}
	 

	 public static BigDecimal getRound(double dou, int scale, int roundmode) {
//	  BigDecimal paramNumber = new BigDecimal(dou);
//	  return paramNumber.setScale(scale, roundmode);
	  return new BigDecimal(dou).setScale(scale , roundmode);
	  
	 }
	 
	 /**
	  * 
	  * @param dou
	  * @param scale
	  * @param roundmode
	  * @return
	  * 计费方式各种计算的结果保存
	  */
	 public static BigDecimal getRound(double dou) {
		  return new BigDecimal(dou).setScale(DEFAULT_DIV_SCALE , BigDecimal.ROUND_HALF_UP);
	}
	 
	 
	 

		/**
		 * 判断是否是大于零的Integer整数
		 * 
		 * @author dylan_xu
		 * @date Mar 11, 2012
		 * @param obj
		 * @return
		 */
		public static boolean isGTIntegerZero(Object obj) {
			return ((obj != null) && (obj instanceof Integer) && (((Integer) obj)
					.intValue() > 0));
		}

		/**
		 * 判断是否是大于零的Long整数
		 * 
		 * @author dylan_xu
		 * @date Mar 11, 2012
		 * @param obj
		 * @return
		 */
		public static boolean isGTLongZero(Object obj) {
			return ((obj != null) && (obj instanceof Long) && (((Long) obj)
					.longValue() > 0));
		}

		/**
		 * 精确的加法运算
		 * 
		 * @author dylan_xu
		 * @date Mar 11, 2012
		 * @param v1
		 * @param v2
		 * @return
		 */
		public static double add(double v1, double v2) {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			return b1.add(b2).doubleValue();
		}

 

 

		/**
		 * 提供精确的乘法运算，并对运算结果截位.
		 * 
		 * @author dylan_xu
		 * @date Mar 11, 2012
		 * @param v1
		 * @param v2
		 * @param scale
		 *            运算结果小数后精确的位数
		 * @return
		 */
		public static double multiply(double v1, double v2, int scale) {
			if (scale < 0) {
				throw new IllegalArgumentException(
						"The scale must be a positive integer or zero");
			}
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
		}

 

		/**
		 * 提供（相对）精确的除法运算. 由scale参数指定精度，以后的数字四舍五入.
		 * 
		 * @author dylan_xu
		 * @date Mar 11, 2012
		 * @param v1
		 *            被除数
		 * @param v2
		 *            除数
		 * @param scale
		 *            表示表示需要精确到小数点以后几位
		 * @return
		 */
		public static double divide(double v1, double v2, int scale) {
			if (scale < 0) {
				throw new IllegalArgumentException(
						"The scale must be a positive integer or zero");
			}

			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		}

		/**
		 * 提供精确的小数位四舍五入处理.
		 * 
		 * @author dylan_xu
		 * @date Mar 11, 2012
		 * @param v
		 *            需要四舍五入的数字
		 * @param scale
		 *            小数点后保留几位
		 * @return
		 */
		public static double round(double v, int scale) {
			if (scale < 0) {
				throw new IllegalArgumentException(
						"The scale must be a positive integer or zero");
			}
			BigDecimal b = new BigDecimal(v);
			return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		}

		/**
		 * 提供精确的类型转换(Float)
		 * 
		 * @author dylan_xu
		 * @date Mar 11, 2012
		 * @param v
		 *            需要被转换的数字
		 * @return 返回转换结果
		 */
		public static float convertsToFloat(double v) {
			BigDecimal b = new BigDecimal(v);
			return b.floatValue();
		}

		/**
		 * 提供精确的类型转换(Int)不进行四舍五入
		 * 
		 * @author dylan_xu
		 * @date Mar 11, 2012
		 * @param v需要被转换的数字
		 * @return 返回转换结果
		 */
		public static int convertsToInt(double v) {
			BigDecimal b = new BigDecimal(v);
			return b.intValue();
		}

		/**
		 * 提供精确的类型转换(Long)
		 * 
		 * @author dylan_xu
		 * @date Mar 11, 2012
		 * @param v
		 *            需要被转换的数字
		 * @return 返回转换结果
		 */
		public static long convertsToLong(double v) {
			BigDecimal b = new BigDecimal(v);
			return b.longValue();
		}

		/**
		 * 返回两个数中大的一个值
		 * 
		 * @author dylan_xu
		 * @date Mar 11, 2012
		 * @param v1
		 *            需要被对比的第一个数
		 * @param v2
		 *            需要被对比的第二个数
		 * @return 返回两个数中大的一个值
		 */
		public static double returnMax(double v1, double v2) {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			return b1.max(b2).doubleValue();
		}

		/**
		 * 返回两个数中大的一个值
		 * 
		 * @author dylan_xu
		 * @date Mar 11, 2012
		 * @param v1
		 *            需要被对比的第一个数
		 * @param v2
		 *            需要被对比的第二个数
		 * @return 返回两个数中小的一个值
		 */
		public static double returnMin(double v1, double v2) {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			return b1.min(b2).doubleValue();
		}

		/**
		 * 精确对比两个数字
		 * 
		 * @author dylan_xu
		 * @date Mar 11, 2012
		 * @param v1
		 *            需要被对比的第一个数
		 * @param v2
		 *            需要被对比的第二个数
		 * @return 如果两个数一样则返回0，如果第一个数比第二个数大则返回1，反之返回-1
		 */
		public static int compareTo(double v1, double v2) {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			return b1.compareTo(b2);
		}

		public static void main(String[] args) {
			double v1 = 3.565;
			double v2 = 5.345;

			System.out.println(convertsToLong(v1));
			System.out.println(convertsToFloat(v2));
			System.out.println(convertsToInt(v1));
			System.out.println(round(v1, 1));
		}

	
}
