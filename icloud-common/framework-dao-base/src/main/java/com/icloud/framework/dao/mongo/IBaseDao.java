package com.icloud.framework.dao.mongo;

import java.util.List;
import java.util.Map;

import com.github.jmkgreen.morphia.dao.DAO;
import com.github.jmkgreen.morphia.query.Query;
import com.icloud.framework.dao.vo.PageVo;

public interface IBaseDao<T,K> extends DAO<T, K>{
	Query<T> createQuery();

	PageVo list(int start, int limit, int pageSize, Query<T> query);

	PageVo list(int start, int limit, int pageSize, Query<T> query,
			boolean filterNeedField);

	PageVo list(int start, int limit, int pageSize, Query<T> query,
			boolean filterNeedField, String order);

	public List<Map<String, Object>> filterNeedField(List list);

	// 列出全部，不包括删除的
	public List<T> listAll();

	// 列出全部，包括已删除的
	public List<T> listAllWithDeleted();

	// 列出所有活跃状态的
	public List<T> listActive();
}
