package com.cn.icloud.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.util.ReflectionUtils;

public class DemoTest {
	public static void main(String[] args) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Demo deom = new Demo("aaa");
		Field[] fields = deom.getClass().getDeclaredFields();
		// fields = deom.getClass().getFields();
		for (Field field : fields) {
			System.out.println(field.getName());
			ReflectionUtils.makeAccessible(field);
			String value = (String) ReflectionUtils.getField(field, deom);
			System.out.println(value);
		}
		Method[] declaredMethods = deom.getClass().getDeclaredMethods();
		for (Method method : declaredMethods) {
			System.out.println(method.getName());
			ReflectionUtils.makeAccessible(method);
			String retobj = (String) method.invoke(deom, null);
			Class<?> returnType = method.getReturnType();
			System.out.println(retobj);
			System.out.println(returnType.getName());
		}
	}
}
