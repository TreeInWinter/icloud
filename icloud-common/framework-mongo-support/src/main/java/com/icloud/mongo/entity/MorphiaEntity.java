package com.icloud.mongo.entity;


public interface MorphiaEntity<I> {

	I getId();

	void setId(I id);

	void setId(String id);

}
