package com.travelzen.framework.dao.mongo.entity;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

/**
 * 共通信息.
 * 
 */
public abstract class CommonEntityEmbedded implements Serializable {
	private static final long serialVersionUID = -5637643848707876855L;

	private String id;
	private ObjectId userId;
	private ObjectId createdBy;
	private ObjectId updatedBy;
	private DateTime created;
	private DateTime updated;

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

	public ObjectId getUserId() {
		return userId;
	}

	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommonEntityEmbedded other = (CommonEntityEmbedded) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
