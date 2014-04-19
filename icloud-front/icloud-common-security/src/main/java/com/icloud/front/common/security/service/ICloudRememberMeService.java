package com.icloud.front.common.security.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;

import com.icloud.front.common.security.model.ICloudUserDetails;

public class ICloudRememberMeService extends AbstractRememberMeServices {

	private static final Logger LOG = LoggerFactory
			.getLogger(ICloudRememberMeService.class);

	private boolean useSecureCookie;

	// @Resource(name = "memberAuthUserService")
	// private MemberAuthUserService userService;
	//
	// @Resource
	// private TopsUserDataCompleter topsPostAnthentication;

	public ICloudRememberMeService(
			ICloudUserDetailsService icloudUserDetailsService) {
		super("adsfda", icloudUserDetailsService);
		// this.setCookieName(MemberAuthUtils.getRememberMeCookieName());
	}

	@Override
	protected void onLoginSuccess(HttpServletRequest request,
			HttpServletResponse response,
			Authentication successfulAuthentication) {
		// TopsUserDetails tud = (TopsUserDetails)
		// successfulAuthentication.getPrincipal();
		// String token = userService.markLogin(tud.getUserData().getKey(), 1000
		// * this.getTokenValiditySeconds());
		// MemberAuthUtils.addRememberMeCookie(token,
		// this.getTokenValiditySeconds(), useSecureCookie, request,
		// response);
		System.out.println("1111111111111");
	}

	@Override
	protected UserDetails processAutoLoginCookie(String[] cookieTokens,
			HttpServletRequest request, HttpServletResponse response)
			throws RememberMeAuthenticationException, UsernameNotFoundException {
		// User user = null;
		// try {
		// Cookie ck = getCookie(request);
		// if (ck == null) {
		// throw new RememberMeAuthenticationException("no cookie found.");
		// }
		// LOG.debug("process auto login, token[{}]", ck.getValue());
		// user = userService.authenticateToken(ck.getValue());
		// if (PlatformUtils.getPlatform() != user.getPlatform()) {
		// throw new MemberException(MemberExceptionCode.PLATFORM_NOT_MATCH,
		// "not current platform user");
		// }
		// LOG.info("User[{}] automatical login succeed", user.getUsername());
		// } catch (MemberException e) {
		// LOG.warn("authenticate token failed. ", e);
		// if
		// (e.getErrorCode().equals(MemberExceptionCode.USERNAME_ERROR.name()))
		// {
		// throw new UsernameNotFoundException("username not found.");
		// } else {
		// throw new RememberMeAuthenticationException(e.getMessage());
		// }
		// }
		ICloudUserDetails iud = new ICloudUserDetails();
		// topsPostAnthentication.check(tud);
		return iud;
	}

	@Override
	public void logout(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) {
		// MemberAuthUtils.removeRememberMeCookie(useSecureCookie, request,
		// response);
	}

	// private Cookie getCookie(HttpServletRequest req) {
	// Cookie[] cookies = req.getCookies();
	// for (Cookie ck : cookies) {
	// if (MemberAuthUtils.getRememberMeCookieName().equals(ck.getName())) {
	// return ck;
	// }
	// }
	// return null;
	// }

	public boolean getUseSecureCookie() {
		return useSecureCookie;
	}

	public void setUseSecureCookie(boolean useSecureCookie) {
		this.useSecureCookie = useSecureCookie;
	}

	public boolean rememberMeRequested(HttpServletRequest request) {
		return super.rememberMeRequested(request, this.getParameter());
	}

}
