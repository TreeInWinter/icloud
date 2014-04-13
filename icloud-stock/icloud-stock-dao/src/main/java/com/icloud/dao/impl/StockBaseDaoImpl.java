package com.icloud.dao.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.hibernate.SessionFactory;

import com.icloud.dao.StockBaseDao;
import com.icloud.framework.dao.hibernate.impl.HibernateBaseDaoImpl;

public class StockBaseDaoImpl<T> extends HibernateBaseDaoImpl<T> implements
		StockBaseDao<T> {
	@Resource(name = "stockSessionFactory")
	private SessionFactory stockSessionFactory;

	@PostConstruct
	public void replaceDatastore() {
		// super.setDatastore(hotelDatastore);
		super.setSessionFactory(stockSessionFactory);
	}

}
