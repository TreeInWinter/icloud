package com.icloud.user.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.icloud.framework.dao.hibernate.IHibernateBaseDao;
import com.icloud.framework.service.impl.SqlBaseService;
import com.icloud.stock.model.Session;
import com.icloud.user.dao.ISessionDao;
import com.icloud.user.service.ISessionService;

@Service("sessionService")
public class SessionServiceImpl extends SqlBaseService<Session> implements
		ISessionService {
	@Resource(name = "sessionDao")
	private ISessionDao sessionDao;

	@Override
	protected IHibernateBaseDao<Session> getDao() {
		// TODO Auto-generated method stub
		return sessionDao;
	}

}
