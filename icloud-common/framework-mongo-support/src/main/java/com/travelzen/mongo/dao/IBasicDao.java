package com.travelzen.mongo.dao;

import java.util.Collection;
import java.util.List;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.query.Query;
import com.mongodb.WriteResult;

public interface IBasicDao<E, I> {

	void setDatastore(Datastore ds);

	I create(E entity);

	I createOrReplace(E entity);

	WriteResult deleteById(I id);

	E getById(I id);

	boolean isExists(I id);

	void updateAndAppend(E entity);

	List<E> getByIds(Collection<I> ids);

	long getCount(Query<E> query);

}
