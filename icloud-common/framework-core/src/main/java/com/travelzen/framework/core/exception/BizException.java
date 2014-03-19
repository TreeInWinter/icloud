package com.travelzen.framework.core.exception;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.travelzen.framework.core.common.ReturnCode;

/**
 *   instance 函数构造的 BizException 中 设置的是 retMsg 成员变量
 *     BizException 构造函数中的  Object 数组设置的是 objects 成员变量
 */

public class BizException extends RuntimeException {

    private static final long serialVersionUID = 430305575267731710L;
    private String retMsg = "";
    private ReturnCode retCode;
    private Object[] objects;

    
	public static BizException instance(String retMsg) {
		return new BizException(ReturnCode.ERROR, retMsg);
	}
	
    public static BizException instance(ReturnCode retCode, String retMsg) {
        return new BizException(retCode, retMsg);
    }

    public static BizException instance(ReturnCode retCode, String retMsg, Throwable thr) {
        return new BizException(retCode, retMsg, thr);
    }

    public BizException(Throwable thr, Object... objects) {
        this(ReturnCode.ERROR, "", thr, objects);
    }

    public BizException(ReturnCode retCode, Throwable thr, Object... objects) {
        this(retCode, "", thr, objects);
    }

    public BizException(ReturnCode retCode, String retMsg, Throwable thr, Object... objects) {
        super(String.format("[retCode=%s,retMsg=%s]", retCode.getErrorCode(), retMsg));
        this.retCode = retCode;
        this.objects = objects;
        this.retMsg = retMsg;
        this.initCause(thr);
    }

    public BizException(Object... objects) {
        this(ReturnCode.ERROR, "", objects);
    }

    public BizException(ReturnCode retCode, Object... objects) {
        this(retCode, "", objects);
    }

    public BizException(ReturnCode retCode, String retMsg, Object... objects) {
        super(String.format("[retCode=%s,retMsg=%s]", retCode.getErrorCode(), retMsg));
        this.retCode = retCode;
        this.objects = objects;
        this.retMsg = retMsg;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public ReturnCode getRetCode() {
        return retCode;
    }

    @Override
    public String toString() {
        return String.format("[retCode=%s,retMsg=%s]", retCode.getErrorCode(), retMsg);
    }

    @Override
    public String getMessage() {
        return String.format("[retCode=%s,retMsg=%s, objects=%s]", this.retCode, this.retMsg, Arrays.deepToString(objects));
    }

    public String getMessage(String format, String separator) {
        return String.format(format, StringUtils.join(objects, separator));
    }

    public String getMessage(String format) {
        return String.format(format, objects);
    }
    /**
     * 从thr中提取BizException,若提取不到返回thr
     * @param thr
     * @return
     */
    public static Exception unwrap(Exception e){
    	Throwable cause = e.getCause();
		if(cause != null && cause instanceof BizException)
			return (Exception)cause;
		return e;
    }
    /**
     * 获取原始的消息
     * @param e
     * @return
     */
    public static String getRawMsg(Exception e){
    	if(e == null)
    		return null;
    	e = unwrap(e);
    	if(e instanceof BizException){
    		BizException bizException = (BizException)e;
    		return bizException.getRetMsg();
    	}else return e.getMessage();
    }
}
