package com.icloud.framework.core.dict;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public enum CancelPnrStatus {
	cancelByHelp("1", "系统帮我取消，或请平台客服帮我取消座位"),
	keepSeat("2", "请保留座位"), 
	seatCanceled("3", "座位已取消"), 
	cancelBeforeDeparture("4", "起飞前取消"), 
	cancelAfterDeparture("5", "起飞后取消"),

	;
	private String value;
	private String key;

	private CancelPnrStatus(String key, String value) {
		this.value = value;
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	public static List<CancelPnrStatus> getCancelPnrStatusList(String orderType){
    	List<CancelPnrStatus> cancelPnrStatusList = new ArrayList<CancelPnrStatus>();
    	orderType = StringUtils.trimToEmpty(orderType);
    	if("FP".equals(orderType)){
    		cancelPnrStatusList.add(keepSeat);
    	}else{
    		cancelPnrStatusList.addAll(Arrays.asList(CancelPnrStatus.values()));
    	}
    	return cancelPnrStatusList;
    }
}
