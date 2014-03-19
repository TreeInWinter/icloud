package com.travelzen.mongo.dao;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.query.Query;
import com.travelzen.mongo.dao.impl.MorphiaBasicDao;
import com.travelzen.mongo.entity.Foo;

public class FooDao extends MorphiaBasicDao<Foo, ObjectId> {

	public Foo getOne() {
		return super.createQuery().get();
	}

	@Override
	public long getCount(Query<Foo> query) {
		// TODO Auto-generated method stub
		return 0;
	}

}
