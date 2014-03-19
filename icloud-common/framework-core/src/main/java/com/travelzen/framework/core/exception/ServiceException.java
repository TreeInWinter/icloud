package com.travelzen.framework.core.exception;

import com.travelzen.framework.core.common.ReturnClass;


public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 7758476083917374792L;

	ReturnClass returnClass;
	
	public ServiceException() {
	}

	public ServiceException(ReturnClass returnClass) {
		this.returnClass = returnClass;
	}
	
	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable throwable) {
		super(throwable);
	}

	public ServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public ServiceException(String message, Throwable throwable,ReturnClass returnClass) {
		super(message, throwable);
		this.returnClass = returnClass;
	}
}