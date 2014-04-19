package com.icloud.framework.core.dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

public enum EndorseFlightOrderItemState {
	init("新建", EndorsePageOrderState.init, true, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.init, FlightOrderItemMajorStageState.complete),AllQueryState.booked,null,IssueQueryState.unprcossed,PayQueryState.notPaid,GatheringQueryState.notGathering,BusinessFlowState.all,EndorseOrderPageBusinessFlowState.init),
	waiting_for_review("等待审核", EndorsePageOrderState.waiting_for_review, true, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.init, FlightOrderItemMajorStageState.complete),AllQueryState.submitted,ReviewQueryState.unprocess,IssueQueryState.unprcossed,PayQueryState.notPaid,GatheringQueryState.notGathering,BusinessFlowState.review,EndorseOrderPageBusinessFlowState.waiting_for_review),
	reviewing("审核中", EndorsePageOrderState.reviewing, true, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.review, FlightOrderItemMajorStageState.processing),AllQueryState.processing,ReviewQueryState.processing,IssueQueryState.unprcossed,PayQueryState.notPaid,GatheringQueryState.notGathering,BusinessFlowState.review,EndorseOrderPageBusinessFlowState.reviewing),
	review_success("审核通过", EndorsePageOrderState.review_success, true, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.review, FlightOrderItemMajorStageState.complete),AllQueryState.processed,ReviewQueryState.reviwePassed,IssueQueryState.unprcossed,PayQueryState.notPaid,GatheringQueryState.notGathering,BusinessFlowState.review,EndorseOrderPageBusinessFlowState.review_success),
	review_fail("审核未通过", EndorsePageOrderState.review_fail, true, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.review, FlightOrderItemMajorStageState.exception),AllQueryState.returned,ReviewQueryState.reviewReturned,IssueQueryState.unprcossed,PayQueryState.notPaid,GatheringQueryState.notGathering,BusinessFlowState.review,EndorseOrderPageBusinessFlowState.review_fail),
	review_suspend("审核挂起", EndorsePageOrderState.review_suspend, true, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.review, FlightOrderItemMajorStageState.processing),AllQueryState.processing,ReviewQueryState.processing,IssueQueryState.unprcossed,PayQueryState.notPaid,GatheringQueryState.notGathering,BusinessFlowState.review,EndorseOrderPageBusinessFlowState.review_suspend),
	cancel("取消", EndorsePageOrderState.cancel,false, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.cancel, FlightOrderItemMajorStageState.complete),null,null,null,null,null,BusinessFlowState.all,EndorseOrderPageBusinessFlowState.cancel),
	req_frozen("请求冻结", EndorsePageOrderState.review_success, true, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.review, FlightOrderItemMajorStageState.complete),null,null,IssueQueryState.unprcossed,PayQueryState.notPaid,GatheringQueryState.notGathering,null,null),
	frozen_success("冻结成功", EndorsePageOrderState.waiting_for_process, true, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.review, FlightOrderItemMajorStageState.complete),null,null,IssueQueryState.unprcossed,PayQueryState.paid,GatheringQueryState.notGathering,null,null),
	frozen_fail("冻结失败", EndorsePageOrderState.review_success, true, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.review, FlightOrderItemMajorStageState.complete),null,null,IssueQueryState.unprcossed,PayQueryState.notPaid,GatheringQueryState.notGathering,null,null),
	waiting_for_process("待处理", EndorsePageOrderState.waiting_for_process, true, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.review, FlightOrderItemMajorStageState.complete),AllQueryState.submitted,null,IssueQueryState.unprcossed,PayQueryState.paid,GatheringQueryState.notGathering,BusinessFlowState.issue,EndorseOrderPageBusinessFlowState.waiting_for_process),
	processing("处理中", EndorsePageOrderState.processing, true, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.endorse, FlightOrderItemMajorStageState.processing),AllQueryState.processing,null,IssueQueryState.processing,PayQueryState.paid,GatheringQueryState.notGathering,BusinessFlowState.issue,EndorseOrderPageBusinessFlowState.processing),
	process_suspend("处理挂起", EndorsePageOrderState.process_suspend, true, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.endorse, FlightOrderItemMajorStageState.processing),AllQueryState.processing,null,IssueQueryState.processing,PayQueryState.paid,GatheringQueryState.notGathering,BusinessFlowState.issue,EndorseOrderPageBusinessFlowState.process_suspend),
	req_unfrozen_causedBy_reject("退回引发的请求解冻", EndorsePageOrderState.returned, false, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.endorse, FlightOrderItemMajorStageState.exception),AllQueryState.returned,null,IssueQueryState.rejectProcess,PayQueryState.notPaid,GatheringQueryState.notGathering,BusinessFlowState.issue,EndorseOrderPageBusinessFlowState.reject),
	unfrozen_success_causedBy_reject("退回引发的解冻成功", EndorsePageOrderState.returned, false, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.endorse, FlightOrderItemMajorStageState.exception),AllQueryState.returned,null,IssueQueryState.rejectProcess,PayQueryState.notPaid,GatheringQueryState.notGathering,BusinessFlowState.issue,EndorseOrderPageBusinessFlowState.reject),
	unfrozen_fail_causedBy_reject("退回引发的解冻失败", EndorsePageOrderState.returned, false, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.endorse, FlightOrderItemMajorStageState.exception),AllQueryState.returned,null,IssueQueryState.rejectProcess,PayQueryState.notPaid,GatheringQueryState.notGathering,BusinessFlowState.issue,EndorseOrderPageBusinessFlowState.reject),
	returned("退回", EndorsePageOrderState.returned, false, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.endorse, FlightOrderItemMajorStageState.exception),AllQueryState.returned,null,IssueQueryState.rejectProcess,PayQueryState.notPaid,GatheringQueryState.notGathering,BusinessFlowState.issue,EndorseOrderPageBusinessFlowState.reject),
	endorse_complete("改签完成", EndorsePageOrderState.endorse_complete, true, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.endorse, FlightOrderItemMajorStageState.complete),AllQueryState.processed,null,IssueQueryState.complete,PayQueryState.paid,GatheringQueryState.notGathering,BusinessFlowState.issue,EndorseOrderPageBusinessFlowState.endorse_complete),
	req_gathering("请求收银", EndorsePageOrderState.endorse_complete, true, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.complete, FlightOrderItemMajorStageState.complete),AllQueryState.processed,null,IssueQueryState.complete,PayQueryState.paid,GatheringQueryState.notGathering,BusinessFlowState.issue,EndorseOrderPageBusinessFlowState.endorse_complete),
	gathering_success("收银成功", EndorsePageOrderState.endorse_complete, true, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.complete, FlightOrderItemMajorStageState.complete),AllQueryState.processed,null,IssueQueryState.complete,PayQueryState.paid,GatheringQueryState.gathered,BusinessFlowState.issue,EndorseOrderPageBusinessFlowState.endorse_complete),
	gathering_fail("收银失败", EndorsePageOrderState.endorse_complete, true, new ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(EndorseFlightOrderItemMajorStage.complete, FlightOrderItemMajorStageState.complete),AllQueryState.processed,null,IssueQueryState.complete,PayQueryState.paid,GatheringQueryState.notGathering,BusinessFlowState.issue,EndorseOrderPageBusinessFlowState.endorse_complete);

	private String desc;
	private EndorsePageOrderState pageOrderState;
	private AllQueryState allQueryState;
	private ReviewQueryState reviewQueryState;
	private IssueQueryState issueQueryState;
	private PayQueryState payQueryState;
	private GatheringQueryState gatheringQueryState;
	private BusinessFlowState businessFlowState;
	private EndorseOrderPageBusinessFlowState endorseOrderPageBusinessFlowState;
	private boolean orderProcessingWhenOnThisState;
	private ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState> majorStageState;

	public static Map<EndorsePageOrderState, List<EndorseFlightOrderItemState> > pageOrderStateMappingtTable = new HashMap<EndorsePageOrderState, List<EndorseFlightOrderItemState>>();
	static{
		for(EndorsePageOrderState pageOrderState : EndorsePageOrderState.values())
	    	pageOrderStateMappingtTable.put(pageOrderState, new ArrayList<EndorseFlightOrderItemState>());
	    for(EndorseFlightOrderItemState orderItemState : EndorseFlightOrderItemState.values())
	    	pageOrderStateMappingtTable.get(orderItemState.getPageOrderState()).add(orderItemState);
	}

	private EndorseFlightOrderItemState(String desc, EndorsePageOrderState pageOrderState, boolean orderProcessingWhenOnThisState, ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState> majorStageState,AllQueryState allQueryState,ReviewQueryState reviewQueryState,IssueQueryState issueQueryState,PayQueryState payQueryState,GatheringQueryState gatheringQueryState,BusinessFlowState businessFlowState,EndorseOrderPageBusinessFlowState endorseOrderPageBusinessFlowState) {
		this.desc = desc;
		this.pageOrderState = pageOrderState;
		this.allQueryState = allQueryState;
		this.reviewQueryState = reviewQueryState;
		this.issueQueryState = issueQueryState;
		this.payQueryState = payQueryState;
		this.gatheringQueryState = gatheringQueryState;
		this.businessFlowState = businessFlowState;
		this.endorseOrderPageBusinessFlowState = endorseOrderPageBusinessFlowState;
		this.orderProcessingWhenOnThisState = orderProcessingWhenOnThisState;
		this.majorStageState = majorStageState;
	}

	public String getDesc() {
		return desc;
	}

	public EndorsePageOrderState getPageOrderState() {
		return pageOrderState;
	}


	public void setPageOrderState(EndorsePageOrderState pageOrderState) {
		this.pageOrderState = pageOrderState;
	}

	public ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState> getMajorStageState() {
		return majorStageState;
	}

	public void setMajorStageState(ImmutablePair<EndorseFlightOrderItemMajorStage, FlightOrderItemMajorStageState> majorStageState) {
		this.majorStageState = majorStageState;
	}

	public boolean isOrderProcessingWhenOnThisState() {
		return orderProcessingWhenOnThisState;
	}

	public void setOrderProcessingWhenOnThisState(boolean orderProcessingWhenOnThisState) {
		this.orderProcessingWhenOnThisState = orderProcessingWhenOnThisState;
	}

    public AllQueryState getAllQueryState() {
        return allQueryState;
    }

    public void setAllQueryState(AllQueryState allQueryState) {
        this.allQueryState = allQueryState;
    }

    public ReviewQueryState getReviewQueryState() {
        return reviewQueryState;
    }

    public void setReviewQueryState(ReviewQueryState reviewQueryState) {
        this.reviewQueryState = reviewQueryState;
    }

    public IssueQueryState getIssueQueryState() {
        return issueQueryState;
    }

    public void setIssueQueryState(IssueQueryState issueQueryState) {
        this.issueQueryState = issueQueryState;
    }

    public PayQueryState getPayQueryState() {
        return payQueryState;
    }

    public void setPayQueryState(PayQueryState payQueryState) {
        this.payQueryState = payQueryState;
    }

    public GatheringQueryState getGatheringQueryState() {
        return gatheringQueryState;
    }

    public void setGatheringQueryState(GatheringQueryState gatheringQueryState) {
        this.gatheringQueryState = gatheringQueryState;
    }
    
    
    public static List<EndorseFlightOrderItemState>  getItemStates(AllQueryState allQueryState){
        List<EndorseFlightOrderItemState> result = new ArrayList<EndorseFlightOrderItemState>();
        for(EndorseFlightOrderItemState state : EndorseFlightOrderItemState.values())
            if(allQueryState == state.getAllQueryState())
                result.add(state);
        return result;
        
    }
    public static List<EndorseFlightOrderItemState>  getItemReviewStates(ReviewQueryState reviewQueryState){
        List<EndorseFlightOrderItemState> result = new ArrayList<EndorseFlightOrderItemState>();
        for(EndorseFlightOrderItemState state : EndorseFlightOrderItemState.values())
            if(reviewQueryState == state.getReviewQueryState())
                result.add(state);
        return result;
        
    }

    public static List<EndorseFlightOrderItemState>  getItemIssueStates(IssueQueryState issueQueryState){
         List<EndorseFlightOrderItemState> result = new ArrayList<EndorseFlightOrderItemState>();
         for(EndorseFlightOrderItemState state : EndorseFlightOrderItemState.values())
             if(issueQueryState == state.getIssueQueryState())
                 result.add(state);
         return result;
         
     }
    
    public static List<EndorseFlightOrderItemState>  getItemPayStates(PayQueryState payQueryState){
        List<EndorseFlightOrderItemState> result = new ArrayList<EndorseFlightOrderItemState>();
        for(EndorseFlightOrderItemState state : EndorseFlightOrderItemState.values())
            if(payQueryState == state.getPayQueryState())
                result.add(state);
        return result;
        
    }
    public static List<EndorseFlightOrderItemState>  getItemGatheringStates(GatheringQueryState gatheringQueryState){
        List<EndorseFlightOrderItemState> result = new ArrayList<EndorseFlightOrderItemState>();
        for(EndorseFlightOrderItemState state : EndorseFlightOrderItemState.values())
            if(gatheringQueryState == state.getGatheringQueryState())
                result.add(state);
        return result;
        
    }

    
    public BusinessFlowState getBusinessFlowState() {
        return businessFlowState;
    }

    public void setBusinessFlowState(BusinessFlowState businessFlowState) {
        this.businessFlowState = businessFlowState;
    }

    public EndorseOrderPageBusinessFlowState getEndorseOrderPageBusinessFlowState() {
        return endorseOrderPageBusinessFlowState;
    }

    public void setEndorseOrderPageBusinessFlowState(EndorseOrderPageBusinessFlowState endorseOrderPageBusinessFlowState) {
        this.endorseOrderPageBusinessFlowState = endorseOrderPageBusinessFlowState;
    }
    
}
