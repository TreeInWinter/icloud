package com.travelzen.framework.core.dict;

public enum DateType {
   createDate("创建日期"),
   departureDate("起飞时间"),
   updateDate("修改时间"),
   issueDate("出票时间"),
   gatheringDate("收款时间"),
   ;
   private String desc;
   private DateType(String desc) {
       this.desc = desc;
   }
   public String getDesc() {
       return desc;
   }
   public void setDesc(String desc) {
       this.desc = desc;
   }
   
}
