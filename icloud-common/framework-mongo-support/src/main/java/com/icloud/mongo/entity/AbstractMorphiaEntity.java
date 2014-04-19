package com.icloud.mongo.entity;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Id;

public abstract class AbstractMorphiaEntity implements MorphiaEntity<ObjectId> {

	@Id
	private ObjectId id;

	@Override
	public ObjectId getId() {
		return this.id;
	}

	@Override
	public void setId(ObjectId id) {
		this.id = id;
	}

	@Override
	public void setId(String id) {
		this.id = new ObjectId(id);
	}

}
