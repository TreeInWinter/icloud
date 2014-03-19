package com.travelzen.framework.core.dict;

public enum TravelRecycleType {
	notInvalid("未作废"),
	notRecycle("未回收"),
	recycled("已回收")
	;
	private String state;
	
	private TravelRecycleType(String state) {
		this.state = state;
	}
	
	public String getDesc() {
		return state;
	}

	public static TravelRecycleType getEnum(String text) {
		switch (text) {
			case "未作废":
				return TravelRecycleType.notInvalid;
			case "未回收":
				return TravelRecycleType.notRecycle;
			case "已回收":
				return TravelRecycleType.recycled;
			default:
				return null;
		}
	}
}
