package com.icloud.framework.dao.mongo.entity;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import com.github.jmkgreen.morphia.annotations.Entity;

/**
 * 共通信息.
 */
@Entity
public class CommonEntity extends IdEntity {
	private ObjectId userId;

	private ObjectId createdBy;
	private ObjectId updatedBy;
	private DateTime created;
	private DateTime updated;

	private boolean isActive;

	public ObjectId getUserId() {
		return userId;
	}
	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}
	public ObjectId getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(ObjectId createdBy) {
		this.createdBy = createdBy;
	}
	public ObjectId getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(ObjectId updatedBy) {
		this.updatedBy = updatedBy;
	}
 
	public DateTime getCreated() {
		return created;
	}
	public void setCreated(DateTime created) {
		this.created = created;
	}
	public DateTime getUpdated() {
		return updated;
	}
	public void setUpdated(DateTime updated) {
		this.updated = updated;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public int simpleHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isActive ? 1231 : 1237);
		result = prime * result + ((updated == null) ? 0 : updated.hashCode());
		return result;
	}
}
