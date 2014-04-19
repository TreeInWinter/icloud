package com.icloud.framework.dao.mongo.entity;


public class MDStoredSeqence extends CommonEntity {

	String collName;

	Long  value;

	public MDStoredSeqence() {

	}

	public MDStoredSeqence(String collName) {
		this.collName = collName;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public String getCollName() {
		return collName;
	}

	public void setCollName(String collName) {
		this.collName = collName;
	}

}
