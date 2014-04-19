package com.icloud.mongo.dao;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.query.Query;
import com.icloud.mongo.dao.impl.MorphiaBasicDao;
import com.icloud.mongo.entity.Foo;

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
