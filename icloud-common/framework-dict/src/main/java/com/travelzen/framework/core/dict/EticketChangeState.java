package com.travelzen.framework.core.dict;

public enum EticketChangeState {
	endorsing("改签中"), 
	endorse_complete("完成"),
	refunding("退票中"),
	refund_complete("完成"),
	adjustment("调账"), 
	adjustment_complete("完成");
	
	private String desc;

	private EticketChangeState(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
