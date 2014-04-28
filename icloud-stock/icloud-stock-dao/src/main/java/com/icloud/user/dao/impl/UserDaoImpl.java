package com.icloud.user.dao.impl;

import org.springframework.stereotype.Repository;

import com.icloud.dao.impl.StockBaseDaoImpl;
import com.icloud.stock.model.User;
import com.icloud.user.dao.IUserDao;

@Repository("userDao")
public class UserDaoImpl extends StockBaseDaoImpl<User> implements IUserDao {

}
