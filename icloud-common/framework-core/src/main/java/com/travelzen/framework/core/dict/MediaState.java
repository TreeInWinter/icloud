package com.travelzen.framework.core.dict;

public enum MediaState {

	OK(0),
	
	NONE(1001),
	
	NOT_EXIST(1002),
	
	BAD_PARAMETER(1003),
	
	NOT_GET_MEDIA(1004),
	
	SERVER_ERROR(1005);
	
	private int value;
	
	MediaState(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}

}
