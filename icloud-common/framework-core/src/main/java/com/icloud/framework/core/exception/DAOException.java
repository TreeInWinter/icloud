package com.icloud.framework.core.exception;

import java.sql.SQLException;

import com.icloud.framework.core.common.ReturnClass;

public class DAOException extends SQLException {
	private static final long serialVersionUID = 7450480424769236320L;
		ReturnClass returnClass;

	public DAOException() {
	}

	public DAOException(String message) {
		super(message);
	}
	
	public DAOException(Throwable throwable) {
		super(throwable);
	}

	public DAOException(ReturnClass returnClass) {
		this.returnClass = returnClass;
	}

	public DAOException(String message, Throwable throwable){
		super(message, throwable);
	}

	public DAOException(String message, Throwable throwable,
			ReturnClass returnClass) {
		super(message, throwable);
		this.returnClass = returnClass;
	}
}