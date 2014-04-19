/**
 * 机票预订方式
 * Description: 
 * Copyright (c) 2013
 * Company:真旅网
 * @author renshui
 * @version 1.0
 * @date 2013-6-28
 */
package com.icloud.framework.core.dict;

public enum FlightOrderBookingType {
	
	pnr_import("后台黑屏导入"),
	white_screen("白屏预订"),
	;
	
	private String desc;
	
	private FlightOrderBookingType(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
