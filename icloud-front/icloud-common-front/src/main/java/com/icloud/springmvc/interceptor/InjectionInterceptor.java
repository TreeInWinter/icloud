package com.icloud.springmvc.interceptor;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 防止sql注入
 * 
 * @author wangmeng
 * 
 */
public class InjectionInterceptor extends HandlerInterceptorAdapter {

	private final String atackStr = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|; |or|-|+|,";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		HttpServletRequest req = (HttpServletRequest) request;
		Iterator values = req.getParameterMap().values().iterator();// 获取所有的表单参数
		while (values.hasNext()) {
			String[] value = (String[]) values.next();
			for (int i = 0; i < value.length; i++) {
				if (isAttackStr(value[i])) {
					throw new Exception();
				}
			}
		}
		return true;
	}

	public boolean isAttackStr(String str) {
		String[] atackStrs = atackStr.split("\\|");
		for (int i = 0; i < atackStrs.length; i++) {
			if (str.indexOf(" " + atackStrs[i] + " ") >= 0) {
				return true;
			}
		}
		return false;
	}
}
