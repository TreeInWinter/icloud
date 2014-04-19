package com.icloud.framework.core.common;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

// process return object and code,   and error format 
public class SReturnClass<T> {
	private int status = 0;

	private T object = null;
	private Object[] statusObjects = new Object[0];

	public SReturnClass() {
	}

	public SReturnClass(int sStatus) {
		setStatus(sStatus);
	}

	public SReturnClass(int sStatus, Object[] objects) {
		setStatus(sStatus, objects);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int sStatus) {
		this.status = sStatus;
	}

	public void setStatus(int sStatus, Object[] objects) {
		this.status = sStatus;
		this.statusObjects = objects;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public String getMessage() {
		return String.format("%s", Arrays.deepToString(statusObjects));
	}

	public String getMessage(String format, String separator) {
		return String.format(format, StringUtils.join(statusObjects, separator));
	}

	public String getMessage(String format) {
		return String.format(format, statusObjects);
	}

	public boolean isSuccess() {
		return status == 0;
	}

	public boolean isError() {
		return status >  0;
	}

	public Object[] getStatusObjects() {
		return statusObjects;
	}

	public void setStatusObjects(Object[] statusObjects) {
		this.statusObjects = statusObjects;
	}

	public String toString() {

		return ReflectionToStringBuilder.toString(this);

	}
}