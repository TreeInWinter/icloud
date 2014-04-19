package com.icloud.front.common.filter;

import java.io.IOException;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.icloud.framework.logger.ri.RequestIdentityHolder;
import com.icloud.framework.logger.ri.RequestIdentityLogger;

public class AccessLogSessionFilter implements Filter {
	private static final Logger logger = RequestIdentityLogger
			.getLogger(AccessLogSessionFilter.class);

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		logger.debug("AccessLogSessionFilter start");
		String reqId = genReqId();
		RequestIdentityHolder.set("username = " + reqId);
		// String username = null;
		// TopsUserDetails tud = TopsSecurityUtils.getUserFromSession();
		// if (tud != null) {
		// username = tud.getUsername();
		// } else {
		// username = FrontUtils.getIpAddr(req);
		// }
		// String reqId = username + '-' + genReqId();
		// RequestIdentityHolder.set(reqId);
		res.setHeader("Tops-Request-Identity", reqId);
		chain.doFilter(request, response);
		logger.debug("AccessLogSessionFilter end ");
		RequestIdentityHolder.remove();
	}

	private static Random RDM = new Random();

	protected String genReqId() {
		return String.format("%08x", RDM.nextInt());
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {
	}

}
