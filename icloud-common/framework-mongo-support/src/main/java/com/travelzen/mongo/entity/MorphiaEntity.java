package com.travelzen.mongo.entity;


public interface MorphiaEntity<I> {

	I getId();

	void setId(I id);

	void setId(String id);

}
