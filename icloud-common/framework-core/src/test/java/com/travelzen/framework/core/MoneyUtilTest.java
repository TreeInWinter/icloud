/**
 * 
 * Description: 
 * Copyright: Copyright (c) 2009
 * Company:联动优势
 * @author 任水
 * @version 1.0
 * @date Jan 15, 2010
 */
package com.travelzen.framework.core;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.junit.Test;

import com.travelzen.framework.core.util.MoneyUtil;

public class MoneyUtilTest extends TestCase {
	public void testYuan2Cent(){
		assertEquals("12300", MoneyUtil.yuan2Cent("123"));
		assertEquals("0", MoneyUtil.yuan2Cent("0"));
		assertEquals("10", MoneyUtil.yuan2Cent("0.1"));
		assertEquals("12", MoneyUtil.yuan2Cent("0.123"));
		assertEquals("1012", MoneyUtil.yuan2Cent("10.123"));
		assertEquals("-1012", MoneyUtil.yuan2Cent("-10.123"));
		assertEquals("1013", MoneyUtil.yuan2Cent("10.129"));
		assertEquals("-1013", MoneyUtil.yuan2Cent("-10.129"));
		assertEquals("1000", MoneyUtil.yuan2Cent("10.00"));
		assertEquals("1000", MoneyUtil.yuan2Cent("0010.00"));
		assertEquals("-1000", MoneyUtil.yuan2Cent("-0010.00"));
		assertEquals(100, MoneyUtil.yuan2Cent(new BigDecimal(1.001)));
		assertEquals(100, MoneyUtil.yuan2Cent(new BigDecimal(1)));
		assertEquals(101, MoneyUtil.yuan2Cent(new BigDecimal(1.006)));
		assertEquals(100001, MoneyUtil.yuan2Cent(new BigDecimal(1000.01)));
		assertEquals(100000001, MoneyUtil.yuan2Cent(new BigDecimal(1000000.01)));
		assertEquals(100000055, MoneyUtil.yuan2Cent(new BigDecimal(1000000.55)));
		assertEquals(100000010, MoneyUtil.yuan2Cent(new BigDecimal(1000000.1)));
		assertEquals(-100000010, MoneyUtil.yuan2Cent(new BigDecimal(-1000000.1)));
		assertEquals(34, MoneyUtil.yuan2Cent(new BigDecimal(0.335)));
	}
	public void testCent2Yuan(){
		assertEquals("0.01",MoneyUtil.cent2Yuan("1"));
		assertEquals("0.11",MoneyUtil.cent2Yuan("11"));
		assertEquals("0.00",MoneyUtil.cent2Yuan("0"));
		assertEquals("1.11",MoneyUtil.cent2Yuan("111"));
		assertEquals("1.11",MoneyUtil.cent2Yuan("0111"));
		assertEquals("1.11",MoneyUtil.cent2Yuan("0111"));
		assertEquals("-1.11",MoneyUtil.cent2Yuan("-111"));
		assertEquals("-11111111111111.11",MoneyUtil.cent2Yuan("-1111111111111111"));
		assertEquals("123456",MoneyUtil.cent2YuanFloor(12345699));
		assertEquals("123456",MoneyUtil.cent2YuanFloor(12345600));
	}
	@Test
	public void test_roundUpCent(){
		assertEquals(MoneyUtil.roundUpCent(1, MoneyUtil.SCALE.YUAN), 100);
		assertEquals(MoneyUtil.roundUpCent(101, MoneyUtil.SCALE.YUAN), 200);
		assertEquals(MoneyUtil.roundUpCent(199, MoneyUtil.SCALE.YUAN), 200);
		assertEquals(MoneyUtil.roundUpCent(101, MoneyUtil.SCALE.TEN_YUAN), 1000);
		assertEquals(MoneyUtil.roundUpCent(1000, MoneyUtil.SCALE.TEN_YUAN), 1000);
		assertEquals(MoneyUtil.roundUpCent(1001, MoneyUtil.SCALE.TEN_YUAN), 2000);
		assertEquals(MoneyUtil.roundDownCent(1, MoneyUtil.SCALE.YUAN), 0);
		assertEquals(MoneyUtil.roundDownCent(101, MoneyUtil.SCALE.YUAN), 100);
		assertEquals(MoneyUtil.roundDownCent(199, MoneyUtil.SCALE.YUAN), 100);
		assertEquals(MoneyUtil.roundDownCent(101, MoneyUtil.SCALE.TEN_YUAN), 0);
		assertEquals(MoneyUtil.roundDownCent(1000, MoneyUtil.SCALE.TEN_YUAN), 1000);
		assertEquals(MoneyUtil.roundDownCent(1001, MoneyUtil.SCALE.TEN_YUAN), 1000);
	}
}

