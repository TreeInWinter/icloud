package com.icloud.front.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessLogSessionFilter implements Filter {
	private static final Logger logger = LoggerFactory
			.getLogger(AccessLogSessionFilter.class);

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		logger.info("LogRedisSessionFilter11111 start");
		chain.doFilter(request, response);
		logger.info("LogRedisSessionFilter11111 end");
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {
	}

}
