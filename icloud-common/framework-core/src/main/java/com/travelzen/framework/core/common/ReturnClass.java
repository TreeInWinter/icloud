package com.travelzen.framework.core.common;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ReturnClass<T> {
	private ReturnCode status = ReturnCode.SUCCESS;

	private T object = null;
	private Object[] objects = null;

	public ReturnClass() {
	}

	public ReturnClass(ReturnCode sStatus) {
		setStatus(sStatus);
	}

	public ReturnClass(ReturnCode sStatus, Object[] objects) {
		setStatus(sStatus, objects);
	}

	public Object[] getObjects() {
		return objects;
	}

	public void setObjects(Object[] objects) {
		this.objects = objects;
	}

	public ReturnCode getStatus() {
		return status;
	}

	public void setStatus(ReturnCode sStatus) {
		this.status = sStatus;
	}

	public void setStatus(ReturnCode sStatus, Object[] objects) {
		this.status = sStatus;
		this.objects = objects;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public void setObjectAndSuccess(T object) {
		this.object = object;
		this.status = ReturnCode.SUCCESS;
	}

	public String getMessage() {
		return String.format("%s", Arrays.deepToString(objects));
	}

	public String getMessage(String format, String separator) {
		return String.format(format, StringUtils.join(objects, separator));
	}

	public String getMessage(String format) {
		return String.format(format, objects);
	}

	public boolean isSuccess() {
		return StringUtils.startsWith(status.getErrorCode(), "S");
	}

	public boolean isError() {
		return StringUtils.startsWith(status.getErrorCode(), "E");
	}

	public String toString() {

		return ReflectionToStringBuilder.toString(this);

	}
}