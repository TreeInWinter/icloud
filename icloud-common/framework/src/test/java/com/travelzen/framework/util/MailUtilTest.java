package com.travelzen.framework.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MailUtilTest {

	MailUtil mailUtil;

	@Test
	public void testSend() throws IOException {
		mailUtil = new MailUtil();
		mailUtil.setHost("shmail1.travelzen.com");
		mailUtil.setUsername("biz.b2b");
		mailUtil.setPassword("123456");
//		mailUtil.setUsername("tops.test");
//		mailUtil.setPassword("Abc12345");
		mailUtil.setFrom("tops.test@travelzen.com");
		mailUtil.setTo("xiangjiang.bao@travelzen.com,guangxian.ding@travelzen.com,shui.ren@travelzen.com,tongli.wang@travelzen.com,xian.zeng@travelzen.com");
		mailUtil.setSubject("测试邮件");

//		String content = FileUtils.readFileToString(new File("/home/simon/酒店入住单.html"));
//		Map<String, String> images = new HashMap<> ();
//		images.put("logo", "/home/simon/travelzen_logo.gif");
//		mail.setImages(images);


		String html = "<html><head><title>测试一下！</title></head>"
				+ "<body><center>good morning</center>"
				+ "<img src=\"cid:img001\"/>"
				+ "<img src=\"cid:img002\"/>"
				+ "</body></html>";
		mailUtil.setContent(html);
//		mailUtil.setContent(content);
		mailUtil.setContentType("text/plain;charset=utf-8");
		mailUtil.setContentType("text/html;charset=utf-8");
		Map<String, String> images = new HashMap<String, String>();
		images.put("img001", "/home/simon/Pictures/phone.jpg");
		images.put("img002", "/home/simon/Pictures/美女.jpg");
//		mailUtil.setImages(images);
//		List<String> attachments = new ArrayList<String>();
//		attachments.add("/home/simon/Pictures/bycle.jpg");
//		mailUtil.setAttachments(attachments);
		mailUtil.sendMail();

	}

}
