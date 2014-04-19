package com.icloud.framework.core.dict;

public enum PayQueryState {
    notPaid("未付款"),
    paid("已付款"),
    ;
    private String desc;
    private PayQueryState(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
