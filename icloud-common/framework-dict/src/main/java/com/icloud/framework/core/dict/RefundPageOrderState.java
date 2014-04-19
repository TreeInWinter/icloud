package com.icloud.framework.core.dict;

/*
 * 退废订单页面状态
 */
public enum RefundPageOrderState {
	init("新建"),
	waiting_for_confirm("待审核"),
	confirming("审核中"),
	confirm_suspend("审核挂起"),
	refund_suspend("退票挂起"),
	return_order("退回"),
	cancel("已取消"),
	waiting_for_refund("待退票"),
	refunding("退票中"),
	refund_success("完成退票");

	private String desc;

	private RefundPageOrderState(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	
	/**
	 * 退票相关状态
	 */
	public static RefundPageOrderState[] getRefundRelatedState(){
	    return new  RefundPageOrderState[]{waiting_for_confirm,confirming,waiting_for_refund,refunding,confirm_suspend,refund_suspend};
	}
	public static RefundPageOrderState[] getRefundPageRelatedState(){
        return new  RefundPageOrderState[]{waiting_for_confirm,confirming,waiting_for_refund,refunding};
    }
}
