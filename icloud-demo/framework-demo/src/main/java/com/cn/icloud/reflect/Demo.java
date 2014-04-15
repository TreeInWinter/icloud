package com.cn.icloud.reflect;

public class Demo {
	private String a;

	public Demo(String a) {
		this.a = a;
	}

	private String getPrivate() {
		System.out.println("yes,in me");
		return "getPrivate";
	}

}
