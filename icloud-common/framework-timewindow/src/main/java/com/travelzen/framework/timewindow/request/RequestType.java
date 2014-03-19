package com.travelzen.framework.timewindow.request;

public enum RequestType {
	
	ADD,
	EVICT,
	
	GET_RESULT,
	
	GET_LAST_FRAME_RESULT,
	
	GET_ALL_FRAME_RESULT, 
	
	GET_FRAME_RESULT_AT_TIME,
	
	START_WATCH,
	STOP_WATCH,
	MAX_LATENCY,
	AVERAGE_LATENCY,
}
