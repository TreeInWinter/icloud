package com.travelzen.framework.core.dict;

public enum AdjustmentPageOrderState {
	init("新建调账"),
	modify_money("修改金额"),
	commit("提交申请"),
    review_success("审核通过"),
    adjustment_complete("调账完成"),
	returned("退回");

	private String desc;

	private AdjustmentPageOrderState(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
