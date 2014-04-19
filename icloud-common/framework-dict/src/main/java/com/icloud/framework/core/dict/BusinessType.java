/**
 * 
 * Description: 
 * Copyright (c) 2013
 * Company:真旅网
 * @author renshui
 * @version 1.0
 * @date 2013-9-4
 */
package com.icloud.framework.core.dict;

public enum BusinessType {
	normal("普通询价"),
	special("特价申请"),
	group("团队申请");
	private String desc;
	private BusinessType(String desc) {
        this.desc = desc;
    }
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
