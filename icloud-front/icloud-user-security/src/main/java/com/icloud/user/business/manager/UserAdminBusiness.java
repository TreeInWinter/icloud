package com.icloud.user.business.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.icloud.user.service.ISessionService;

@Service("userAdminBusiness")
public class UserAdminBusiness {
	@Resource(name = "sessionService")
	private ISessionService sessionService;
}
