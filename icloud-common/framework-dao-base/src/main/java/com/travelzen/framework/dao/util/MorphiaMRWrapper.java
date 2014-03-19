package com.travelzen.framework.dao.util;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.DatastoreImpl;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.github.jmkgreen.morphia.mapping.cache.EntityCache;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.travelzen.framework.config.NamedItemConfHolder;

public class MorphiaMRWrapper {

	private Datastore ds;
	private DB db;
	private Mapper mapper;
	private EntityCache cache;

	private DBCollection collection;

	private Class<?> entityClazz;
	private String mapFunction;
	private String reduceFunction;

	public MorphiaMRWrapper(Datastore ds, Class<?> cls) {

		this.ds = ds;
		db = ds.getDB();

		DatastoreImpl dbImpl = (DatastoreImpl) ds;
		mapper = dbImpl.getMapper();

		cache = mapper.createEntityCache();

		entityClazz = (Class<?>) ((ParameterizedType) cls
				.getGenericSuperclass()).getActualTypeArguments()[0];

		collection = db.getCollection(entityClazz.getSimpleName());

		NamedItemConfHolder itemMap = NamedItemConfHolder
				.getInstance("/morphia/loginfo.xml");

		Map<String, String> realItemMap = new HashMap<String, String>();
		realItemMap.putAll(itemMap.namedItemMap);
		for (String key : itemMap.namedItemMap.keySet()) {

			String newkey = key.replace("_map", "PG_map");
			newkey = newkey.replace("_reduce", "PG_reduce");
			realItemMap.put(newkey, itemMap.namedItemMap.get(key));
		}
		itemMap.namedItemMap = realItemMap;

		this.setMapFunction(itemMap.getItem(entityClazz.getSimpleName()
				+ "_map"));
		this.setReduceFunction(itemMap.getItem(entityClazz.getSimpleName()
				+ "_reduce"));

	}

	public Datastore getDs() {
		return ds;
	}

	public void setDs(Datastore ds) {
		this.ds = ds;
	}

	public DB getDb() {
		return db;
	}

	public void setDb(DB db) {
		this.db = db;
	}

	public Mapper getMapper() {
		return mapper;
	}

	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	public EntityCache getCache() {
		return cache;
	}

	public void setCache(EntityCache cache) {
		this.cache = cache;
	}

	public DBCollection getCollection() {
		return collection;
	}

	public void setCollection(DBCollection collection) {
		this.collection = collection;
	}

	public String getMapFunction() {
		return mapFunction;
	}

	public void setMapFunction(String mapFunction) {
		this.mapFunction = mapFunction;
	}

	public String getReduceFunction() {
		return reduceFunction;
	}

	public void setReduceFunction(String reduceFunction) {
		this.reduceFunction = reduceFunction;
	}

	public Class<?> getEntityClazz() {
		return entityClazz;
	}

	public void setEntityClazz(Class<?> entityClazz) {
		this.entityClazz = entityClazz;
	}

}
