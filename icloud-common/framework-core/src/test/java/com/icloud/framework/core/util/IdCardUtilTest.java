package com.icloud.framework.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.icloud.framework.core.util.IdCardUtil;

public class IdCardUtilTest {

	@Test
	public void verify() {
		assertTrue(IdCardUtil.verify("370983198311065830"));
		assertFalse(IdCardUtil.verify("370983198311065831"));
	}
	@Test
	public void getBirthDate(){
		assertEquals("1983-11-06", IdCardUtil.getBirthDate("370983198311065830"));
	}
	@Test
	public void isMale(){
		assertTrue(IdCardUtil.isMale("370983198311065830"));
	}

}
