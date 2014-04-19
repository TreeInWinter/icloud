package com.icloud.framework.core.exception;

public class ActionException extends Exception
{
	private static final long serialVersionUID = 7450480424769236320L;

	public ActionException()
	{
	}

	
	public ActionException(String message)
	{
		super(message);
	}

	public ActionException(Throwable throwable)
	{
		super(throwable);
	}

	public ActionException(String message, Throwable throwable)
	{
		super(message, throwable);
	}
}