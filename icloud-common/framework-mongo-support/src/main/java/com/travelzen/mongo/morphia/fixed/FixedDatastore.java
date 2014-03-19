package com.travelzen.mongo.morphia.fixed;

import com.github.jmkgreen.morphia.DatastoreImpl;
import com.github.jmkgreen.morphia.Morphia;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.github.jmkgreen.morphia.query.Query;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

/**
 * 用来支持FixedQuery，以解决一些查询问题 
 * @author chengwenlee
 */
public class FixedDatastore extends DatastoreImpl {

	public FixedDatastore(Mapper mapr, Mongo mongo, String dbName) {
		super(mapr, mongo, dbName);
	}

	public FixedDatastore(Morphia morphia, Mongo mongo, String dbName, String username, char[] password) {
		super(morphia, mongo, dbName, username, password);
	}

	public FixedDatastore(Morphia morphia, Mongo mongo, String dbName) {
		super(morphia, mongo, dbName);
	}

	public FixedDatastore(Morphia morphia, Mongo mongo) {
		super(morphia, mongo);
	}

	@Override
	public <T> Query<T> createQuery(Class<T> clazz) {
		return new FixedQuery<T>(clazz, getCollection(clazz), this);
	}

	@Override
	public <T> Query<T> createQuery(Class<T> kind, DBObject q) {
		return new FixedQuery<T>(kind, getCollection(kind), this, q);
	}

	@Override
	public <T> Query<T> createQuery(String kind, Class<T> clazz, DBObject q) {
		return new FixedQuery<T>(clazz, db.getCollection(kind), this, q);
	}

	@Override
	public <T> Query<T> createQuery(String kind, Class<T> clazz) {
		return new FixedQuery<T>(clazz, db.getCollection(kind), this);
	}

}
