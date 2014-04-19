package com.icloud.framework.core.dict;

public enum FlightOrderItemFlowState {
	processing("处理中"), idle("等待处理");

	private String desc;

	private FlightOrderItemFlowState(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
