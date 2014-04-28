package com.icloud.user.service.impl;

import javax.annotation.Resource;

import com.icloud.framework.dao.hibernate.IHibernateBaseDao;
import com.icloud.framework.service.impl.SqlBaseService;
import com.icloud.stock.model.UserAccess;
import com.icloud.user.dao.IUserAccessDao;
import com.icloud.user.service.IUserAccessService;

public class UserAccessServiceImpl extends SqlBaseService<UserAccess> implements
		IUserAccessService {
	@Resource(name = "userAccessDao")
	private IUserAccessDao userAccessDao;

	@Override
	protected IHibernateBaseDao<UserAccess> getDao() {
		// TODO Auto-generated method stub
		return userAccessDao;
	}

}
