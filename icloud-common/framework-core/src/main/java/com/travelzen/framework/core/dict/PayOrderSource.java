package com.travelzen.framework.core.dict;

public enum PayOrderSource {
	ORDER("O"), FINANCE("F");
	private String value;
	private PayOrderSource(String p_value){
		this.value = p_value;
	}
	public String getValue() {
		return value;
	}
}
