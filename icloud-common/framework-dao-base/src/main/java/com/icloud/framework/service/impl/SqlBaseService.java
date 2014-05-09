package com.icloud.framework.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import com.icloud.framework.dao.hibernate.IHibernateBaseDao;
import com.icloud.framework.service.ISqlBaseService;

public abstract class SqlBaseService<T> implements ISqlBaseService<T> {

	protected IHibernateBaseDao<T> baseDao;

	protected abstract IHibernateBaseDao<T> getDao();

	@PostConstruct
	protected void initBaseDao() {
		this.baseDao = getDao();
	}

	@Override
	public T getById(Integer id) {
		// TODO Auto-generated method stub
		return this.baseDao.getById(id);
	}

	@Override
	public void update(T t) {
		// TODO Auto-generated method stub
		this.baseDao.update(t);
	}

	@Override
	public void save(T t) {
		// TODO Auto-generated method stub
		this.baseDao.save(t);
	}

	@Override
	public void delete(T t) {
		// TODO Auto-generated method stub
		this.baseDao.delete(t);
	}

	@Override
	public List<T> findAll() {
		// TODO Auto-generated method stub
		return this.baseDao.findAll();
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		this.baseDao.deleteById(id);
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return this.baseDao.count();
	}

	@Override
	public void deteleteAll() {
		// TODO Auto-generated method stub
		this.baseDao.deteleteAll();
	}

	@Override
	public List<T> findByProperies(String property, Object value) {
		return this.baseDao.findByProperty(property, value);
	}

	@Override
	public long countByProperties(String property, Object value) {
		// TODO Auto-generated method stub
		return this.baseDao.countByProperty(property, value);
	}

	@Override
	public List<T> findByProperties(String property, Object value, int start,
			int limit) {
		return this.baseDao.findByProperty(property, value, start, limit);
	}

	@Override
	public List<T> findAll(int start, int limit) {
		return this.baseDao.findAll(start, limit);
	}

}
