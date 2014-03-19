package com.travelzen.framework.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.travelzen.framework.core.util.NetworkUtil;

public class NetworkUtilTest {
	@Test
	public void test_getLocalIp(){
		System.out.println(NetworkUtil.getLocalIp());
		assertNotNull(NetworkUtil.getLocalIp());
	}
}
