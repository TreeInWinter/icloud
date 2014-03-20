package com.travelzen.framework.core.dict;

public enum FlightOrderItemMajorStageState {
	not_begin("未被处理"), processing("处理中"), complete("完成"), exception("异常");

	private String desc;

	private FlightOrderItemMajorStageState(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
