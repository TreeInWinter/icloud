package com.travelzen.framework.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Provide a series of function that relative to convert date information.
 *
 * @author muyuansun
 */
public class DateUtils {

	private DateUtils() {

	}

	/**
	 * @param sDate
	 * @return
	 */
	public static Date getDate(String sDate) {
		return getDate(sDate, "");
	}

	/**
	 * @param sDate
	 * @return
	 */
	public static Date getJustDate(String sDate) {
		return getDate(sDate, "yyyy-MM-dd");
	}

	/**
	 * @param date 日期  format : yyyy-MM-dd
	 * @param time 时间  format : hh:mm
	 * @return date
	 */
	public static Date getFlightDate(String date, String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			return sdf.parse(date + " " + time);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * @param lDate
	 * @return
	 */
	public static Date getDate(long lDate) {
		return getDate(lDate, "");
	}

	/**
	 * @param lDate
	 * @return
	 */
	public static Date getJustDate(long lDate) {
		return getDate(lDate, "yyyy-MM-dd");
	}

	public static Date getDate(long lDate, String sFormat) {
		if (sFormat == null || "".equals(sFormat)) {
			sFormat = "yyyy-MM-dd HH:mm:ss";
		}

		return getDate(formatDate(new Date(lDate), sFormat), sFormat);
	}

	public static Date getDate(String sDate, String sFormat) {
		Date dValue;

		try {
			if (sFormat == null || "".equals(sFormat)) {
				sFormat = "yyyy-MM-dd HH:mm:ss";
			}

			dValue = new SimpleDateFormat(sFormat).parse(sDate);
		} catch (Exception e) {
			dValue = new Date(0);
		}

		return dValue;
	}

	public static String formatDate(long sDate, String sFormat) {
		return formatDate(new Date(sDate), sFormat);
	}

	public static String formatDate(long lDate) {
		return formatDate(new Date(lDate), "");
	}

	public static String formatJustDate(long lDate) {
		return formatDate(new Date(lDate), "yyyy-MM-dd");
	}

	public static String formatDate(Date date, String sFormat) {
		if (sFormat == null || "".equals(sFormat)) {
			sFormat = "yyyy-MM-dd HH:mm:ss";
		}

		return new SimpleDateFormat(sFormat).format(date);
	}

	public static String formatDate(Date date) {
		return formatDate(date, TimeZone.getDefault());
	}

	public static String formatDate(Date date, TimeZone timeZone) {
		return formatDate(date, timeZone, "yyyy-MM-dd");
	}

	public static String formatDate(Date date, TimeZone timeZone, String sFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(sFormat);
		dateFormat.setTimeZone(TimeZone.getDefault());
		return dateFormat.format(date);
	}

	public static String simplifyDate(String date) {

		Date d = getDate(date, "yyyy-MM-dd HH:mm:ss");
		return formatDate(d, "yy-MM-dd HH:mm");

	}

	public static String getTodayStr() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date todate = new Date(System.currentTimeMillis());
		String today = df.format(todate);
		return today;
	}

	public static String getDateStamp(int intFormat) {
		return findFormat(intFormat).format(new Date());
	}

	public static final int TIMESTAMPTYPE_UNIX = 2;

	/**
	 * 将日期对象转为指定格式日期字符串
	 *
	 * @param date
	 * @param intFormat
	 * @return
	 */
	public static String format(Date date, int intFormat) {
		SimpleDateFormat sdf = findFormat(intFormat);
		return sdf.format(date);
	}

