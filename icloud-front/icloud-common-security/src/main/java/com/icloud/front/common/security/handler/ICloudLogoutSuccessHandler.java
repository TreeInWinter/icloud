package com.icloud.front.common.security.handler;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class ICloudLogoutSuccessHandler implements LogoutSuccessHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(ICloudLogoutSuccessHandler.class);

	private String logoutSuccessUrl;

	// @Resource(name = "memberAuthUserService")
	// private MemberAuthUserService userService;
	//
	// @Resource(name = "memberAuthRedisService")
	// private MemberAuthRedisService redisService;

	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		// if (authentication == null || authentication.getPrincipal() == null
		// || !(authentication.getPrincipal() instanceof TopsUserDetails)) {
		// response.sendRedirect(request.getContextPath());
		// return;
		// }
		// TopsUserDetails tud = (TopsUserDetails)
		// authentication.getPrincipal();
		// userService.deauthenticate(tud.getUserData().getKey());
		// redisService.remove(MemberAuthUtils.getRedisSessionKey(request
		// .getSession()));
		LOG.info("User[{}] signed out successfully. username");
		if (logoutSuccessUrl == null) {
			response.sendRedirect(request.getContextPath());
		} else {
			response.sendRedirect(logoutSuccessUrl);
		}
	}

	public String getLogoutSuccessUrl() {
		return logoutSuccessUrl;
	}

	public void setLogoutSuccessUrl(String logoutSuccessUrl) {
		this.logoutSuccessUrl = logoutSuccessUrl;
	}

}
