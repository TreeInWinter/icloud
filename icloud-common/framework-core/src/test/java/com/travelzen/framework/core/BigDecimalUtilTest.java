package com.travelzen.framework.core;
/**
 * 
 */


import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.Test;

import com.travelzen.framework.core.util.BigDecimalUtil;

/**
 * @author guobinwu
 *
 */
public class BigDecimalUtilTest {

	@Test
	public void test() throws Exception{
		
		System.out.println(BigDecimalUtil.add(1.55519999999111, 2.2200000000022, MathContext.DECIMAL128));
		System.out.println(BigDecimalUtil.add(1.55519999999111, 2.2200000000022, MathContext.DECIMAL32));
		System.out.println(BigDecimalUtil.add(1.55519999999111, 2.2200000000022, MathContext.DECIMAL64));
		System.out.println(BigDecimalUtil.add("1", "2"));
		System.out.println(BigDecimalUtil.getRound(1.256884, 8, 2));
		System.out.println(BigDecimalUtil.divide(10, 3, 2, BigDecimal.ROUND_HALF_UP));
		System.out.println(BigDecimalUtil.divide(7.6654589632115631, 3, 2, BigDecimal.ROUND_HALF_UP));
		System.out.println(BigDecimalUtil.divide((long)7.6654589632115631, (long)3, 2, BigDecimal.ROUND_HALF_UP));
		System.out.println(BigDecimalUtil.divide((long)7.66545, (long)3, 2, BigDecimal.ROUND_HALF_UP));
		System.out.println(BigDecimalUtil.divide((long)9.9, (long)3, 2, BigDecimal.ROUND_HALF_UP));
		System.out.println(BigDecimalUtil.divide((long)10, (long)3, 2, BigDecimal.ROUND_HALF_UP));
		System.out.println((long)9.9);
		System.out.println(BigDecimalUtil.divide("1434654", "46546461", 2, 2));
		System.out.println(BigDecimalUtil.divide("1434654", "46546461", 2, BigDecimal.ROUND_HALF_UP));
//		System.out.println(BigDecimalUtil.divide("aa", "dd", 2, 2));
		System.out.println(BigDecimalUtil.multiply(2.56655, 3.254566));
		System.out.println(BigDecimalUtil.multiply("1131", "12.415"));
		System.out.println(BigDecimalUtil.subtract(23.6544,2.365478 ));
		System.out.println(BigDecimalUtil.subtract("89563", "11224.2256655"));
		
		System.out.println(BigDecimalUtil.getRound(1.256884, 2, 4));	
		System.out.println(BigDecimalUtil.getRound(1.256884, 4, 2));
		
		
	}

}
