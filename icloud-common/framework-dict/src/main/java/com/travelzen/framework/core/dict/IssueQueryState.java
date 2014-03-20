
package com.travelzen.framework.core.dict;

public enum IssueQueryState {
    unprcossed("未处理"),
    processing("处理中"),
    canNotProcess("暂不能处理"),
    rejectProcess("拒绝处理"),
    complete("已完成"),
    ;
    private String desc;
    private IssueQueryState(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
