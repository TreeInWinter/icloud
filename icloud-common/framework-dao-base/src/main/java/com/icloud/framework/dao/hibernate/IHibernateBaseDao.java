package com.icloud.framework.dao.hibernate;

import java.util.List;

public interface IHibernateBaseDao<T> {
	Class<T> getDomainClass();

	public T getById(Integer id);

	public void update(T t);

	public void save(T t);

	public void delete(T t);

	public List<T> findAll();

	public void deleteById(Integer id);

	public long count();

	public void deteleteAll();
}
