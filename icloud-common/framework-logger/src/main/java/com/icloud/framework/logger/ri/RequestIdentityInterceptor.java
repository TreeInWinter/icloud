package com.icloud.framework.logger.ri;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


public class RequestIdentityInterceptor implements HandlerInterceptor {

	private static Random RDM = new Random();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		RequestIdentityHolder.set(genReqId(request, response));
		response.addHeader("tops-request-identity", RequestIdentityHolder.get());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception { }

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		RequestIdentityHolder.remove();
	}

	protected String genReqId(HttpServletRequest request, HttpServletResponse response) {
		return String.format("%08x", RDM.nextInt());
	}

}
