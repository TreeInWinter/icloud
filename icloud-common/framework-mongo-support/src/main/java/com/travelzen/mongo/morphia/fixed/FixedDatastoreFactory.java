package com.travelzen.mongo.morphia.fixed;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.DatastoreFactory;
import com.github.jmkgreen.morphia.Morphia;
import com.mongodb.Mongo;

/**
 * 用来支持FixedDatastore，以解决一些查询问题 
 * @author chengwenlee
 */
public class FixedDatastoreFactory implements DatastoreFactory {

	@Override
	public Datastore create(Morphia morphia, Mongo mongo, String dbName, String username, char[] password) {
		return new FixedDatastore(morphia, mongo,  dbName, username, password);
	}

}
