package com.icloud.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class TZBeanUtils {

	private static Logger logger = LoggerFactory.getLogger(TZBeanUtils.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setProperties(Object bean, HttpServletRequest request) throws IllegalAccessException, InvocationTargetException {
		HashMap map = new HashMap();
		Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			map.put(name, request.getParameterValues(name));
		}
		BeanUtils.populate(bean, map);
	}

	@SuppressWarnings("rawtypes")
	public static Object trimObjectFields(Object obj, boolean null2Empty) {
		if (obj == null)
			return "";
		Class cls = obj.getClass();
		if (obj instanceof String)
			return StringUtils.trimToEmpty(obj.toString());
		List<Field> allFields = new ArrayList<Field>();
		while (cls != null && cls != Object.class) {
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields)
				allFields.add(field);
			cls = cls.getSuperclass();
		}
		try {
			for (Field field : allFields) {
				if (Modifier.isFinal(field.getModifiers()))
					continue;
				field.setAccessible(true);
				Object value = field.get(obj);
				if (value instanceof String)
					field.set(obj, StringUtils.trimToEmpty((String) value));
				if(value == null && field.getType() == String.class && null2Empty)
					field.set(obj, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	public static Object trimObjectFields(Object obj) {
		return trimObjectFields(obj, false);
	}
	public static String describe(Object obj) {
		try {
			return new Gson().toJson(obj);
		} catch (Throwable thr) {
			logger.error("", thr);
			return "";
		}
	}
	
	public static Object Empty2NullObjectFields(Object obj) {
		if (obj == null)
			return obj;
		Class cls = obj.getClass();
		if (obj instanceof String){
			if(obj != null && obj.toString().equals(""))
				return null;
		}
			
		List<Field> allFields = new ArrayList<Field>();
		while (cls != null && cls != Object.class) {
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields)
				allFields.add(field);
			cls = cls.getSuperclass();
		}
		try {
			for (Field field : allFields) {
				if (Modifier.isFinal(field.getModifiers()))
					continue;
				field.setAccessible(true);
				Object value = field.get(obj);
				if (value instanceof String){
					if(value != null && value.toString().equals("")){
						field.set(obj, null);
					}
				}
					
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

}
