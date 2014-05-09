package com.icloud.framework.service;

import java.util.List;

public interface ISqlBaseService<T> {
	public T getById(Integer id);

	public void update(T t);

	public void save(T t);

	public void delete(T t);

	public List<T> findAll();

	public void deleteById(Integer id);

	public long count();

	public void deteleteAll();

	public List<T> findByProperies(String property, Object value);

	public long countByProperties(String property, Object value);

	public List<T> findByProperties(String property, Object value, int start,
			int limit);

	public List<T> findAll(int start, int limit);

}
