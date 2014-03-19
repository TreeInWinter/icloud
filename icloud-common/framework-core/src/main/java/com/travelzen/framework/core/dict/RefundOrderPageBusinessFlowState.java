package com.travelzen.framework.core.dict;

public enum RefundOrderPageBusinessFlowState {
    init("新建"),
    waiting_for_review("待审核"),
    reviewing("审核中"),
    confirm_suspend("审核挂起"),
    review_fail("审核未通过"),
    waiting_for_refund("待退票"),
    refunding("退票中"),
    refund_suspend("退票挂起"),
    reject("拒绝退票"),
    refund_complete("完成退票"),
    cancel("取消"),
    
    ;
    private String desc;
    private RefundOrderPageBusinessFlowState(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
