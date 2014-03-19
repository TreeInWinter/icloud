/**
 * 
 * Description: 
 * Copyright (c) 2013
 * Company:真旅网
 * @author renshui
 * @version 1.0
 * @date 2013-5-28
 */
package com.travelzen.framework.core.dict;

public enum GatheringType {
    account("账户支付");
    private String desc;
    private GatheringType(String desc){
	this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
