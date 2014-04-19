package com.icloud.framework.core.exception;

public class IbatisException extends RuntimeException
{
	private static final long serialVersionUID = -7454062778703823651L;

	public IbatisException()
	{
		super();
	}

	public IbatisException(String message)
	{
		super(message);
	}

	public IbatisException(String message, Throwable cause)
	{
		super(message, cause);
	}
}