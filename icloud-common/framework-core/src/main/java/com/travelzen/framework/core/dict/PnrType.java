package com.travelzen.framework.core.dict;

public enum PnrType {
    pnr("PNR"),
    ics_pnr("大编码"),
    ;
    private String desc;
    private PnrType(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
