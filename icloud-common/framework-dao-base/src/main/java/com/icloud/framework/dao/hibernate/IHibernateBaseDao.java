package com.icloud.framework.dao.hibernate;

import java.util.List;

public interface IHibernateBaseDao<T> {
	Class<T> getDomainClass();

	public T load(String id);

	public void update(T t);

	public void save(T t);

	public void delete(T t);

	public List<T> getList();

	public void deleteById(String id);

	public int count();

	public void deteleteAll();
}
