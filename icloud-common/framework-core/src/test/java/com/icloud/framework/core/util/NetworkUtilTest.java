package com.icloud.framework.core.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.icloud.framework.core.util.NetworkUtil;

public class NetworkUtilTest {
	@Test
	public void test_getLocalIp(){
		System.out.println(NetworkUtil.getLocalIp());
		assertNotNull(NetworkUtil.getLocalIp());
	}
}
