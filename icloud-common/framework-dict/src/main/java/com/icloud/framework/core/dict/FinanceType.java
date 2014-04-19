package com.icloud.framework.core.dict;

public enum FinanceType {
    pay("付款"),
    gather("收款"),
    check("复核"),
    ;
    private String desc;
    private FinanceType(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
   

}
