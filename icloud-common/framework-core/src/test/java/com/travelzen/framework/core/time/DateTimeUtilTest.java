package com.travelzen.framework.core.time;

import java.util.Date;

import org.junit.Test;

public class DateTimeUtilTest {

	@Test
	public void test_parseDate8() {
		System.out.println(DateTimeUtil.parseDate8("20121201").toDate());
		System.out.println(DateTimeUtil.addDay(DateTimeUtil.parseDate8("20121201"), 1).toDate());
	}
	@Test
	public void test_getBeginDateTime() {
		System.out.println(DateTimeUtil.getBeginDateTime(DateTimeUtil.parseDate8("20121201").toDate()));
		System.out.println(DateTimeUtil.getBeginDateTime(new Date()));
		System.out.println(new Date());
	}
	@Test
	public void diffInDay() {
		Date a = DateTimeUtil.parseDate8("20121201").toDate();
		Date b = DateTimeUtil.parseDate8("20130101").toDate();
		org.junit.Assert.assertEquals(31, DateTimeUtil.diffInDay(a, b));
	}}
