package com.travelzen.framework.core.dict;

public enum GatheringQueryState {
        notGathering("未收款"),
        gathered("已收款"),
    ;
    private String desc;
    private GatheringQueryState(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
}
