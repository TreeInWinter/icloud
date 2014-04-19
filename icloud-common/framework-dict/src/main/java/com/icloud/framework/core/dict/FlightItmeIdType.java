package com.icloud.framework.core.dict;

public enum FlightItmeIdType {
     flightItemId("订单号"),
     purchaseFlightItmeId("采购订单号"),
     supplyFlightItemId("供应订单号"),
    ;
    private String desc;
    private FlightItmeIdType(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
