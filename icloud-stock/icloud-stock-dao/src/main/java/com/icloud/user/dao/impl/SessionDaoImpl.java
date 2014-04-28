package com.icloud.user.dao.impl;

import org.springframework.stereotype.Repository;

import com.icloud.dao.impl.StockBaseDaoImpl;
import com.icloud.stock.model.Session;
import com.icloud.user.dao.ISessionDao;

@Repository("sessionDao")
public class SessionDaoImpl extends StockBaseDaoImpl<Session> implements
		ISessionDao {

}
