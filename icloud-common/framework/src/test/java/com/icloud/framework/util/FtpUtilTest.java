package com.icloud.framework.util;

import org.junit.Test;

import com.icloud.framework.util.FtpUtil;

public class FtpUtilTest {
	@Test
	public void test_put() throws Exception{
		 FtpUtil ftp = new FtpUtil();
	     ftp.connect("localhost");
	     ftp.login("shuiren", "pa$$w9RD");
	     ftp.cd("tmp");
	     ftp.put("pom.xml");
	     ftp.bye();
	}
}
