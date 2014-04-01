package com.cn.icloud.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class DynamicSubjectTest {
	public static void main(String[] args) throws Throwable {
		Subject rs = new RealSubject();// 这里指定被代理类
		InvocationHandler ds = new DynamicSubject(rs);
		Class<?> cls = rs.getClass();
		Subject subject = (Subject) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), ds);
		subject.request();
	}
}
