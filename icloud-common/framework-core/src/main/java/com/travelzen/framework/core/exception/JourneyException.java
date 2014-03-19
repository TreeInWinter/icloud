package com.travelzen.framework.core.exception;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.travelzen.framework.core.common.ReturnCode;

/**
 * service统一转换抛出此异常
 * 非运行时异常，要求控制层必须处理
 * @author zhengzhichao
 *
 */
public class JourneyException extends Exception {

    private static final long serialVersionUID = 430305575267731710L;
    private String retMsg = "";
    private ReturnCode retCode;
    private Object[] objects;
    
	public static JourneyException instance(String retMsg) {
		return new JourneyException(ReturnCode.ERROR,retMsg,null,new Object[0]);
	}
	
	public static JourneyException instance(String retMsg,Throwable thr) {
		return new JourneyException(ReturnCode.ERROR,retMsg,thr,new Object[0]);
	}
	
    public static JourneyException instance(ReturnCode retCode, String retMsg) {
        return new JourneyException(retCode,retMsg,null,new Object[0]);
    }

    public static JourneyException instance(ReturnCode retCode, String retMsg, Throwable thr) {
        return new JourneyException(retCode, retMsg, thr, new Object[0]);
    }

    public JourneyException(ReturnCode retCode, String retMsg, Throwable thr, Object... objects) {
        super(String.format("[retCode=%s,retMsg=%s]", retCode.getErrorCode(), retMsg));
        this.retCode = retCode;
        this.objects = objects;
        this.retMsg = retMsg;
        this.initCause(thr);
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

}
