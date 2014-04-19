package com.icloud.mongo.morphia.fixed;

import com.github.jmkgreen.morphia.query.CriteriaContainer;
import com.github.jmkgreen.morphia.query.CriteriaContainerImpl;
import com.github.jmkgreen.morphia.query.CriteriaJoin;
import com.github.jmkgreen.morphia.query.FieldEnd;
import com.github.jmkgreen.morphia.query.QueryImpl;

/**
 * 用来支持FixedFieldEnd，以解决一些查询问题 
 * @author chengwenlee
 */
public class FixedCriteriaContainer extends CriteriaContainerImpl {

	public FixedCriteriaContainer(CriteriaJoin joinMethod) {
		super(joinMethod);
	}

	public FixedCriteriaContainer(QueryImpl<?> query, CriteriaJoin joinMethod) {
		super(query, joinMethod);
	}

	@Override
	public FieldEnd<? extends CriteriaContainer> criteria(String name) {
		return new FixedFieldEnd<FixedCriteriaContainer>(this.query, name, this, this.query.isValidatingNames());
	}

}
