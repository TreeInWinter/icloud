package com.icloud.user.business.operation;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.icloud.user.service.ISessionService;

@Service("userLogOperation")
public class UserLogOperation {
	@Resource(name = "sessionService")
	private ISessionService sessionService;

}
