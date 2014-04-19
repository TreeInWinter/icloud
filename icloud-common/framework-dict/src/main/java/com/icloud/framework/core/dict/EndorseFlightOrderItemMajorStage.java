package com.icloud.framework.core.dict;

public enum EndorseFlightOrderItemMajorStage {
	init("创建"), review("审核"), endorse("改签"), complete("完成"), cancel("取消");

	private String desc;

	private EndorseFlightOrderItemMajorStage(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
