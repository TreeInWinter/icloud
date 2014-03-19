package com.travelzen.framework.core.dict;

public enum EndorseExecutor {
	airline("提交航司改签"), 
	self("提交我司改签");

	private String desc;

	private EndorseExecutor(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
