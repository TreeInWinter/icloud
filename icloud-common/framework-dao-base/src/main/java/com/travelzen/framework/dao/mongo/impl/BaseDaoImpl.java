package com.travelzen.framework.dao.mongo.impl;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.query.Query;
import com.github.jmkgreen.morphia.query.UpdateOperations;
import com.github.jmkgreen.morphia.query.UpdateResults;
import com.mongodb.WriteConcern;
import com.travelzen.framework.dao.annotation.ShowInList;
import com.travelzen.framework.dao.mongo.IBaseDao;
import com.travelzen.framework.dao.mongo.entity.CommonEntity;
import com.travelzen.framework.dao.util.MongoDBPKUtil;
import com.travelzen.framework.dao.util.SecurityContextUtil;
import com.travelzen.framework.dao.vo.PageVo;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class BaseDaoImpl<T extends CommonEntity, K> extends BasicDAO<T, K> implements IBaseDao<T, K> {
	private static Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);

	protected BaseDaoImpl(Datastore ds) {
		super(ds);
	}

	@Override
	public Key<T> save(T entity) {
		setEntityCreatedByRelaParams(entity);
		return super.save(entity);
	}

	@Override
	public Key<T> save(T entity, WriteConcern wc) {
		setEntityCreatedByRelaParams(entity);
		return super.save(entity, wc);
	}

	@Override
	public UpdateResults<T> update(Query<T> q, UpdateOperations<T> ops) {
		setEntityUpdatedByRelaParams(ops);
		return super.update(q, ops);
	}

	@Override
	public UpdateResults<T> updateFirst(Query<T> q, UpdateOperations<T> ops) {
		setEntityUpdatedByRelaParams(ops);
		return super.updateFirst(q, ops);
	}

	/**
	 * 为保存的实体赋于递增主键值
	 * 
	 * @param entity
	 * @return
	 */
	protected T setEntityIncrementPkId(T entity) {
		try {
			entity.setId(new ObjectId(MongoDBPKUtil.getLongPKByStrFormat(getEntityClass().getName(), ds)));
			return entity;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 由于每次保存实体对象的时候都要设置父类CommonEntity中的通用的参数值，比较麻烦
	 * 所有通过此方法统一设置，减少service层和action层的重复代码编写
	 * 
	 * @param entity
	 * @return
	 */
	protected void setEntityCreatedByRelaParams(T entity) {
		try {
			DateTime now = new DateTime();
			if (entity.getCreated() == null)
				entity.setCreated(now); // 设置created时间
			entity.setUpdated(now); // 设置updated时间
			ObjectId userId = new SecurityContextUtil<T>().getSessionUserId(); // 获得当前执行操作的用户ID
			if (userId != null) {
				if (entity.getUserId() == null)
					entity.setUserId(userId); // 设置userId
				if (entity.getCreatedBy() == null)
					entity.setCreatedBy(userId); // 设置createdBy
				entity.setUpdatedBy(userId); // 设置updatedBy
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 由于每次修改操作的都要设置父类CommonEntity中的通用的参数值，比较麻烦
	 * 所有通过此方法统一设置，减少service层和action层的重复代码编写
	 * 
	 * @param ops
	 */
	protected UpdateOperations<T> setEntityUpdatedByRelaParams(UpdateOperations<T> ops) {
		if (ops != null)
			try {
				ObjectId userId = new SecurityContextUtil().getSessionUserId(); // 获得当前执行操作的用户ID
				if (userId != null)
					ops.set("updatedBy", userId);
				ops.set("updated", new DateTime());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		return ops;
	}

	public Class<T> getEntityClass() {
		return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	@Override
	public PageVo list(int start, int limit, int pageSize, Query<T> query) {
		return list(start, limit, pageSize, query, false);
	}

	@Override
	public PageVo list(int start, int limit, int pageSize, Query<T> query, boolean filterNeedField) {
		return list(start, limit, pageSize, query, filterNeedField, null);
	}

	public PageVo list(int start, int limit, int pageSize, Query<T> query, boolean filterNeedField, String order) {
		PageVo result = new PageVo();
		query.offset(start).limit(limit);
		query.order(StringUtils.isBlank(order) ? "-updated" : order);
		List<T> data = query.asList();
		result.setData(filterNeedField ? filterNeedField(data) : data);
		result.setTotalCount(query.countAll());
		result.setPageSize(pageSize);
		result.setStart(start);
		result.setLimit(limit);
		return result;
	}

	/**
	 * 将每个对象需要的field 反射出来，不需要的去掉，方便页面的显示
	 * 
	 * @param list
	 * @return
	 */
	public List<Map<String, Object>> filterNeedField(List list) {
		if (list == null || list.isEmpty())
			return null;
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			for (Object o : list) {
				Map<String, Object> record = new HashMap<String, Object>();
				for (Method method : o.getClass().getMethods())
					if (ifListNeedForField(method)) {
						String methodName = method.getName();
						if (methodName.startsWith("get")) {
							String fieldName = methodName.substring(3, methodName.length());
							fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1, fieldName.length());
							Object value = method.invoke(o, new Object[] {});
							value = value == null ? "" : value.toString();
							record.put(fieldName, value);
						}
					}
				result.add(record);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	private boolean ifListNeedForField(Method m) {
		if (m == null)
			return false;
		ShowInList listNeedField = m.getAnnotation(ShowInList.class);
		return listNeedField == null ? false : listNeedField.value();
	}

	@Override
	public List<T> listAll() {
		return createQuery().filter("isActive", true).asList();
	}

	@Override
	public List<T> listAllWithDeleted() {
		return find().asList();
	}

	@Override
	public List<T> listActive() {
		return createQuery().filter("status", "ACTIVED").asList();
	}
}
