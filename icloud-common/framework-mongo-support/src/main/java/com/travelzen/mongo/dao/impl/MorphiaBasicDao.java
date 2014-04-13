package com.travelzen.mongo.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.github.jmkgreen.morphia.query.Query;
import com.github.jmkgreen.morphia.query.UpdateOperations;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.travelzen.mongo.dao.IBasicDao;
import com.travelzen.mongo.entity.MorphiaEntity;


public abstract class MorphiaBasicDao<E extends MorphiaEntity<I>, I> implements IBasicDao<E, I> {
	/**
	 * 该field当作缓存，不建议直接使用。
	 * 请使用 {@link getEntityClass()})
	 */
	@Deprecated
	private Class<E> entityClass;

	@Resource
	protected Datastore datastore;

	protected Datastore getDatastore() {
		return this.datastore;
	}

	@Override
	public void setDatastore(Datastore ds) {
		this.datastore = ds;
	}

	@Override
	public I create(E entity) {
		if (entity.getId() != null) {
			Key<?> k = datastore.exists(entity);
			if (k != null) {
				throw new IllegalArgumentException("id already exists.");
			}
		}
		datastore.save(entity);
		return entity.getId();
	}

	@Override
	public I createOrReplace(E entity) {
		datastore.save(entity);
		return entity.getId();
	}

	@Override
	public WriteResult deleteById(I id) {
		return datastore.delete(getEntityClass(), id);
	}

	@Override
	public E getById(I id) {
		return datastore.get(getEntityClass(), id);
	}

	@Override
	public boolean isExists(I id) {
		return datastore.exists(new Key<E>(getEntityClass(), id)) != null;
	}

	@SuppressWarnings({ "unchecked", "deprecation"})
	protected Class<E> getEntityClass() {
		if (entityClass != null) {
			return entityClass;
		}
		Type superclass = this.getClass().getGenericSuperclass();
		if (superclass instanceof Class) {
			throw new RuntimeException("Missing type parameter.");
		}
		ParameterizedType parameterized = (ParameterizedType) superclass;
		entityClass = (Class<E>) parameterized.getActualTypeArguments()[0];
		return entityClass;
	}

	protected Query<E> createQuery() {
		return datastore.createQuery(getEntityClass());
	}

	protected UpdateOperations<E> createUpdateOptions() {
		return datastore.createUpdateOperations(getEntityClass());
	}

	protected Key<E> createKey(I id) {
		return new Key<E>(this.getEntityClass(), id);
	}

	protected E getByProperty(String property, Object value) {
		return datastore.find(getEntityClass(), property, value).get();
	}

	protected List<E> findByProperty(String property, Object value) {
		return datastore.find(getEntityClass(), property, value).asList();
	}

	protected List<E> findByProperty(String property, Object value, int offset, int limit) {
		return datastore.find(getEntityClass(), property, value).offset(offset).limit(limit).asList();
	}

	protected void update(I id, UpdateOperations<E> ops) {
		datastore.update(this.createQuery().field(Mapper.ID_KEY).equal(id), ops);
	}

	protected void update(Query<E> qry, UpdateOperations<E> ops) {
		datastore.update(qry, ops);
	}

	/**
	 * 更新数据库中的现有字段，并添加entity中的非null字段（如果数据库中没有这个字段）。
	 * 值是null的字段不会被更新。
	 */
	@Override
	public void updateAndAppend(E entity) {
		datastore.merge(entity);
	}

	@Override
	public List<E> getByIds(Collection<I> ids) {
		if (ids == null || ids.size() == 0) {
			return null;
		}
		List<Key<E>> keys = new ArrayList<Key<E>>();
		for (I id : ids) {
			keys.add(new Key<E>(getEntityClass(), id));
		}
		return datastore.getByKeys(keys);
	}

	@SuppressWarnings("unchecked")
	protected Map<I, Object> queryProperty(Collection<I> ids, String property) {
		DBObject qry = new BasicDBObject(Mapper.ID_KEY, new BasicDBObject("$in", ids));
		DBObject retrieve = new BasicDBObject(property, true);
		DBCursor cursor = this.getDatastore().getCollection(getEntityClass()).find(qry, retrieve);
		Map<I, Object> result = new HashMap<I, Object>();
		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			Object key = dbo.get(Mapper.ID_KEY);
			Object value = dbo.get(property);
			result.put((I) key, value);
		}
		return result;
	}

	protected Object queryProperty(I id, String property) {
		DBObject qry = new BasicDBObject(Mapper.ID_KEY, id);
		DBObject retrieve = new BasicDBObject(property, true);
		DBObject found = this.getDatastore().getCollection(getEntityClass()).findOne(qry, retrieve);
		if (found == null) {
			return null;
		}
		return found.get(property);
	}

	/**
	 * 分组统计个数。相当于SQL中的 select count(*) from <b>Collection</b> group by
	 * <b>groupBy</b> where ...
	 *
	 * @param groupBy
	 *            分组子段，相当于SQL中的 group by
	 * @param conditions
	 *            查询条件，如：{ "name" : "=someName" }
	 * @return 分组个数统计结果：{ property : count }
	 */
	protected Map<Object, Integer> countGroup(String groupBy, Map<String, Object> conditions) {
		DBObject keys = new BasicDBObject(groupBy, true);
		DBObject cond = new BasicDBObject();
		if (conditions != null && conditions.size() > 0) {
			for (Entry<String, Object> ety : conditions.entrySet()) {
				cond.put(ety.getKey(), ety.getValue());
			}
		}
		DBObject initial = new BasicDBObject("count", 0);
		String reduce = "function(cur,result){ result.count += 1; }";
		DBObject group = getDatastore().getCollection(getEntityClass()).group(keys, cond, initial, reduce);

		BasicDBList dbList = (BasicDBList) group;
		Map<Object, Integer> result = new HashMap<Object, Integer>();
		for (Object e : dbList) {
			DBObject dbo = (DBObject) e;
			result.put(dbo.get(groupBy), Double.valueOf(dbo.get("count").toString()).intValue());
		}
		return result;
	}

	@Override
	public long getCount(Query<E> query) {
		return query.countAll();
	}

}
