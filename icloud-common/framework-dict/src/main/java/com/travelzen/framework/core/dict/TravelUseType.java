package com.travelzen.framework.core.dict;

public enum TravelUseType {
	notUse("未使用"),
	used("已使用"),
	freeze("已冻结"),
	invalid("已作废"),
	handDelete("手动销号"),
	autoDelete("自动销号");
	private String state;

	private TravelUseType(String state) {
		this.state = state;
	}

	public String getDesc() {
		return state;
	}

	public static TravelUseType getEnum(String text) {
		switch (text) {
			case "未使用":
				return TravelUseType.notUse;
			case "已使用":
				return TravelUseType.used;
			case "已冻结":
				return TravelUseType.freeze;
			case "已作废":
				return TravelUseType.invalid;
			case "手动销号":
				return TravelUseType.handDelete;
			case "自动销号":
				return TravelUseType.autoDelete;
			default:
				return null;
		}
	}
}
