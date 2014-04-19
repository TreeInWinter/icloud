package com.icloud.framework.core.exception;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.icloud.framework.core.constant.HConstants;

public class SException extends RuntimeException {

	private static final long serialVersionUID = 430305575267731710L;
	private String errorMessage = "";
	private int errorCode;
	private Object[] objects;

	public SException(int errorCode, String errorMessage, Throwable thr) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.initCause(thr);
	}

	public SException(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public SException(int errorCode) {
		this.errorCode = errorCode;
	}

	public SException(Throwable thr, Object... objects) {
		this(HConstants.ZERO, "", thr, objects);
	}

	public SException(int errorCode, Throwable thr, Object... objects) {
		this(errorCode, "", thr, objects);
	}

	public SException(int errorCode, String errorMessage, Throwable thr, Object... objects) {
		super(String.format("[retCode=%serrorMessage=%s]", errorCode, errorMessage));
		this.errorCode = errorCode;
		this.objects = objects;
		this.errorMessage = errorMessage;
		this.initCause(thr);
	}

	public SException(int errorCode, String errorMessage, Object... objects) {
		super(String.format("[retCode=%s,errorMessage=%s]", errorCode, errorMessage));
		this.errorCode = errorCode;
		this.objects = objects;
		this.errorMessage = errorMessage;
	}
	

	public SException(String errorMessage, Object... objects) {
		super(String.format("[errorMessage=%s]", errorMessage));
		this.errorMessage = errorMessage;
		this.objects = objects;
	}

 

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public Object[] getObjects() {
		return objects;
	}

	public void setObjects(Object[] objects) {
		this.objects = objects;
	}

	@Override
	public String toString() {
		return String.format("[retCode=%s,errorMessage=%s]", errorCode, errorMessage);
	}

	@Override
	public String getMessage() {
		return String.format("%s", Arrays.deepToString(objects));
	}

	public String getMessage(String format, String separator) {
		return String.format(format, StringUtils.join(objects, separator));
	}

	public String getMessage(String format) {
		return String.format(format, objects);
	}

}
