package com.icloud.framework.core.dict;

public enum PersonType {
      creatorPerson("创建人"),
      payPerson("付款人"),
      reviewerPerson("审核员"),
      issuePerson("出票员"),
      gatheringPerson("收款员"),
      ;
      private String desc;
      private PersonType(String desc) {
          this.desc = desc;
      }
      public String getDesc() {
          return desc;
      }
      public void setDesc(String desc) {
          this.desc = desc;
      }
}
