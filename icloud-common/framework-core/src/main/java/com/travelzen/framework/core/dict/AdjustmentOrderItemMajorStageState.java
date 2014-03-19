package com.travelzen.framework.core.dict;

public enum AdjustmentOrderItemMajorStageState {
	not_begin("未被处理"), processing("处理中"), complete("完成"), exception("异常"),cancel("取消");
	
	private String desc;

	private AdjustmentOrderItemMajorStageState(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	
}
