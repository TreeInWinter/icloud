package com.travelzen.framework.core.dict;

public enum OrderAssignQueue {
	normal_order_issue_queue("正常订单出票队列"), 

	normal_order_review_queue("正常订单审核队列"),

	refund_order_quere("退票队列"),
	
	endorse_order_review_queue("改签审核队列"),

	;

	private String desc;

	private OrderAssignQueue(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
