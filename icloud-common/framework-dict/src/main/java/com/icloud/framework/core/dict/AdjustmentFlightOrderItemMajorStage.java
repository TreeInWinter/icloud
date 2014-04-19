package com.icloud.framework.core.dict;

public enum AdjustmentFlightOrderItemMajorStage {
	init("创建"), review("审核"),pay("支付"), adjustment("调账"), cancel("取消");
	
	private String desc;

	private AdjustmentFlightOrderItemMajorStage(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
