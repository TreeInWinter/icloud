package com.icloud.springmvc.interceptor;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class TZI18nInterceptor extends HandlerInterceptorAdapter {
	@Resource
	private LocaleResolver localeResolver;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestLocale = request.getParameter("request_locale");
		if (requestLocale != null) {
			Locale locale;
			if ("en_US".equals(requestLocale)) {
				locale = Locale.US;
			} else {
				locale = Locale.SIMPLIFIED_CHINESE;
			}
			localeResolver.setLocale(request, response, locale);
		}
		return true;
	}
}
