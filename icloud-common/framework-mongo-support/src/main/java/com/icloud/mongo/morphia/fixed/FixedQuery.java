package com.icloud.mongo.morphia.fixed;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.query.CriteriaContainerImpl;
import com.github.jmkgreen.morphia.query.CriteriaJoin;
import com.github.jmkgreen.morphia.query.FieldEnd;
import com.github.jmkgreen.morphia.query.Query;
import com.github.jmkgreen.morphia.query.QueryImpl;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * 用来支持FixedFieldEnd，以解决一些查询问题 
 * @author chengwenlee
 */
public class FixedQuery <T> extends QueryImpl<T> {

	public FixedQuery(Class<T> clazz, DBCollection coll, Datastore ds, DBObject baseQuery) {
		super(clazz, coll, ds, baseQuery);
	}

	public FixedQuery(Class<T> clazz, DBCollection coll, Datastore ds, int offset, int limit) {
		super(clazz, coll, ds, offset, limit);
	}

	public FixedQuery(Class<T> clazz, DBCollection coll, Datastore ds) {
		super(clazz, coll, ds);
	}

	@Override
	public FieldEnd<? extends Query<T>> field(String name) {
		return new FixedFieldEnd<FixedQuery<T>>(this, name, this, this.isValidatingNames());
	}

	@Override
	public FieldEnd<? extends CriteriaContainerImpl> criteria(String field) {
        FixedCriteriaContainer container = new FixedCriteriaContainer(this, CriteriaJoin.AND);
        this.add(container);

        return new FixedFieldEnd<FixedCriteriaContainer>(this, field, container, this.isValidatingNames());
	}

}
