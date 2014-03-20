package com.travelzen.framework.core.dict;

public enum CheckQueryStatus {
     notCheck("未复核"),
     checked("已复核"),
    
    ;
    private String desc;
    private CheckQueryStatus(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
