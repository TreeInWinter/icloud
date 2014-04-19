package com.icloud.framework.core.dict;

public enum BusinessFlowState {
    all("预订"),
    review("审核"),
    issue("出票"),
    ;
    private String desc;
    private BusinessFlowState(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
