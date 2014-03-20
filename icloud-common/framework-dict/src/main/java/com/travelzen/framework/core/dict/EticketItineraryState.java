/**
 * 
 * Description: 
 * Copyright (c) 2013
 * Company:真旅网
 * @author renshui
 * @version 1.0
 * @date 2013-8-14
 */
package com.travelzen.framework.core.dict;

public enum EticketItineraryState {
	voided("已作废"),
	used("已使用");
	private String desc;

	private EticketItineraryState(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
