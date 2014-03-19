package com.travelzen.search.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateUtil {

	public static final Log log = LogFactory.getLog(DateUtil.class);

	public static java.util.Date parseDate(String dateStr, String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			log.error(e, e);
			return null;
		}
	}

	public static String format(java.util.Date date, String format) {
		String result = "";
		try {
			if (date != null) {
				java.text.DateFormat df = new java.text.SimpleDateFormat(format);
				result = df.format(date);
			}
		} catch (Exception e) {
		}
		return result;
	}

	public static Date getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	public static Date addDay(Date d, int offset) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, offset);
		return cal.getTime();
	}

	public static int minusDate(Date date1, Date date2) {
		int result = 0;
		date1 = parseDate(format(date1,"yyyy-MM-dd"),"yyyy-MM-dd");
		date2 = parseDate(format(date2,"yyyy-MM-dd"),"yyyy-MM-dd");
		result = (int) ((date2.getTime()-date1.getTime()) / (1000 * 60 * 60 * 24));
		return result;
	}


	public static final String getCurrentDateStr() {
		Calendar cal = Calendar.getInstance();
		java.text.SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String cdate = sdf.format(cal.getTime());
		return cdate;
	}

	public static void main(String[] args) {
		 System.out.println(minusDate(parseDate("2007-07-11 16:00:18.0",
		 "yyyy-MM-dd HH:mm:ss.S"), parseDate("2007-07-13 7:00:18.0",
		 "yyyy-MM-dd HH:mm:ss.S")));
		/*System.out.println(parseDate("2008-09-01 06:00:18.0",
				"yyyy-MM-dd HH:mm:ss.S").toString());
		Date date = new Date("Mon Sep 01 06:00:18 CST 2008");
		System.out.println(date.toLocaleString());*/
		// System.out.println()
	}
}
