package com.icloud.framework.dao.hibernate.impl;

import java.sql.SQLException;
import java.util.List;

import org.eclipse.jetty.util.log.Log;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

import com.icloud.framework.dao.hibernate.IHibernateBaseDao;
import com.icloud.framework.hibernate.util.GenericsUtils;

public class HibernateBaseDaoImpl<T> extends HibernateDaoSupport implements
		IHibernateBaseDao<T> {
	protected Class<T> domainClass;

	public HibernateBaseDaoImpl() {
		this.domainClass = GenericsUtils.getSuperClassGenricType(getClass());
	}

	@Override
	public Class<T> getDomainClass() {
		return this.domainClass;
	}

	@Override
	public T getById(Integer id) {
		return (T) getHibernateTemplate().load(domainClass, id);
	}

	@Override
	public void update(T t) {
		getHibernateTemplate().update(t);
	}

	@Override
	public void save(T t) {
		getHibernateTemplate().save(t);
	}

	@Override
	public void delete(T t) {
		getHibernateTemplate().delete(t);
	}

	@Override
	public List findAll() {
		// TODO Auto-generated method stub
		return (getHibernateTemplate().find("from " + domainClass.getName()
				+ " x"));

	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		Object obj = getById(id);
		getHibernateTemplate().delete(obj);
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		List list = getHibernateTemplate().find(
				"select count(*) from " + domainClass.getName() + " x");

		Long count = (Long) list.get(0);
		return count.longValue();

	}

	@Override
	public void deteleteAll() {
		// TODO Auto-generated method stub
		getHibernateTemplate().execute(new HibernateCallback<T>() {
			@Override
			public T doInHibernate(Session session) throws HibernateException,
					SQLException {
				// TODO Auto-generated method stub
				String hqlDelete = "delete " + domainClass.getName();
				session.createQuery(hqlDelete).executeUpdate();
				return null;
			}

		});

	}

}
