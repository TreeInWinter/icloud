package com.travelzen.framework.core.dict;

public enum AutoState {
	ISSUE_ING("出票中"), ISSUE_FAIL("出票失败"), ISSUE_SUCCESS("出票成功");
	private String desc;

	private AutoState(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
