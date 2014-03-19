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

public enum SupplierPayType {
	ALIPAY("支付宝"), 
	PRESTORE("预存余额"),
	CREDIT("授信"),
	AUTO("自动");
    private String desc;
    private SupplierPayType(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
