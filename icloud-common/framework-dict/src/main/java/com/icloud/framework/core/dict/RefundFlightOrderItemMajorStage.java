package com.icloud.framework.core.dict;

public enum RefundFlightOrderItemMajorStage {
	init("创建"), review("审核"), refund("退票"), complete("完成"), cancel("取消");

	private String desc;

	private RefundFlightOrderItemMajorStage(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
