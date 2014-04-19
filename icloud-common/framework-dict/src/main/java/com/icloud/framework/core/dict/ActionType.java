package com.icloud.framework.core.dict;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public enum ActionType {
    refundAtSaleDate("1","当日作废，扣10元手续费"),
    otherFP("2","其它废票情况"),
    refundOnPassengerDemand("3","客人自愿退票，按客规收取手续费"),
    applyRefundAll("4","南航F/C舱、国航F/C舱、东航F/C/Y舱、海航F/C舱，申请全退"),
    refundBecauseOfAirPlainProblem("5","因航班取消/延误，申请全退"),
    changeTicket("6","升舱换开，申请全退"),
    byErrorName("7","名字错换开重出，申请全退"), 
    fullRefundBecauseOfDisease("8","客人因病无法乘机，申请全退"),
    otherTP("9","其它退票情况"),
    returnDetaPrice("10","申请退回票款差价"),
    ;
    private String value;
    private String key;
    private ActionType(String key, String value) {
        this.value = value;
        this.key = key;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public static List<ActionType> getActionTypeList(String orderType){
    	List<ActionType> actionTypeList = new ArrayList<ActionType>();
    	orderType = StringUtils.trimToEmpty(orderType);
    	if("FP".equals(orderType)){
    		actionTypeList.add(refundAtSaleDate);
    		actionTypeList.add(otherFP);
    	}else{
    		for(ActionType ele : ActionType.values()){
    			if(ele != refundAtSaleDate && ele != otherFP)
    				actionTypeList.add(ele);
    		}
    	}
    	return actionTypeList;
    }
    
    
}
