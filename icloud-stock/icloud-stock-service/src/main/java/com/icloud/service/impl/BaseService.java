package com.icloud.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import com.icloud.framework.dao.hibernate.IHibernateBaseDao;
import com.icloud.service.IBaseService;

public abstract class BaseService<T> implements IBaseService<T> {

	private IHibernateBaseDao<T> baseDao;

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

}
