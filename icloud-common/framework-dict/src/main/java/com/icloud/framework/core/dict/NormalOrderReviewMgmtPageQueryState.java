package com.icloud.framework.core.dict;

import java.util.ArrayList;
import java.util.List;

public enum NormalOrderReviewMgmtPageQueryState {
    waiting_for_review("待审核"),
    reviewing("审核中"),
    ;
    private String desc;
    private NormalOrderReviewMgmtPageQueryState(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    /**
     * 根据页面状态查询对应的数据库状态。如果传入的参数为null，返回所有的相关的数据库状态。
     * @param pageState
     * @return
     */
    public static List<FlightOrderItemState> getFlightOrderItemStateListByPageState(NormalOrderReviewMgmtPageQueryState pageState){
    	List<FlightOrderItemState> orderItemStateList = new ArrayList<FlightOrderItemState>();
    	if(pageState == null){
    		orderItemStateList.add(FlightOrderItemState.waiting_for_review);
    		orderItemStateList.add(FlightOrderItemState.reviewing);
    		orderItemStateList.add(FlightOrderItemState.review_suspend);
    	}else if(pageState == waiting_for_review){
    		orderItemStateList.add(FlightOrderItemState.waiting_for_review);
    	}else if(pageState == reviewing){
    		orderItemStateList.add(FlightOrderItemState.reviewing);
    		orderItemStateList.add(FlightOrderItemState.review_suspend);
    	}
    	return orderItemStateList;
    }
}
