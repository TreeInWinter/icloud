/**
 * 机票预订使用的系统
 * Description: 
 * Copyright (c) 2013
 * Company:真旅网
 * @author renshui
 * @version 1.0
 * @date 2013-6-28
 */
package com.icloud.framework.core.dict;

public enum FlightOrderBookingSystem {
	
	purchaser("采购商"),
	operator("运营商"),
	api("接口"),
	;
	
	private String desc;
	
	private FlightOrderBookingSystem(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
