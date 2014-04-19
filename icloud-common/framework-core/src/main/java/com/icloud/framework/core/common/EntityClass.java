package com.icloud.framework.core.common;

import java.io.Serializable;
import java.util.Date;

public abstract class EntityClass extends RootClass implements Serializable
{
	
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	private static final long serialVersionUID = -2280935910217787417L;
	
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
 
	public String getUpdatedby() {
		return updatedby;
	}
	public void setUpdatedby(String updatedby) {
		this.updatedby = updatedby;
	}
	public Date created;
	public String createdby;
	public String isActive;
	public Date updated;
	public String updatedby;
 
	  
}