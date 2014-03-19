package com.travelzen.framework.core.time;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

public class DateTimeUtil {

	static final Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);

	// yyyyMMddHHmmssSSS
	public static final int DATETIME_FORMAT_IDX_LONG17 = 17;

	public static String DateTimeFormatter = "yyyyMMdd";
	public final static String SIMPLE_DATE_PATTERN = "yyyyMMdd";
	public final static String DATE_PATTERN = "yyyy-MM-dd";

	public final static String TIME_PATTERN = "HH:mm:ss";

	public final static String DATE_TIME_PATTERN = DATE_PATTERN + " "
			+ TIME_PATTERN;

	private DateTimeUtil() {
	}

	public static DateTime getDate(String sDate) {
		return getDate(sDate, "");
	}

	public static DateTime getJustDate(String sDate) {
		return getDate(sDate, "yyyy-MM-dd");
	}

	public static DateTime getDateWithOutMillisec(DateTime time) {
		if (time == null)
			return new DateTime((new DateTime().getMillis() / 1000) * 1000);
		else
			return new DateTime((time.getMillis() / 1000) * 1000);
	}

	// public static DateTime getJustDate_Begin(String sDate) {
	// Date date = getDate(sDate, "yyyy-MM-dd");
	// Calendar calendar = null;
	// calendar = GregorianCalendar.getInstance();
	// calendar.setTime(date);
	// calendar.set(Calendar.MILLISECOND, 0);
	// calendar.set(Calendar.SECOND, 0);
	// calendar.set(Calendar.MINUTE, 0);
	// calendar.set(Calendar.HOUR_OF_DAY, 0);
	// return calendar.getTime();
	// }

	// public static DateTime getJustDate_End(String sDate) {
	// DateTime date = getDate(sDate, "yyyy-MM-dd");
	// Calendar calendar = null;
	// calendar = GregorianCalendar.getInstance();
	// calendar.setTime(date);
	// calendar.set(Calendar.MILLISECOND, 0);
	// calendar.set(Calendar.SECOND, 0);
	// calendar.set(Calendar.MINUTE, 0);
	// calendar.set(Calendar.HOUR_OF_DAY, 0);
	// calendar.add(Calendar.DATE, 1);
	// return new Date(calendar.getTime().getTime() - 1000L);
	// }

	// public static DateTime getTomDateBegin(DateTime date) {
	//
	// Calendar calendar = null;
	// calendar = GregorianCalendar.getInstance();
	// calendar.setTime(date);
	// calendar.set(Calendar.MILLISECOND, 0);
	// calendar.set(Calendar.SECOND, 0);
	// calendar.set(Calendar.MINUTE, 0);
	// calendar.set(Calendar.HOUR_OF_DAY, 0);
	// calendar.add(Calendar.DATE, 1);
	// return calendar.getTime();
	// }

	// public static DateTime getJustDateBegin(DateTime date) {
	// Calendar calendar = null;
	// calendar = GregorianCalendar.getInstance();
	// calendar.setTime(date);
	// calendar.set(Calendar.MILLISECOND, 0);
	// calendar.set(Calendar.SECOND, 0);
	// calendar.set(Calendar.MINUTE, 0);
	// calendar.set(Calendar.HOUR_OF_DAY, 0);
	// return calendar.getTime();
	// }

	// public static DateTime getJustDateEnd(DateTime date) {
	// Calendar calendar = null;
	// calendar = GregorianCalendar.getInstance();
	// calendar.setTime(date);
	// calendar.set(Calendar.MILLISECOND, 0);
	// calendar.set(Calendar.SECOND, 0);
	// calendar.set(Calendar.MINUTE, 0);
	// calendar.set(Calendar.HOUR_OF_DAY, 0);
	// calendar.add(Calendar.DATE, 1);
	// return new DateTime(calendar.getTime().getTime());
	// }
	//
	// public static Date getTomDateEnd(Date date) {
	// Calendar calendar = null;
	// calendar = GregorianCalendar.getInstance();
	// calendar.setTime(date);
	// calendar.set(Calendar.MILLISECOND, 0);
	// calendar.set(Calendar.SECOND, 0);
	// calendar.set(Calendar.MINUTE, 0);
	// calendar.set(Calendar.HOUR_OF_DAY, 0);
	// calendar.add(Calendar.DATE, 2);
	// return calendar.getTime();
	// }

	// public static Optional<DateTime> getJustDateBegin(String sDate) {
	// Optional<DateTime> date = getDate(sDate, "yyyy-MM-dd");
	// return getJustDateBegin(date.get());
	//
	// }

	public static DateTime getDate(long lDate) {
		return getDate(lDate, "");
	}

	public static DateTime getJustDate(long lDate) {
		return getDate(lDate, "yyyy-MM-dd");
	}

	public static DateTime getDate(long lDate, String sFormat) {
		if (sFormat == null || "".equals(sFormat)) {
			sFormat = "yyyy-MM-dd HH:mm:ss";
		}

		return getDate(formatDate(new DateTime(lDate), sFormat), sFormat);
	}

	public static DateTime getDate(String sDate, String sFormat, Locale locale) {
		DateTime dValue;

		if (sFormat == null || "".equals(sFormat)) {
			sFormat = "yyyy-MM-dd HH:mm:ss";
		}

		DateTimeFormatter dateTimeFormatter = DateTimeFormat
				.forPattern(sFormat).withLocale(locale);

		dValue = dateTimeFormatter.parseDateTime(sDate);

		return dValue;

	}

	public static DateTime getDate(String sDate, String sFormat) {
		DateTime dValue;

		if (sFormat == null || "".equals(sFormat)) {
			sFormat = "yyyy-MM-dd HH:mm:ss";
		}

		DateTimeFormatter dateTimeFormatter = DateTimeFormat
				.forPattern(sFormat);

		dValue = dateTimeFormatter.parseDateTime(sDate);

		return dValue;

	}

	public static String formatDate(long lDate, DateTimeFormatter format) {
		return format.print(new DateTime(lDate));
	}

	public static String formatDate(long sDate, String sFormat) {
		return formatDate(new DateTime(sDate), sFormat);
	}

	public static String formatDate(Date lDate) {
		return formatDate(new DateTime(lDate), "");
	}

	public static String formatDate(long lDate) {
		return formatDate(new DateTime(lDate), "");
	}

	public static String formatJustDate(long lDate) {
		return formatDate(new DateTime(lDate), "yyyy-MM-dd");
	}

	public static String formatDate(DateTime date) {
		return formatDate(date, DateTimeZone.getDefault());
	}

	public static String formatDate(DateTime date, String sFormat) {
		if (StringUtils.isBlank(sFormat))
			sFormat = "yyyy-MM-dd HH:mm:ss";
		return formatDate(date, DateTimeZone.getDefault(), Locale.getDefault(),
				sFormat);
	}

	public static String formatDate(DateTime date, DateTimeZone DateTimeZone) {
		return formatDate(date, DateTimeZone, Locale.getDefault(), "yyyy-MM-dd");
	}

	public static String formatDate(DateTime date, Locale locale, String sFormat) {

		return formatDate(date, DateTimeZone.getDefault(), locale, sFormat);

	}

	public static String formatDate(DateTime date, DateTimeZone dateTimeZone,
			Locale locale, String sFormat) {

		DateTimeFormatter dateFormat = DateTimeFormat.forPattern(sFormat);

		return dateFormat.withLocale(locale).withZone(dateTimeZone).print(date);

	}

	public static String simplifyDate(String date) {

		DateTime d = getDate(date, "yyyy-MM-dd HH:mm:ss");
		return formatDate(d, "yy-MM-dd HH:mm");

	}

	public static String getTodayStr() {

		DateTimeFormatter dateTimeFormatter = DateTimeFormat
				.forPattern("yyyy-MM-dd");

		DateTime todate = new DateTime(System.currentTimeMillis());
		String today = dateTimeFormatter.print(todate);
		return today;
	}

	public static final int TIMESTAMPTYPE_UNIX = 2;

	/**
	 * 将日期对象转为指定格式日期字符串
	 * 
	 * @param date
	 * @param intFormat
	 * @return
	 */
	public static String format(DateTime date, String format) {
		DateTimeFormatter sdf = DateTimeFormat.forPattern(format);
		return sdf.print(date);
	}

	public static String format(Date date, String format) {
		return format(new DateTime(date), format);
	}

	/**
	 * 将日期对象转为指定格式日期字符串
	 * 
	 * @param date
	 * @param intFormat
	 * @return
	 */
	public static String format(DateTime date, int intFormat) {
		DateTimeFormatter sdf = findFormat(intFormat);
		return sdf.print(date);
	}

	/**
	 * 根据参数获得简单日期格式对象
	 * 
	 * @param intFormat
	 * @return
	 */
	public static DateTimeFormatter findFormat(int intFormat) {
		String strFormat = "yyyy'??'MM'??'dd'??' H:mm:ss.S";
		switch (intFormat) {
		case 0: // '\0'
			strFormat = "yyyy'??'MM'??'dd'??' H:mm:ss.S";
			break;

		case 1: // '\001'
			strFormat = "yyyy'-'MM'-'dd H:mm:ss.S";
			break;

		case 2: // '\002'
			strFormat = "yyyy'??'MM'??'dd'??'";
			break;

		case 3: // '\003'
			strFormat = "yyyy'-'MM'-'dd";
			break;

		case 4: // '\004'
			strFormat = "H:mm:ss";
			break;

		case 5: // '\005'
			strFormat = "K:mm:ss a";
			break;

		case 6: // '\006'
			strFormat = "yyyy'??'MM'??'dd'??' H:mm:ss";
			break;

		case 7: // '\007'
			strFormat = "yyyy'??'MM'??'dd'??' K:mm:ss a";
			break;

		case 8: // '\b'
			strFormat = "yyyy-MM-dd H:mm:ss";
			break;

		case 9: // '\t'
			strFormat = "yyyy-MM-dd K:mm:ss a";
			break;

		case 10: // '\n'
			strFormat = "H:mm:ss.S";
			break;

		case 11: // '\013'
			strFormat = "K:mm:ss.S a";
			break;

		case 12: // '\f'
			strFormat = "H:mm";
			break;

		case 13: // '\r'
			strFormat = "K:mm a";
			break;

		case 14: // '\r'
			strFormat = "yyyy-MM-dd H:mm";
			break;

		case 15: // '\r'
			strFormat = "yyyyMMddHHmmssS";
			break;
		case 16: // '\r'
			strFormat = "yyyyMMdd";
			break;

		case 17: // '\r'
			strFormat = "yyyyMMddHHmmssSSS";
			break;

		default:
			strFormat = "yyyy'??'MM'??'dd'??' H:mm:ss.S";
			break;
		}
		return DateTimeFormat.forPattern(strFormat);
	}

	public static String formatDateStr(String dateStr, int newtimestampType,
			int oldtimestampType) {
		DateTime oldDate = findFormat(oldtimestampType).parseDateTime(dateStr);
		return format(oldDate, newtimestampType);
	}

	/**
	 * 判断字符日期是否合法，根据给定时间戳格式
	 * 
	 * @param strDate
	 * @param timestampType
	 * @return
	 */
	public static boolean isValdateDate(String strDate, int timestampType) {

		DateTimeFormatter sdf = findFormat(timestampType);

		try {
			sdf.parseDateTime(strDate);
		} catch (Exception e) {
			return false;
		}
		return true;

		//
		// sdf.setLenient(false);
		// if (strDate != null && strDate.length() > 0) {
		// try {
		// Date dtCheck = (Date) (sdf.parseDateTime(strDate));
		// String strCheck = sdf.format(dtCheck);
		// if (strDate.equals(strCheck)) {
		// isPassed = true;
		// } else {
		// isPassed = false;
		// }
		// } catch (Exception e) {
		// isPassed = false;
		// }
		// }
		// return isPassed;
	}

	// public static List<String> getDateList(String startTime, String endTime)
	// throws Exception {
	// DateTimeFormatter sdf = new DateTimeFormatter("yyyy-MM-dd HH:mm:ss");
	// List<String> dateInfos = new ArrayList<String>();
	// Date start = null;
	// Date end = null;
	// startTime = startTime + " 00:00:00";
	// endTime = endTime + " 23:59:59";
	// try {
	// start = sdf.parseDateTime(startTime);
	// end = sdf.parseDateTime(endTime);
	// } catch (Exception e) {
	// throw new Exception("Parse date exception !");
	// }
	//
	// if (start.after(end)) {
	// throw new Exception("Query time error.Start time after end time.");
	// }
	//
	// Calendar calStart = Calendar.getInstance();
	// calStart.setTime(start);
	//
	// Calendar calEnd = Calendar.getInstance();
	// calEnd.setTime(end);
	//
	// sdf = new DateTimeFormatter("yyyy-MM-dd");
	// if (calStart.after(calEnd)) {
	// dateInfos.add(startTime);
	// } else {
	// while (!calStart.after(calEnd)) {
	// dateInfos.add(sdf.format(calStart.getTime()));
	// calStart.add(Calendar.DAY_OF_MONTH, 1);
	// }
	// }
	// return dateInfos;
	// }

	/**
	 * 开始日期(include)和结束日期(include)多少分钟
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	public static long findDateSpaceInMinuties(String startTime, String endTime) {

		DateTimeFormatter sdf = DateTimeFormat.forPattern("yyyy-MM-dd H:mm:ss");

		DateTime startDate = sdf.parseDateTime(startTime + " 00:00:00");

		DateTime endDate = sdf.parseDateTime(endTime + " 23:59:59");

		double minutes = (endDate.getMillis() - startDate.getMillis())
				/ (1000 * 60);

		return (long) Math.ceil(minutes);
	}

	/**
	 * 通过起始时间和结束时间，将当前时间范围分割为参数（divisor）代表的份数，
	 * 并将每一份转化为对应的日期封装到集合中返回。注：divisor必须为偶数， 返回的日期格式为标准格式，yyyy-MM-dd H:mm:ss
	 * 
	 * @param startTime
	 * @param endTime
	 * @param divisor
	 * @return
	 * @throws ParseException
	 */
	// public static List<String> findDateScopeStandardFormat(String startTime,
	// String endTime, int divisor, String strFormat)
	// throws ParseException {
	// List<String> returnValue = null;
	// if (startTime == null || startTime.length() <= 0) {
	// return returnValue;
	// }
	// if (endTime == null || endTime.length() <= 0) {
	// return returnValue;
	// }
	// if (divisor == 0 || divisor % 2 != 0) {
	// return returnValue;
	// }
	// returnValue = new ArrayList<String>();
	// DateTimeFormatter sdf = new DateTimeFormatter(strFormat);
	//
	// Date startDate = sdf.parseDateTime(startTime + " 00:00:00");
	// Date endDate = sdf.parseDateTime(endTime + " 23:59:59");
	//
	// Calendar calStartTime = Calendar.getInstance();
	// calStartTime.setTime(startDate);
	//
	// Calendar calEndTime = Calendar.getInstance();
	// calEndTime.setTime(endDate);
	//
	// int minuties = (int) findDateSpaceInMinuties(startTime, endTime)
	// / divisor;
	//
	// while (!calStartTime.after(calEndTime)) {
	// returnValue.add(sdf.format(calStartTime.getTime()));
	// calStartTime.add(Calendar.MINUTE, minuties);
	// }
	// return returnValue;
	// }

	// public static long getDateTime(String date, String format) {
	// DateTimeFormatter sdf = new DateTimeFormatter(format);
	// try {
	// return sdf.parseDateTime(date).getTime() / 1000;
	// } catch (ParseException e) {
	// e.printStackTrace();
	// return 0;
	// }
	// }

	/**
	 * 从日期对象得到该日期零点的时间戳
	 * 
	 * @param date
	 * @param timestampType
	 * @return
	 */
	public static long getZeroTimeStampOfDay(DateTime date, int timestampType) {

		MutableDateTime zerotimeOfDay = date.toMutableDateTime();
		zerotimeOfDay.setTime(0, 0, 0, 0);
		return zerotimeOfDay.getMillis();

	}

	/**
	 * 获取日期对应的时间
	 * 
	 * @param date
	 * @param timestampType
	 * @return
	 */
	public static long getDateTimestamp(DateTime date, int timestampType) {
		long returnValue = -1;
		try {
			if (date == null) {
				return -1;
			}
			returnValue = date.getMillis();

			if (timestampType == TIMESTAMPTYPE_UNIX) {
				returnValue = returnValue / 1000;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	public static final DateTime convertStringToDate(String pattern,
			Locale locale, DateTimeZone zone, String strDate) {
		if (locale == null)
			locale = Locale.getDefault();
		if (zone == null)
			zone = DateTimeZone.getDefault();

		DateTimeFormatter df = DateTimeFormat.forPattern(pattern)
				.withLocale(locale).withZone(zone);

		return df.parseDateTime(strDate);

	}

	public static final DateTime convertStringToDate(String strDate) {
		Locale locale = Locale.CHINESE;
		try {
			return convertStringToDate(DATE_PATTERN, locale, null, strDate);
		} catch (Exception e) {
			return null;
		}
	}

	public static final DateTime convertStringToDate(String strDate,
			String sytle) {
		Locale locale = Locale.CHINESE;
		try {
			return convertStringToDate(sytle, locale, null, strDate);
		} catch (Exception e) {
			return null;
		}
	}

	public static final String convertDateToString(String pattern,
			Locale locale, DateTimeZone zone, DateTime aDate) {
		if (locale == null)
			locale = Locale.getDefault();
		if (zone == null)
			zone = DateTimeZone.getDefault();

		DateTimeFormatter df = DateTimeFormat.forPattern(pattern)
				.withLocale(locale).withZone(zone);

		return df.print(aDate);
	}

	public static final String convertDateToString(String pattern,
			DateTime aDate) {
		Locale locale = Locale.CHINESE;
		return convertDateToString(pattern, locale, null, aDate);
	}

	public static DateTime getBeginDateTime(Date date) {
		DateTime datetime = new DateTime(date);
		MutableDateTime mutabelDatetime = datetime.toMutableDateTime();
		mutabelDatetime.setMillisOfDay(0);
		return mutabelDatetime.toDateTime();
	}

	public static DateTime getBeginDateTime(DateTime date) {
		return getBeginDateTime(date.toDate());
	}

	/**
	 * 提供yyyy-MM-dd类型的日期字符串转化
	 */
	public static final DateTime getBeginDate(String beginDate) {
		Locale locale = Locale.CHINESE;
		try {
			return convertStringToDate("yyyy-MM-dd HH:mm:ss", locale, null,
					beginDate + " 00:00:00");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 提供yyyy-MM-dd类型的日期字符串转化 专门提供Web页面结束日期转化 如输入2006-07-27，则转化为2006-07-28
	 * 00:00:00
	 */
	public static final DateTime getEndDate(String endDate) {
		Locale locale = Locale.CHINESE;
		try {
			DateTime date = convertStringToDate("yyyy-MM-dd HH:mm:ss", locale,
					null, endDate + " 00:00:00");
			return new DateTime(date.getMillis() + 24 * 3600 * 1000);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * yyyy年mm月dd日 星期w
	 */
	public static String getFullDateStr() {

		DateTime now = new DateTime();

		return DateTimeFormat.fullDateTime().withLocale(Locale.CHINESE)
				.print(now);

		// now.pr
		//
		// DateFormat format = DateFormat.getDateInstance(DateFormat.FULL,
		// Locale.CHINESE);
		// return format.format(new DateTime());
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */

	public static int diffdates(DateTime date1, DateTime date2) {

		int days = Days.daysBetween(getStartDateTime(date1, null),
				new DateTime(date2)).getDays();

		return days;
	}

	/**
	 * 前一天的24：00 往前1秒
	 * 
	 * @return
	 */
	public static DateTime getYestardayEnd() {

		MutableDateTime dt = new MutableDateTime();

		// dt.addDays(-1);
		dt.setMillisOfDay(0);
		dt.addMillis(-1000);

		return dt.toDateTime();

	}

	// public static int getDays(GregorianCalendar g1, GregorianCalendar g2) {
	// int elapsed = 0;
	// GregorianCalendar gc1, gc2;
	//
	// if (g2.after(g1)) {
	// gc2 = (GregorianCalendar) g2.clone();
	// gc1 = (GregorianCalendar) g1.clone();
	// } else {
	// gc2 = (GregorianCalendar) g1.clone();
	// gc1 = (GregorianCalendar) g2.clone();
	// }
	//
	// gc1.clear(Calendar.MILLISECOND);
	// gc1.clear(Calendar.SECOND);
	// gc1.clear(Calendar.MINUTE);
	// gc1.clear(Calendar.HOUR_OF_DAY);
	//
	// gc2.clear(Calendar.MILLISECOND);
	// gc2.clear(Calendar.SECOND);
	// gc2.clear(Calendar.MINUTE);
	// gc2.clear(Calendar.HOUR_OF_DAY);
	//
	// while (gc1.before(gc2)) {
	// gc1.add(Calendar.DATE, 1);
	// elapsed++;
	// }
	// return elapsed;
	// }

	// /**
	// * 功能：将表示时间的字符串以给定的样式转化为java.util.Date类型
	// * 且多于或少于给定的时间多少小时（formatStyle和formatStr样式相同）
	// *
	// * @param:formatStyle 要格式化的样式,如:yyyy-MM-dd HH:mm:ss
	// * @param:formatStr 待转化的字符串(表示的是时间)
	// * @param:hour 多于或少于的小时数(可正可负) 单位为小时
	// * @return java.util.Date
	// */
	// public static DateTime formartDate(String formatStyle, String formatStr,
	// int hour) {
	// DateTimeFormatter format = new DateTimeFormatter(formatStyle,
	// Locale.CHINA);
	// try {
	// Date date = new Date();
	// date.setTime(format.parseDateTime(formatStr).getTime() + hour * 60
	// * 60 * 1000);
	// return date;
	// } catch (Exception e) {
	// System.out.println(e.getMessage());
	// return null;
	// }
	// }

	// // 增加一个时间点的小时和分钟数后返回时间类型
	// public static DateTime getProperDate(DateTime time_start, int hour, int
	// minute)
	// {
	//
	// DateTimeFormatter formatter = new DateTimeFormatter(
	// "yyyy-MM-dd HH:mm:ss");
	// Date properTime = formatter.parseDateTime(formatDate(time_start,
	// "yyyy-MM-dd HH:mm:ss"));
	// properTime.setTime(properTime.getTime() + hour * 60 * 60 * 1000
	// + minute * 60 * 1000);
	//
	// return getDate(formatter.format(properTime), "yyyy-MM-dd HH:mm:ss");
	//
	// }

	// /**
	// * 获取现在时刻
	// */
	// public static Date getNow() {
	// return new Date(new Date().getTime());
	// }
	//
	// /**
	// * 获取前一小时
	// */
	// public static Date getPreHour() {
	// return new Date(new Date().getTime() - 3600 * 1000L);
	// }
	//
	// /**
	// * 获取下一小时
	// */
	// public static Date getNextHour() {
	// return new Date(new Date().getTime() + 3600 * 1000L);
	// }
	//
	// /**
	// * 获取昨天
	// */
	// public static Date getYesterday() {
	// return new Date(new Date().getTime() - 24 * 3600 * 1000L);
	// }
	//
	// /**
	// * 获取昨天
	// */
	// public static Date getYesterdayDate(Date day) {
	// return new Date(day.getTime() - 24 * 3600 * 1000L);
	// }
	//
	// /**
	// * 获取明天
	// */
	// public static Date getTomorrowDate(Date day) {
	// return new Date(day.getTime() + 24 * 3600 * 1000L);
	// }
	//
	// /**
	// * 获取上周
	// */
	// public static Date getLastWeek(Date day) {
	// return new Date(day.getTime() - 7 * 24 * 3600 * 1000L);
	// }
	//
	// /**
	// * 获取下周
	// */
	// public static Date getNextWeek(Date day) {
	// return new Date(day.getTime() + 7 * 24 * 3600 * 1000L);
	// }

	// /**
	// * 获取上个月
	// */
	// public static Date getLastMonth() {
	// return getLastMonth(new Date());
	// }

	// /**
	// * 获得指定时间的某月的第一天
	// *
	// * @param date
	// * @return
	// *
	// */
	// public static Date getMonthFirstDay(Date date) {
	// int[] dateArr = getDateArray(date);
	// String year = String.valueOf(dateArr[0]);
	// String month = String.valueOf(dateArr[1]);
	// month = month.length() == 1 ? "0" + month : month;
	// Date retDate = convertStringToDate(year + month + "01", "yyyyMMdd");
	// return retDate;
	// }

	// /**
	// * 获得指定时间的某月的最后一天
	// *
	// * @param date
	// * @return
	// *
	// */
	// public static Date getMonthLastDay(Date date) {
	// int[] dateArr = getDateArray(date);
	// int year = dateArr[0];
	// int month = dateArr[1];
	// int maxDayOfMonth = getMaxDayOfMonth(year, month);
	// String monStr = String.valueOf(month);
	// monStr = monStr.length() == 1 ? "0" + monStr : monStr;
	// Date retDate = convertStringToDate(
	// String.valueOf(year) + String.valueOf(monStr)
	// + String.valueOf(maxDayOfMonth), "yyyyMMdd");
	// return retDate;
	// }

	// /**
	// * 获取制定时间的上个月
	// */
	// public static Date getLastMonth(Date date) {
	// Calendar cal = Calendar.getInstance(DateTimeZone.getDefault(),
	// Locale.CHINA);
	// cal.clear();
	// cal.setTime(date);
	// cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
	// cal.getTime();
	// return cal.getTime();
	// }

	// /**
	// * 获取制定时间的下个月
	// */
	// public static Date getNextMonth(Date date) {
	// Calendar cal = Calendar.getInstance(DateTimeZone.getDefault(),
	// Locale.CHINA);
	// cal.clear();
	// cal.setTime(date);
	// cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
	// cal.getTime();
	// return cal.getTime();
	// }
	//
	// /**
	// * 获取指定年和月中该月的最大天数
	// *
	// * @param year
	// * 指定年
	// * @param month
	// * 指定月 1-12
	// * @return 该月最大天数
	// */
	// public static int getMaxDayOfMonth(int year, int month) {
	// Calendar cal = Calendar.getInstance(DateTimeZone.getDefault(),
	// Locale.CHINA);
	// cal.clear();
	// cal.set(year, month - 1, 1);
	// return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	// }

	// /**
	// * 根据指定的年份和指定的第多少周序号得到该周的第一天和最后一天日期
	// *
	// * @param year
	// * 指定的年份,如2006
	// * @param weekNo
	// * 指定年份中的第多少周,如37
	// * @return 该周的起始日期后该周的结束日期<br>
	// * Date[0] 起始日期<br>
	// * Date[1] 结束日期
	// */
	// public static Date[] getGivenWeekDates(int year, int weekNo) {
	// Calendar cal = Calendar.getInstance(DateTimeZone.getDefault(),
	// Locale.CHINA);
	// cal.clear();
	// cal.set(Calendar.YEAR, year);
	// cal.set(Calendar.WEEK_OF_YEAR, weekNo);
	// Date begin = cal.getTime();
	// cal.add(Calendar.DAY_OF_YEAR, 6);
	// Date end = cal.getTime();
	// return new Date[] { begin, end };
	// }

	// /**
	// * 根据指定日期获取其在一年中的第多少周
	// *
	// * @param date
	// * 指定的日期,为null默认为当时日期
	// * @return 当年的第多少周序号,如37
	// */
	// public static int getWeekNo(Date date) {
	// if (date == null)
	// date = new Date();
	// Calendar cal = Calendar.getInstance(DateTimeZone.getDefault(),
	// Locale.CHINA);
	// cal.clear();
	// cal.setTime(date);
	// return cal.get(Calendar.WEEK_OF_YEAR);
	// }

	// /**
	// * 获取制定时间的年份
	// *
	// * @param date
	// * 制定时间
	// * @return 年份
	// */
	// public static int getYear(Date date) {
	// if (date == null)
	// date = new Date();
	// Calendar cal = Calendar.getInstance(DateTimeZone.getDefault(),
	// Locale.CHINA);
	// cal.clear();
	// cal.setTime(date);
	// return cal.get(Calendar.YEAR);
	// }

	// /**
	// * 格式化日期
	// *
	// * @param date
	// * 被格式化的日期
	// * @param style
	// * 显示的样式，如yyyyMMdd
	// */
	// public static String fmtDate(Date date, String style) {
	// DateTimeFormatter dateFormat = new DateTimeFormatter(style);
	// return dateFormat.format(date);
	// }

	//
	// /**
	// * 得到当前日期
	// *
	// * @return int[] int[0] 年 int[1] 月 int[2] 日 int[3] 时 int[4] 分 int[5] 秒
	// */
	// public static int[] getCurrentDate() {
	// Calendar cal = Calendar.getInstance(DateTimeZone.getDefault(),
	// Locale.CHINA);
	// cal.setTime(new Date());
	// int[] date = new int[6];
	// date[0] = cal.get(Calendar.YEAR);
	// date[1] = cal.get(Calendar.MONTH) + 1;
	// date[2] = cal.get(Calendar.DATE);
	// date[3] = cal.get(Calendar.HOUR_OF_DAY);
	// date[4] = cal.get(Calendar.MINUTE);
	// date[5] = cal.get(Calendar.SECOND);
	// return date;
	// }

	// /**
	// * 得到指定日期
	// *
	// * @return int[] int[0] 年 int[1] 月 int[2] 日 int[3] 时 int[4] 分 int[5] 秒
	// *
	// */
	// public static int[] getDateArray(Date date) {
	// Calendar cal = Calendar.getInstance(DateTimeZone.getDefault(),
	// Locale.CHINA);
	// cal.setTime(date);
	// int[] dateArr = new int[6];
	// dateArr[0] = cal.get(Calendar.YEAR);
	// dateArr[1] = cal.get(Calendar.MONTH) + 1;
	// dateArr[2] = cal.get(Calendar.DATE);
	// dateArr[3] = cal.get(Calendar.HOUR_OF_DAY);
	// dateArr[4] = cal.get(Calendar.MINUTE);
	// dateArr[5] = cal.get(Calendar.SECOND);
	// return dateArr;
	// }

	// /**
	// * 设置制定的年份和月份，再得到该日期的前多少月或后多少月的日期年份和月份
	// *
	// * @param year
	// * 指定的年份，如 2006
	// * @param month
	// * 制定的月份，如 6
	// * @param monthSect
	// * 月份的差值 如：现在为2006年5月份，要得到后4月，则monthSect = 4，正确日期结果为2006年9月
	// * 如：现在为2006年5月份，要得到前4月，则monthSect = -4，正确日期结果为2006年1月
	// * 如：monthSect = 0，则表示为year年month月
	// * @return int[] int[0] 年份 int[1] 月份
	// */
	// public static int[] getLimitMonthDate(int year, int month, int monthSect)
	// {
	// year = year < 1 ? 1 : year;
	// month = month > 12 ? 12 : month;
	// month = month < 1 ? 1 : month;
	// Calendar cal = Calendar.getInstance(DateTimeZone.getDefault(),
	// new Locale("zh", "CN"));
	// cal.set(Calendar.YEAR, year);
	// cal.set(Calendar.MONTH, month);
	// cal.add(Calendar.MONTH, monthSect);
	// int[] yAndM = new int[2];
	// yAndM[0] = cal.get(Calendar.YEAR);
	// yAndM[1] = cal.get(Calendar.MONTH);
	// if (yAndM[1] == 0) {
	// yAndM[0] = yAndM[0] - 1;
	// yAndM[1] = 12;
	// }
	// return yAndM;
	// }

	/**
	 * 获取Next天
	 */
	public static String addDays(String date, int amount) {

		DateTimeFormatter frm = DateTimeFormat
				.forPattern("yyyy-MM-dd HH:mm:ss");

		DateTime dt = frm.parseDateTime(date).plusDays(amount);

		return frm.print(dt);

		// DateTimeFormatter frm = new DateTimeFormatter("yyyy-MM-dd HH:mm:ss");
		// Calendar calendar = Calendar.getInstance();
		// try {
		// if (date instanceof String) {
		// calendar.setTime(frm.parseDateTime(date.toString()));
		// } else if (date instanceof Date) {
		// calendar.setTime((Date) date);
		// }
		// calendar.add(Calendar.DATE, amount);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// return frm.format(calendar.getTime());
	}

	/**
	 * 获取Next MINUTE
	 */
	public static String addMinutes(String date, int amount) {

		DateTimeFormatter frm = DateTimeFormat
				.forPattern("yyyy-MM-dd HH:mm:ss");

		DateTime dt = frm.parseDateTime(date).plusMinutes(amount);

		return frm.print(dt);

		// Calendar calendar = Calendar.getInstance();
		// try {
		// if (date instanceof String) {
		// calendar.setTime(frm.parseDateTime(date.toString()));
		// } else if (date instanceof Date) {
		// calendar.setTime((Date) date);
		// }
		// calendar.add(Calendar.MINUTE, amount);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// return frm.format(calendar.getTime());
	}

	/**
	 * 将字符串20080808 转换成 2008-08-08
	 */
	public static String getDateForm(String date) {
		if (StringUtils.isBlank(date) || date.length() != 8) {
			return "0000-00-00";
		}
		return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
				+ date.substring(6, 8);
	}

	/***
	 * deduce is the same day
	 * 
	 * @return
	 */
	public static boolean isSameDay(DateTime atime, DateTime nowDate) {

		return atime.getDayOfYear() == nowDate.getDayOfYear()
				&& atime.getYear() == nowDate.getYear();

		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(atime);
		// calendar.set(Calendar.MILLISECOND, 0);
		// calendar.set(Calendar.SECOND, 0);
		// calendar.set(Calendar.MINUTE, 0);
		// calendar.set(Calendar.HOUR_OF_DAY, 0);
		//
		// DateTime begin = new DateTime(calendar.getTime());
		// calendar.add(Calendar.DATE, 1);
		// DateTime end = new DateTime(calendar.getTime());
		//
		// if (!begin.isAfter(nowDate.getTime()) &&
		// end.isAfter(nowDate.getTime()))
		// return true;
		//
		// return false;
	}

	public static boolean isSameDay(Date atime, Date nowDate) {
		if (atime == null || nowDate == null) {
			return false;
		}
		return isSameDay(new DateTime(atime), new DateTime(nowDate));
	}

	/**
	 * test whether <b>date1</b> is after <b>date2</b><br/>
	 * return true if <b>date1</b> is after <b>date2</b>(compare in days,
	 * ignoring hours, minutes and seconds), otherwise return false
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean dayAfter(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return false;
		}
		return truncate(date1, Calendar.DAY_OF_MONTH).after(
				truncate(date2, Calendar.DAY_OF_MONTH));
	}

	/**
	 * return true if <b>date1</b> is before <b>date2</b>(compare in days,
	 * ignoring hours, minutes and seconds), otherwise return false
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean dayBefore(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return false;
		}
		return truncate(date1, Calendar.DAY_OF_MONTH).before(
				truncate(date2, Calendar.DAY_OF_MONTH));
	}

	/**
	 * 截断日期到指定的单位<br/>
	 * if <b>unit = Calendar.DAY_OF_YEAR</b> 则<b>date</b>的时分秒都会被设置成0<br/>
	 * if <b>unit = Calendar.MONTH</b> 则<b>date</b>的day被设置成１,时分秒设置成0<br/>
	 * <b>NOTICE</b>:January is 0, not 1
	 * 
	 * @param date
	 * @param unit
	 * @return
	 */
	public static Date truncate(Date date, int unit) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (unit == Calendar.YEAR) {
			c.set(c.get(Calendar.YEAR), 0, 1, 0, 0, 0);
		} else if (unit == Calendar.MONTH) {
			c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1, 0, 0, 0);
		} else if (unit == Calendar.DATE) {
			c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
					c.get(Calendar.DATE), 0, 0, 0);
		} else if (unit == Calendar.DAY_OF_YEAR) {
			c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR));
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
		} else if (unit == Calendar.HOUR_OF_DAY) {
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
		} else if (unit == Calendar.MINUTE) {
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
		} else {
			throw new UnsupportedOperationException("unsupport unit: " + unit);
		}
		return c.getTime();
	}

	public static long getTimeMillisToAfterDaysHour(int days, int hourOfTomorrow) {

		DateTime d = new DateTime();
		d.plusDays(days);
		d.plusHours(hourOfTomorrow);

		return d.getMillis() - (new DateTime()).getMillis();

		// if (0 == hourOfTomorrow)
		// hourOfTomorrow = 2;
		//
		// Calendar calendar = Calendar.getInstance();
		//
		// int yearOfToday = calendar.get(Calendar.YEAR);
		// int monthOfToday = calendar.get(Calendar.MONTH) + 1;
		// int dayOfToday = calendar.get(Calendar.DAY_OF_MONTH);
		//
		// calendar.set(Calendar.DAY_OF_MONTH, dayOfToday + days);
		// if (31 == dayOfToday && days >= 1) {
		// calendar.set(Calendar.MONTH, monthOfToday + 1);
		// }
		// if (12 == monthOfToday && 31 == dayOfToday && days >= 1) {
		// calendar.set(Calendar.YEAR, yearOfToday + 1);
		// }
		//
		// int dayOfTomorrow = calendar.get(Calendar.DAY_OF_MONTH);
		// int monthOfTomorrow = calendar.get(Calendar.MONTH);
		// int yearOfTomorrow = calendar.get(Calendar.YEAR);
		//
		// Calendar calendarOfTomorrow = new GregorianCalendar(yearOfTomorrow,
		// monthOfTomorrow, dayOfTomorrow, hourOfTomorrow, 0, 0);
		// long startTimeMillis = System.currentTimeMillis();
		//
		// long timeMillisToAfterDaysHour =
		// calendarOfTomorrow.getTime().getTime()
		// - startTimeMillis;
		//
		// if (0 > timeMillisToAfterDaysHour)
		// throw new Exception("时间差为负数，可能设置有误");
		//
		// return timeMillisToAfterDaysHour;
	}

	public static void sleep(long millisecond) {
		try {
			Thread.sleep(millisecond);
		} catch (InterruptedException localInterruptedException) {
		}
	}

	public static void SleepSec(int sec) {
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String getGMTTimeString(long milliSeconds) {
		DateTimeFormatter sdf = DateTimeFormat
				.forPattern("E, d MMM yyyy HH:mm:ss 'GMT'");
		return sdf.print(milliSeconds);
	}

	public static String date(String fmt) {
		return DateTimeFormat.forPattern(fmt).print(new DateTime());
	}

	public static String date(String fmt, long t) {
		return DateTimeFormat.forPattern(fmt).print(t);
	}

	public static String date(String fmt, DateTime date) {
		return DateTimeFormat.forPattern(fmt).print(date);
	}

	public static String date8() {
		return DateTimeFormat.forPattern("yyyyMMdd").print(new DateTime());
	}

	public static String date8(DateTime date) {
		return DateTimeFormat.forPattern("yyyyMMdd").print(date);

	}

	public static String date10(DateTime date) {
		return DateTimeFormat.forPattern("yyyy-MM-dd").print(date);

	}

	public static String date10() {

		return DateTimeFormat.forPattern("yyyy-MM-dd").print(new DateTime());
	}

	public static String date10slash(DateTime date) {
		return DateTimeFormat.forPattern("MM/dd/yyyy").print(new DateTime());

	}

	public static String time5() {

		return DateTimeFormat.forPattern("HH:mm").print(new DateTime());
	}

	public static String time5(DateTime time) {

		return DateTimeFormat.forPattern("HH:mm").print(time);
	}

	public static String time6() {

		return DateTimeFormat.forPattern("HHmmss").print(new DateTime());
	}

	public static String time8(DateTime date) {
		return DateTimeFormat.forPattern("HH:mm:ss").print(new DateTime());

	}

	public static String time6(DateTime date) {

		return DateTimeFormat.forPattern("HHmmss").print(new DateTime());

	}

	public static String datetime14() {

		return DateTimeFormat.forPattern("yyyyMMddHHmmss")
				.print(new DateTime());

	}

	public static String datetime12() {
		return DateTimeFormat.forPattern("yyMMddHHmmss").print(new DateTime());
	}

	public static String datetime14Readable() {
		return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").print(
				new DateTime());
	}

	public static String datetime14(DateTime date) {

		return DateTimeFormat.forPattern("yyyyMMddHHmmss").print(date);

	}

	public static String datetime14(Date date) {

		return datetime14(new DateTime(date));

	}

	public static String datetime14(long t) {

		return DateTimeFormat.forPattern("yyyyMMddHHmmss").print(t);

	}

	public static DateTime addMin(DateTime now, int amount) {

		return now.plusMinutes(amount);

	}

	public static DateTime addMin(Date now, int amount) {

		return addMin(new DateTime(now), amount);

	}

	public static DateTime addHour(DateTime now, int amount) {

		return now.plusHours(amount);

	}

	public static DateTime addHour(Date now, int amount) {

		return addHour(new DateTime(now), amount);

	}

	public static DateTime addSec(DateTime now, int amount) {
		return now.plusSeconds(amount);
	}

	public static DateTime addDay(DateTime now, int amount) {
		return now.plusDays(amount);
	}

	public static DateTime addDay(Date now, int amount) {
		return addDay(new DateTime(now), amount);
	}

	public static DateTime addMonth(DateTime now, int amount) {

		return now.plusMonths(amount);

	}

	public static DateTime addYear(DateTime now, int amount) {

		return now.plusYears(amount);

	}

	public static DateTime parseDate(String format, String date, Locale locale) {

		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format)
				.withLocale(locale);

		return dateTimeFormatter.parseDateTime(date);

	}

	public static DateTime parseDate8(String date) {

		DateTimeFormatter dateTimeFormatter = DateTimeFormat
				.forPattern("yyyyMMdd");

		return dateTimeFormatter.parseDateTime(date);

	}

	public static DateTime parseDate10(String date) {
		DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");

		return format.parseDateTime(date);
	}

	public static boolean validateDate8(String date) {
		DateTime d = parseDate8(date);
		return (d != null) && (date8(d).equals(date));
	}

	public static boolean validateDate10(String date) {
		DateTime d = parseDate10(date);
		return (d != null) && (date10(d).equals(date));
	}

	public static DateTime parseDatetime14(String datetime) {
		DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMddHHmmss");
		return format.parseDateTime(datetime);

	}

	public static DateTime parseTime8(String datetime) {

		DateTimeFormatter dateTimeFormatter = DateTimeFormat
				.forPattern("HH:mm:ss");

		return dateTimeFormatter.parseDateTime(datetime);

	}

	public static Optional<DateTime> parseTime8toDateTime(String datetime) {

		DateTimeFormatter format = DateTimeFormat.forPattern("HH:mm:ss");
		// format.setLenient(false);
		DateTime time8 = format.parseDateTime(datetime);

		// DateTime time8DateTime = new DateTime(time8);
		return Optional.of(time8);

	}

	public static DateTime parseDatetime6(String datetime) {
		DateTimeFormatter format = DateTimeFormat.forPattern("HHmmss");
		return format.parseDateTime(datetime);

	}

	public static DateTime parseTime6(String time) {
		DateTimeFormatter format = DateTimeFormat.forPattern("HHmmss");
		return format.parseDateTime(time);

	}

	public static boolean validateTime6(String time) {
		DateTime d = parseTime6(time);
		return (d != null) && (time6(d).equals(time));
	}

	public static int getDayOfWeek(String date) {
		DateTime day = parseDate8(date);

		return day.getDayOfWeek();

	}

	public static int diffInMin(DateTime d1, DateTime d2) {
		long t1 = d1.getMillis();
		long t2 = d2.getMillis();
		double unit = 60000.0D;
		int absDiff = (int) Math.ceil(Math.abs(t1 - t2) / unit);
		return absDiff;
	}

	public static int diffInSec(DateTime d1, DateTime d2) {

		long t1 = d1.getMillis();
		long t2 = d2.getMillis();
		double unit = 1000.0D;
		int absDiff = (int) Math.ceil(Math.abs(t1 - t2) / unit);
		return absDiff;
	}

	public static int diffInDay(DateTime d1, DateTime d2) {
		long t1 = d1.getMillis();
		long t2 = d2.getMillis();
		double unit = 1000D * 60 * 60 * 24;
		int absDiff = (int) Math.ceil(Math.abs(t1 - t2) / unit);
		return absDiff;
	}

	public static int diffInDay(Date d1, Date d2) {
		return diffInDay(new DateTime(d1), new DateTime(d2));
	}

	public static int diffInSec(Date d1, Date d2) {
		return diffInSec(new DateTime(d1), new DateTime(d2));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<DateTime[]> slice(DateTime beginDate, DateTime endDate,
			int interval_sec) {
		List pieces = new ArrayList();
		while (beginDate.compareTo(endDate) <= 0) {
			DateTime nextEndDate = addSec(beginDate, interval_sec);
			if (nextEndDate.isAfter(endDate))
				nextEndDate = endDate;
			DateTime[] piece = new DateTime[2];
			piece[0] = beginDate;
			piece[1] = nextEndDate;
			pieces.add(piece);
			beginDate = addSec(nextEndDate, 1);
		}
		return pieces;
	}

	public static boolean isInTimeSpan(DateTime time, DateTime startTime,
			DateTime endTime) {
		if (time != null && startTime != null && endTime != null) {

			long startTimeMins = startTime.getMillis();
			long endTimeMins = endTime.getMillis();
			long timeMins = time.getMillis();
			if (startTimeMins <= timeMins && timeMins <= endTimeMins) {
				return true;
			}
		}
		return false;
	}

	public static List<String> getDates(DateTime from, DateTime to,
			String dateFormat) {
		if (from == null || to == null) {
			return null;
		}
		if (StringUtils.isBlank(dateFormat)) {
			dateFormat = "yyyy-MM-dd";
		}
		to = new DateTime(DateTimeUtil.getDate(to.plusDays(1), null));
		List<String> dates = new LinkedList<String>();
		while (from.isBefore(to)) {
			dates.add(from.toString(dateFormat));
			from = from.plusDays(1);
		}
		if (dates.isEmpty()) {
			return null;
		}
		return dates;
	}

	public static String getDate(DateTime date, String dateFormat) {
		if (date == null) {
			return null;
		}
		if (StringUtils.isBlank(dateFormat)) {
			dateFormat = "yyyy-MM-dd";
		}
		String dateStr = date.toString(dateFormat);
		return dateStr;
	}

	public static DateTime getStartDateTime(DateTime date, String dateFormat) {
		String dateStr = getDate(date, dateFormat);
		return new DateTime(dateStr);
	}

	public static DateTime getStartDateTime(Date date, String dateFormat) {
		DateTime beginDate = getBeginDateTime(date);
		return getStartDateTime(beginDate, dateFormat);
	}

	public static final String convertDateToString(String pattern,
			Locale locale, TimeZone zone, Date aDate) {
		if (locale == null)
			locale = Locale.getDefault();
		if (zone == null)
			zone = TimeZone.getDefault();
		SimpleDateFormat df = new SimpleDateFormat(pattern, locale);
		df.setTimeZone(zone);
		try {
			return df.format(aDate);
		} catch (Exception e) {
			return "";
		}
	}

	public static final String convertDateToString(String pattern, Date aDate) {
		Locale locale = Locale.CHINESE;
		return convertDateToString(pattern, locale, null, aDate);
	}

	public static final int getDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	//
	// public static void main(String[] args) {
	// System.out.println(diffdates(new DateTime().minusHours(21),
	// new DateTime("2013-10-14").plusHours(23)));
	// }
	public static void main(String[] args) {
		System.out.print(getDate("20MAR14", "ddMMMyy", Locale.US));
	}
}