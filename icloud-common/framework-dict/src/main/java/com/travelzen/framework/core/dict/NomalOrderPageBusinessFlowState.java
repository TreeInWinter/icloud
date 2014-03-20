package com.travelzen.framework.core.dict;

public enum NomalOrderPageBusinessFlowState {
    init("新建"),
    waiting_for_review("待审核"),
    reviewing("审核中"),
    review_suspend("审核挂起"),
    review_success("审核通过"),
    review_fail("审核未通过"),
    waiting_for_issue("待出票"),
    issuing("出票中"),
    suspend("出票挂起"),
    tmp_reject("暂不能出票"),
    reject("拒绝出票"),
    tk_success("完成出票"),
    cancel("取消"),
    
    ;
    private String desc;
    private NomalOrderPageBusinessFlowState(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
