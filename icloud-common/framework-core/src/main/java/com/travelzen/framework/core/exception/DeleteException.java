package com.travelzen.framework.core.exception;

/**
 * @Description:基础Service实现类中删除失败所抛出的异常
 * @version 1.0
 */
public class DeleteException extends RuntimeException {

	private static final long serialVersionUID = -1118116339896988507L;
	
	public DeleteException() {
		super();
	}
	
	public DeleteException(String msg) {
		super(msg);
	}

}
