package com.travelzen.framework.core.dict;

public enum EndorseOrderPageBusinessFlowState {
    init("新建"),
    waiting_for_review("待审核"),
    reviewing("审核中"),
    review_suspend("审核挂起"),
    review_success("审核通过"),
    review_fail("审核未通过"),
    waiting_for_process("待处理"),
    processing("处理中"),
    process_suspend("处理挂起"),
    reject("拒绝改签"),
    endorse_complete("完成改签"),
    cancel("取消"),
    
    ;
    private String desc;
    private EndorseOrderPageBusinessFlowState(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
