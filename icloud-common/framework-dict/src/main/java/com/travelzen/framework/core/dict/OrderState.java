package com.travelzen.framework.core.dict;

public enum OrderState {
	init("创建"), cancel("取消");

	private String desc;

	private OrderState(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
