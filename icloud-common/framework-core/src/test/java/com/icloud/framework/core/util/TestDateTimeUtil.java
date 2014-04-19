package com.icloud.framework.core.util;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class TestDateTimeUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DateTime now = new DateTime() ;
		
		 System.out.println( DateTimeFormat.fullDateTime().withLocale(Locale.CHINESE).print(now));

	}

}
