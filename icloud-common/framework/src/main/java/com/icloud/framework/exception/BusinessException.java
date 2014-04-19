/**
 * 
 * Description: 
 * Copyright: Copyright (c) 2009
 * Company:云壤
 * @author 任水
 * @version 1.0
 * @date Dec 21, 2011
 */
package com.icloud.framework.exception;

import com.icloud.framework.config.PropertiesUtil;
import com.icloud.framework.core.util.StringUtil;

public class BusinessException extends Exception{
    /**
     * 
     */
    private static final long serialVersionUID = 430305575267731710L;
    private String retCode;
    private String retMsg;
    
    public static BusinessException instance(String retCode, String retMsg){
    	 if(StringUtil.isTrimEmpty(retMsg))
             retMsg = PropertiesUtil.getValue(retCode);
    	 return new BusinessException(retCode,retMsg,null);
    }
    
    public static BusinessException instance(String retCode){
    	String retMsg = PropertiesUtil.getValue(retCode);
     	return new BusinessException(retCode,retMsg,null);
    }
    public static BusinessException instance(String retCode, String retMsg, Throwable thr){
   	 if(StringUtil.isTrimEmpty(retMsg))
            retMsg = PropertiesUtil.getValue(retCode);
   	 return new BusinessException(retCode,retMsg,thr);
   }
    public static BusinessException instance(String retCode, Throwable thr){
         String retMsg = PropertiesUtil.getValue(retCode);
      	 return new BusinessException(retCode,retMsg,thr);
      }
//    private BusinessException(String retCode, String retMsg){
//    	super("[retCode=" + retCode + ", retMsg=" + retMsg + "]");
//        this.retCode = retCode;
//        this.retMsg = retMsg;
//    }
//    private BusinessException(String retCode){
//        this(retCode, "");
//    }
//    private BusinessException(String retCode, Throwable tht){
//        this(retCode,null, tht);
//    }
    private BusinessException(String retCode, String retMsg, Throwable thr){
    	super("[retCode=" + retCode + ", retMsg=" + retMsg + "]");
        this.retCode = retCode;
        this.retMsg = retMsg;
        this.initCause(thr);
    }
    
    public String getRetCode() {
        return retCode;
    }
 
    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    
    public String getRetMsg() {
        return retMsg;
    }

    
    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }
    @Override
    public String toString(){
        return "[retCode=" + retCode + ", retMsg=" + retMsg + "]";
    }

}

