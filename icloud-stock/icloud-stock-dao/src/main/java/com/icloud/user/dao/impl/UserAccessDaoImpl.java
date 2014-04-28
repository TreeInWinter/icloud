package com.icloud.user.dao.impl;

import org.springframework.stereotype.Repository;

import com.icloud.dao.impl.StockBaseDaoImpl;
import com.icloud.stock.model.UserAccess;
import com.icloud.user.dao.IUserAccessDao;

@Repository("userAccessDao")
public class UserAccessDaoImpl extends StockBaseDaoImpl<UserAccess> implements
		IUserAccessDao {

}
