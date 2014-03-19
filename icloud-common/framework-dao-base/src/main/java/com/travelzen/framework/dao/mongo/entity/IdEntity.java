package com.travelzen.framework.dao.mongo.entity;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.travelzen.framework.dao.annotation.ShowInList;

@Entity
public abstract class IdEntity {
	@Id private ObjectId id;

	@ShowInList
	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
}
