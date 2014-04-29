package com.icloud.user.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.icloud.framework.dao.hibernate.IHibernateBaseDao;
import com.icloud.framework.service.impl.SqlBaseService;
import com.icloud.stock.model.User;
import com.icloud.user.dao.IUserDao;
import com.icloud.user.service.IUserService;

@Service("userService")
public class UserServiceImpl extends SqlBaseService<User> implements
		IUserService {
	@Resource(name = "userDao")
	private IUserDao userDao;

	@Override
	protected IHibernateBaseDao<User> getDao() {
		// TODO Auto-generated method stub
		return userDao;
	}

}
