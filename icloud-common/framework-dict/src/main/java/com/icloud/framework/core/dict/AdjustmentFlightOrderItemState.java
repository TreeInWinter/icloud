package com.icloud.framework.core.dict;

import java.util.HashMap;
import java.util.Map;


public enum AdjustmentFlightOrderItemState {
	init("新建",AdjustmentFlightOrderItemMajorStage.init,AdjustmentOrderItemMajorStageState.processing),
	commit("提交申请",AdjustmentFlightOrderItemMajorStage.init,AdjustmentOrderItemMajorStageState.processing),
	review_success("审核通过",AdjustmentFlightOrderItemMajorStage.pay,AdjustmentOrderItemMajorStageState.not_begin),
	review_fail("审核失败",AdjustmentFlightOrderItemMajorStage.review,AdjustmentOrderItemMajorStageState.exception),
	req_pay("请求支付",AdjustmentFlightOrderItemMajorStage.pay,AdjustmentOrderItemMajorStageState.exception),
	req_fail("请求支付失败",AdjustmentFlightOrderItemMajorStage.init,AdjustmentOrderItemMajorStageState.processing),
	pay_fail("支付失败",AdjustmentFlightOrderItemMajorStage.pay,AdjustmentOrderItemMajorStageState.processing),
	pay_success("支付成功",AdjustmentFlightOrderItemMajorStage.pay,AdjustmentOrderItemMajorStageState.processing),
	complete("调账完成",AdjustmentFlightOrderItemMajorStage.adjustment,AdjustmentOrderItemMajorStageState.complete),
	req_gathering("请求收银",AdjustmentFlightOrderItemMajorStage.adjustment,AdjustmentOrderItemMajorStageState.complete),
	gathering_success("收银成功",AdjustmentFlightOrderItemMajorStage.adjustment,AdjustmentOrderItemMajorStageState.complete),
	gathering_fail("收银失败",AdjustmentFlightOrderItemMajorStage.adjustment,AdjustmentOrderItemMajorStageState.complete),
	cancel("取消订单",AdjustmentFlightOrderItemMajorStage.cancel,AdjustmentOrderItemMajorStageState.cancel); 
	private String desc;
	
	private Map<String,String> majorStageState ;
	private AdjustmentFlightOrderItemMajorStage majorStage ;
	private AdjustmentOrderItemMajorStageState stageState;
	
	
	private AdjustmentFlightOrderItemState(String desc,AdjustmentFlightOrderItemMajorStage currentMajorStage,AdjustmentOrderItemMajorStageState stageState) {
		this.desc = desc;
		this.majorStage = currentMajorStage;
		this.stageState = stageState;
		majorStageState = new HashMap<String,String>();
		AdjustmentFlightOrderItemMajorStage[] stages =AdjustmentFlightOrderItemMajorStage.values();
		
		//取消特殊处理
		if(currentMajorStage == AdjustmentFlightOrderItemMajorStage.cancel ){
			AdjustmentOrderItemMajorStageState stageStates = AdjustmentOrderItemMajorStageState.complete;
			
			for (AdjustmentFlightOrderItemMajorStage adjustmentFlightOrderItemMajorStage : stages) {
				if(AdjustmentFlightOrderItemMajorStage.review == adjustmentFlightOrderItemMajorStage){
					majorStageState.put(adjustmentFlightOrderItemMajorStage.name(), stageStates.name());
					stageStates = AdjustmentOrderItemMajorStageState.not_begin;
				}else{
					majorStageState.put(adjustmentFlightOrderItemMajorStage.name(), stageStates.name());
				}
			}
			majorStageState.put(AdjustmentFlightOrderItemMajorStage.cancel.name(), AdjustmentOrderItemMajorStageState.complete.name());
			
		}else{
			AdjustmentOrderItemMajorStageState stageStates = AdjustmentOrderItemMajorStageState.complete;
			for (AdjustmentFlightOrderItemMajorStage adjustmentFlightOrderItemMajorStage : stages) {
				if(currentMajorStage == adjustmentFlightOrderItemMajorStage){
					majorStageState.put(currentMajorStage.name(), stageState.name());
					stageStates = AdjustmentOrderItemMajorStageState.not_begin;
				}else{
					majorStageState.put(adjustmentFlightOrderItemMajorStage.name(), stageStates.name());
				}
			}
		}
		
		

	}
	
	public AdjustmentOrderItemMajorStageState getStageState() {
		return stageState;
	}

	public AdjustmentFlightOrderItemMajorStage getMajorStage() {
		return majorStage;
	}

	public String getDesc() {
		return desc;
	}

	public Map<String, String> getMajorStageState() {
		return majorStageState;
	}
	
}
