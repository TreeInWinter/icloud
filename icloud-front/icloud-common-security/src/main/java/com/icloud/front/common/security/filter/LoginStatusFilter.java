package com.icloud.front.common.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.web.filter.GenericFilterBean;

import com.travelzen.framework.logger.ri.RequestIdentityLogger;

public class LoginStatusFilter extends GenericFilterBean {

	private static final Logger logger = RequestIdentityLogger
			.getLogger(LoginStatusFilter.class);
	//
	// @Resource(name = "memberAuthUserService")
	// private MemberAuthUserService userService;

	private String loginUrl;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		// TopsUserDetails tud = TopsSecurityUtils.getUserFromSession();
		// if (tud == null) {
		// LOG.warn("login status is required for target resource:{}",
		// request.getRequestURI());
		// if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
		// response.setContentType("application/text; charset=utf-8");
		// PrintWriter writer = response.getWriter();
		// response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		// writer.print("请重新登陆!");
		// writer.flush();
		// writer.close();
		// return;
		// }else{
		// response.sendRedirect(request.getContextPath() + loginUrl);
		// return;
		// }
		// }
		//
		logger.info("LoginStatusFilter start");
		chain.doFilter(req, res);
		logger.info("LoginStatusFilter end");
		//
		// userService.refreshAccessTime(tud.getUserData().getKey());
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

}
