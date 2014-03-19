package com.travelzen.framework.core.exception;

/**
 * @Description:基础Service实现类中更新失败所抛出的异常
 * @author  liangwang
 * @version 1.0
 * @created Feb 11, 2009
 */
public class UpdateException extends RuntimeException {

	private static final long serialVersionUID = 2828231233360644674L;
	
	public UpdateException() {
		super();
	}
	
	public UpdateException(String msg) {
		super(msg);
	}

}
