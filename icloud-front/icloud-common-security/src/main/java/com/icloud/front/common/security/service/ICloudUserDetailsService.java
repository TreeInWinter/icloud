package com.icloud.front.common.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.icloud.front.common.security.model.ICloudUserDetails;

@Component("icloudUserDetailsService")
public class ICloudUserDetailsService implements UserDetailsService {

	// @Resource(name = "memberAuthUserService")
	// private MemberAuthUserService userService;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		// User user = userService.getByUsername(username);
		// if (user == null) {
		// throw new UsernameNotFoundException(username + " not found.");
		// }
		return new ICloudUserDetails();
	}

}
