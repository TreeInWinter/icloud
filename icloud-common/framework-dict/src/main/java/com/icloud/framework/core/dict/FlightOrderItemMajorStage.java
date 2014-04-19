package com.icloud.framework.core.dict;

public enum FlightOrderItemMajorStage {
	init("创建"), review("审核"), pay("支付"), issue_ticket("出票"), delivery("派送"), complete("完成"), cancel("取消");

	private String desc;

	private FlightOrderItemMajorStage(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
