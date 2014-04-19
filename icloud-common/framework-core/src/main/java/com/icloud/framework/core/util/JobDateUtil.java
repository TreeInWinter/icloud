/**
 * Copyright:Copyright (c)2011
 * Company:travelzen.com
 * @author:peilv
 * Create at:2011-8-23
 * Filename:JobDateUtil.java
 * @desc:
 */
package com.icloud.framework.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description:主要处理与日期相关的数据,例如日期转换、where子句中的日期范围
 * @author peilv
 * @created 2011-8-23
 */
public class JobDateUtil {
	private static java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
			"yyyyMMddHHmm");
	private static java.text.SimpleDateFormat format2 = new java.text.SimpleDateFormat(
			"yyyyMMdd");
	private static java.text.SimpleDateFormat format3 = new java.text.SimpleDateFormat(
	"yyyyMM");

	//返回30分钟前时间的字符串形式
	public static String getLastThirtyMinute(String time){
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			Date format = sdf.parse(time);
			calendar.setTime(format);
			calendar.add(Calendar.MINUTE, -30);
			return sdf.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	//返回30分钟后时间的字符串形式
	public static String getNextThirtyMinute(String time){
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			Date format = sdf.parse(time);
			calendar.setTime(format);
			calendar.add(Calendar.MINUTE, 30);
			return sdf.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 统计上一天数据
	public static String getDateDayInterval(Date time) {
		if (time == null)
			return null;
		Calendar end = null;
		end = Calendar.getInstance();
		end.clear();
		end.setTime(time);
		end.set(Calendar.SECOND, 0);
		end.set(Calendar.MINUTE, 0);
		end.set(Calendar.HOUR_OF_DAY, 0);
		StringBuilder weekInterval = new StringBuilder();
		weekInterval.append("'" + format.format(end.getTime())
				+ "' < timetable and '");
		end.add(Calendar.DATE, 1);
		weekInterval.append(format.format(end.getTime()) + "' > timetable");

		return weekInterval.toString();

	}

	// 统计前七天数据(今天除外)
	public static String getDateWeekInterval(Date time) {
		if (time == null)
			return null;
		Calendar end = null;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyyMMddHHmm");
		end = Calendar.getInstance();
		end.clear();
		end.setTime(time);
		end.set(Calendar.SECOND, 0);
		end.set(Calendar.MINUTE, 0);
		end.set(Calendar.HOUR_OF_DAY, 0);
		StringBuilder weekInterval = new StringBuilder();
		weekInterval.append("'" + format.format(end.getTime())
				+ "' > timetable and '");
		end.add(Calendar.DATE, -7);
		weekInterval.append(format.format(end.getTime()) + "' < timetable");

		return weekInterval.toString();
	}

	// 统计上一个月数据
	public static String getDateMonthInterval(Date time) {
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyyMMddHHmm");
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(time);
		// 取得系统当前时间所在月第一天时间对象
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		StringBuilder monthInterval = new StringBuilder();
		monthInterval.append("'" + format.format(cal.getTime())
				+ "' > timetable and '");

		// 日期减一,取得上月最后一天时间对象
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		monthInterval.append(format.format(cal.getTime()) + "' < timetable");
		return monthInterval.toString();
	}

	// 判断是星期几
	public static String getWeekDayString(Date time)
	{
		String weekString = "";

		final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五","星期六"};
		Calendar calendar = Calendar.getInstance();
		calendar.clear();

		calendar.setTime(time);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		weekString = dayNames[dayOfWeek - 1];

		return weekString;
	}
	//判断是否星期一
	public static boolean ifOneOfWeek(Date time){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		System.out.println(time.toString());
		calendar.setTime(time);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek ==2 ? true:false;
	}
	//判断是否为当月第一天
	public static boolean ifOneOfMonth(Date time){
		Calendar current = Calendar.getInstance();
		current.clear();
		current.setTime(time);
        int i = current.get(Calendar.DAY_OF_MONTH);
        return i==1?true:false;
	}
	//判断是否到达特定时间点
	public static boolean ifTimeOfDay(Date time){
		Calendar current = Calendar.getInstance();
		current.clear();
		current.setTime(time);
		int i = current.get(Calendar.HOUR);
//		int j = current.get(Calendar.MINUTE);
		//在零时区间内执行
		return i==0?true:false;
	}
	//返回前一天
	public static Date preDate(Date time){
		Calendar current = Calendar.getInstance();
		current.clear();
		current.setTime(time);
		current.add(Calendar.DATE, -1);
		return current.getTime();
	}
	//返回前一天
	public static Date preMonth(Date time){
		Calendar current = Calendar.getInstance();
		current.clear();
		current.setTime(time);
		current.add(Calendar.MONTH, -1);
		return current.getTime();
	}

	public static String getNowString(){
		Calendar calendar = Calendar.getInstance();
		// calendar.setTimeInMillis(adsClickLog.click_time);
//		calendar.set(2011, 11, 31,23,31,00);
		String format = new SimpleDateFormat("yyyyMMddkk")
				.format(calendar.getTime());
		if("24".equals(format.substring(format.length()-2))){
			format = format.substring(0,8)+"00";
		}
		if ( calendar.get(Calendar.MINUTE) < 30)
			format += "00";
		else
			format += 30;
		return getLastThirtyMinute(format);
	}

	public static String toDateString(Date time) {
		return format.format(time);
	}
	public static String toDayDateString(Date time) {
		return format2.format(time);
	}
	public static String toPreDayDateString(Date time) {
		return format2.format(preDate(time));
	}

	public static String toWeekDateString(Date time) {
		return format2.format(time);
	}
	public static String toMonthDateString(Date time) {
		return format3.format(time);
	}
	public static String toPreMonthDateString(Date time) {
		return format3.format(preMonth(time));
	}

	public static void main(String[] args) {
		System.out.println(getNowString());
	}
}