	/**
	 * 根据参数获得简单日期格式对象
	 *
	 * @param intFormat
	 * @return
	 */
	public static SimpleDateFormat findFormat(int intFormat) {
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
				strFormat = "yyyy/MM/dd";
				break;

			case 18: // '\r'
				strFormat = "yyyy/MM/dd H:mm:ss";
				break;

			case 19: // '\r'
				strFormat = "yyyy-MM-dd";
				break;
				
			case 20:
				strFormat= "yyyy-MM-dd HH:mm:ss";
				break;

			default:
				strFormat = "yyyy'??'MM'??'dd'??' H:mm:ss.S";
				break;
		}
		return new SimpleDateFormat(strFormat);
	}

	public static String formatDateStr(String dateStr, int newtimestampType,
	                                   int oldtimestampType) throws ParseException {
		Date oldDate = findFormat(oldtimestampType).parse(dateStr);
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
		boolean isPassed = false;

		SimpleDateFormat sdf = findFormat(timestampType);
		sdf.setLenient(false);
		if (strDate != null && strDate.length() > 0) {
			try {
				Date dtCheck = (sdf.parse(strDate));
				String strCheck = sdf.format(dtCheck);
				if (strDate.equals(strCheck)) {
					isPassed = true;
				} else {
					isPassed = false;
				}
			} catch (Exception e) {
				isPassed = false;
			}
		}
		return isPassed;
	}

	public static List<String> getDateList(String startTime, String endTime)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> dateInfos = new ArrayList<String>();
		Date start = null;
		Date end = null;
		startTime = startTime + " 00:00:00";
		endTime = endTime + " 23:59:59";
		try {
			start = sdf.parse(startTime);
			end = sdf.parse(endTime);
		} catch (Exception e) {
			throw new Exception("Parse date exception !");
		}

		if (start.after(end)) {
			throw new Exception("Query time error.Start time after end time.");
		}

		Calendar calStart = Calendar.getInstance();
		calStart.setTime(start);

		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(end);

		sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (calStart.after(calEnd)) {
			dateInfos.add(startTime);
		} else {
			while (!calStart.after(calEnd)) {
				dateInfos.add(sdf.format(calStart.getTime()));
				calStart.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		return dateInfos;
	}

	/**
	 * 通过起始时间和结束时间获得期间有多少分钟
	 *
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	public static long findDateSpaceInMinuties(String startTime, String endTime)
			throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");

		Date startDate = sdf.parse(startTime + " 00:00:00");

		Date endDate = sdf.parse(endTime + " 23:59:59");

		long minutes = (endDate.getTime() - startDate.getTime()) / (1000 * 60);

		return minutes + 1;
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
	public static List<String> findDateScopeStandardFormat(String startTime,
	                                                       String endTime, int divisor, String strFormat)
			throws ParseException {
		List<String> returnValue = null;
		if (startTime == null || startTime.length() <= 0) {
			return returnValue;
		}
		if (endTime == null || endTime.length() <= 0) {
			return returnValue;
		}
		if (divisor == 0 || divisor % 2 != 0) {
			return returnValue;
		}
		returnValue = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);

		Date startDate = sdf.parse(startTime + " 00:00:00");
		Date endDate = sdf.parse(endTime + " 23:59:59");

		Calendar calStartTime = Calendar.getInstance();
		calStartTime.setTime(startDate);

		Calendar calEndTime = Calendar.getInstance();
		calEndTime.setTime(endDate);

		int minuties = (int) findDateSpaceInMinuties(startTime, endTime)
				/ divisor;

		while (!calStartTime.after(calEndTime)) {
			returnValue.add(sdf.format(calStartTime.getTime()));
			calStartTime.add(Calendar.MINUTE, minuties);
		}
		return returnValue;
	}

	public static long getDateTime(String date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(date).getTime() / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 从日期对象得到该日期零点的时间戳
	 *
	 * @param date
	 * @param timestampType
	 * @return
	 */
	public static long getZeroTimeStampOfDay(Date date, int timestampType) {
		long returnValue = -1;
		Calendar calendar = null;
		try {

			calendar = GregorianCalendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.HOUR_OF_DAY, 0);

			returnValue = calendar.getTimeInMillis();

			if (timestampType == TIMESTAMPTYPE_UNIX)
				returnValue = returnValue / 1000;
		} catch (Exception e) {
			returnValue = -1;
		}
		return returnValue;
	}

	/**
	 * 获取日期对应的时间
	 *
	 * @param date
	 * @param timestampType
	 * @return
	 */
	public static long getDateTimestamp(Date date, int timestampType) {
		long returnValue = -1;
		try {
			if (date == null) {
				return -1;
			}
			returnValue = date.getTime();

			if (timestampType == TIMESTAMPTYPE_UNIX) {
				returnValue = returnValue / 1000;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	public static String findYestarday(int timestampType) {
		Calendar calendar = null;
		calendar = GregorianCalendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.add(Calendar.DATE, -1);

		return DateUtils.format(calendar.getTime(), timestampType);
	}

	public static Date formatDate(String date, int intFormat) throws Exception {
		Calendar calendar = null;
		calendar = GregorianCalendar.getInstance();
		calendar.setTime(findFormat(intFormat).parse(date));
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}

	public static String addDate(String dateStr, int intFormat, int addNum)
			throws Exception {
		Calendar calendar = null;
		calendar = GregorianCalendar.getInstance();
		calendar.setTime(findFormat(intFormat).parse(dateStr));
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.add(Calendar.DATE, addNum);
		return findFormat(intFormat).format(calendar.getTime());
	}

	public static Date toDate(String param, String format) throws ParseException {
		if (StringUtils.isEmpty(param)) {
			return null;
		}
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		date = sdf.parse(param);
		return date;
	}

	public static Date computeDate(Date date, int day) {
		GregorianCalendar nowDate = new GregorianCalendar();
		nowDate.setTime(date);
		nowDate.add(GregorianCalendar.DATE, day);
		Date computeAfter = nowDate.getTime();
		return computeAfter;
	}

	public static Date todayTrim(Date date) {
		Calendar calendar = null;
		calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}

	/**
	 * 计算年龄
	 */
	public static int getAge(Date birthDay) {
		Calendar noewCal = Calendar.getInstance();

		if (noewCal.before(birthDay)) {
			throw new IllegalArgumentException("The birthDay is after Now.It's unbelievable!");
		}

		int yearNow = noewCal.get(Calendar.YEAR);
		int monthNow = noewCal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = noewCal.get(Calendar.DAY_OF_MONTH);

		noewCal.setTime(birthDay);
		int yearBirth = noewCal.get(Calendar.YEAR);
		int monthBirth = noewCal.get(Calendar.MONTH);
		int dayOfMonthBirth = noewCal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				age--;
			}
		}

		return age;
	}

	/**
	 * 根据日期计算星期
	 * 1->周日, 2->周一
	 *
	 * @param date
	 * @return
	 */
	public static int getWeek(Date date) {
		int weekIndex = -1;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		weekIndex = calendar.get(Calendar.DAY_OF_WEEK);
		return weekIndex;
	}
	
	/**
	 * 获取中文大写星期  DAY OF WEEK
	 * @param date
	 * @return
	 */
	public static String getDayOfWeek(Date date){
		Calendar  cal = Calendar.getInstance();
		cal.setTime(date);
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		return weekDays[cal.get(Calendar.DAY_OF_WEEK)];
	}
	
	/**
	 * 获取中文大写星期  DAY OF WEEK
	 * @param cal
	 * @return
	 */
	public static String getDayOfWeek(Calendar cal){
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		return weekDays[cal.get(Calendar.DAY_OF_WEEK)-1];
	}

}