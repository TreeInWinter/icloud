package com.nicolatesser.datastructure.strings;

import junit.framework.Assert;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testAppendStringChar() {
		String s1 = StringUtils.createString("test");
		
		String s2 = StringUtils.append(s1, 'a');
		
		System.out.println(s1==s2);
		
		Assert.assertFalse(s1==s2);
		
		
		
	}

	@Test
	public void testAppendStringBufferChar() {
		StringBuffer sb1 = StringUtils.createStringBuffer("test");
		
		StringBuffer sb2 = StringUtils.append(sb1, 'a');
		
		System.out.println(sb1==sb2);
		
		Assert.assertTrue(sb1==sb2);
		
		
		

	}

}
