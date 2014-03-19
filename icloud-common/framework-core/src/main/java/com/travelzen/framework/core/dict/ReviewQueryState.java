package com.travelzen.framework.core.dict;

public enum ReviewQueryState {
    unprocess("未处理"),
    processing("处理中"),
    reviwePassed("审核通过"),
    reviewReturned("审核退回"),
    ;
    private String desc;
    private ReviewQueryState(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
