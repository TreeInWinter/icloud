package com.travelzen.framework.core.dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

public enum RefundFlightOrderItemState {
	init("新建", RefundPageOrderState.init, new ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(RefundFlightOrderItemMajorStage.init, FlightOrderItemMajorStageState.complete),AllQueryState.booked,null,IssueQueryState.unprcossed,GatheringQueryState.notGathering,BusinessFlowState.all,RefundOrderPageBusinessFlowState.init),
	waiting_for_confirm("待审核", RefundPageOrderState.waiting_for_confirm,new ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(RefundFlightOrderItemMajorStage.init, FlightOrderItemMajorStageState.complete),AllQueryState.submitted,ReviewQueryState.unprocess,IssueQueryState.unprcossed,GatheringQueryState.notGathering,BusinessFlowState.review,RefundOrderPageBusinessFlowState.waiting_for_review),
	confirming("审核中", RefundPageOrderState.confirming,new ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(RefundFlightOrderItemMajorStage.review, FlightOrderItemMajorStageState.processing),AllQueryState.processing,ReviewQueryState.processing,IssueQueryState.unprcossed,GatheringQueryState.notGathering,BusinessFlowState.review,RefundOrderPageBusinessFlowState.reviewing),
	confirm_suspend("审核挂起", RefundPageOrderState.confirm_suspend, new ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(RefundFlightOrderItemMajorStage.review, FlightOrderItemMajorStageState.processing),AllQueryState.processing,ReviewQueryState.processing,IssueQueryState.unprcossed,GatheringQueryState.notGathering,BusinessFlowState.review,RefundOrderPageBusinessFlowState.refund_suspend),
	confirm_fail("审核不通过", RefundPageOrderState.return_order, new ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(RefundFlightOrderItemMajorStage.review, FlightOrderItemMajorStageState.exception),AllQueryState.returned,ReviewQueryState.reviewReturned,IssueQueryState.unprcossed,GatheringQueryState.notGathering,BusinessFlowState.review,RefundOrderPageBusinessFlowState.review_fail),
	waiting_for_refund("待退票", RefundPageOrderState.waiting_for_refund, new ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(RefundFlightOrderItemMajorStage.review, FlightOrderItemMajorStageState.complete),AllQueryState.submitted,null,IssueQueryState.unprcossed,GatheringQueryState.notGathering,BusinessFlowState.issue,RefundOrderPageBusinessFlowState.waiting_for_refund),
	return_order("退回", RefundPageOrderState.return_order, new ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(RefundFlightOrderItemMajorStage.refund, FlightOrderItemMajorStageState.exception),AllQueryState.returned,null,IssueQueryState.rejectProcess,GatheringQueryState.notGathering,BusinessFlowState.issue,RefundOrderPageBusinessFlowState.reject),
	cancel("已取消", RefundPageOrderState.cancel, new ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(RefundFlightOrderItemMajorStage.cancel, FlightOrderItemMajorStageState.complete),null,null,IssueQueryState.rejectProcess,null,BusinessFlowState.all,RefundOrderPageBusinessFlowState.cancel),
	refunding("退票中", RefundPageOrderState.refunding, new ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(RefundFlightOrderItemMajorStage.refund, FlightOrderItemMajorStageState.processing),AllQueryState.processing,null,IssueQueryState.processing,GatheringQueryState.notGathering,BusinessFlowState.issue,RefundOrderPageBusinessFlowState.refunding),
	applyPlatformRefundFail("申请平台退票失败", RefundPageOrderState.refunding, new ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(RefundFlightOrderItemMajorStage.refund, FlightOrderItemMajorStageState.processing),AllQueryState.processing,null,IssueQueryState.processing,GatheringQueryState.notGathering,BusinessFlowState.issue,RefundOrderPageBusinessFlowState.refunding),
	platformRefunding("平台退票中", RefundPageOrderState.refunding, new ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(RefundFlightOrderItemMajorStage.refund, FlightOrderItemMajorStageState.processing),AllQueryState.processing,null,IssueQueryState.processing,GatheringQueryState.notGathering,BusinessFlowState.issue,RefundOrderPageBusinessFlowState.refunding),
	platformRefundFail("平台退票失败", RefundPageOrderState.refunding, new ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(RefundFlightOrderItemMajorStage.refund, FlightOrderItemMajorStageState.processing),AllQueryState.processing,null,IssueQueryState.processing,GatheringQueryState.notGathering,BusinessFlowState.issue,RefundOrderPageBusinessFlowState.refunding),
	refund_suspend("退票挂起", RefundPageOrderState.refund_suspend, new ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(RefundFlightOrderItemMajorStage.refund, FlightOrderItemMajorStageState.processing),AllQueryState.processing,null,IssueQueryState.processing,GatheringQueryState.notGathering,BusinessFlowState.issue,RefundOrderPageBusinessFlowState.refund_suspend),
	refund_success("完成退票", RefundPageOrderState.refund_success, new ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(RefundFlightOrderItemMajorStage.complete, FlightOrderItemMajorStageState.complete),AllQueryState.processed,null,IssueQueryState.complete,GatheringQueryState.notGathering,BusinessFlowState.issue,RefundOrderPageBusinessFlowState.refund_complete),
	req_gathering("请求收银", RefundPageOrderState.refund_success, new ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(RefundFlightOrderItemMajorStage.complete, FlightOrderItemMajorStageState.complete),AllQueryState.processed,null,IssueQueryState.complete,GatheringQueryState.notGathering,BusinessFlowState.issue,RefundOrderPageBusinessFlowState.refund_complete),
    gathering_success("收银成功", RefundPageOrderState.refund_success, new ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(RefundFlightOrderItemMajorStage.complete, FlightOrderItemMajorStageState.complete),AllQueryState.processed,null,IssueQueryState.complete,GatheringQueryState.gathered,BusinessFlowState.issue,RefundOrderPageBusinessFlowState.refund_complete),
    gathering_fail("收银失败", RefundPageOrderState.refund_success, new ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState>(RefundFlightOrderItemMajorStage.complete, FlightOrderItemMajorStageState.complete),AllQueryState.processed,null,IssueQueryState.complete,GatheringQueryState.notGathering,BusinessFlowState.issue,RefundOrderPageBusinessFlowState.refund_complete);

	private String desc;
	private RefundPageOrderState pageOrderState;
	private AllQueryState allQueryState;
	private ReviewQueryState reviewQueryState;
	private IssueQueryState issueQueryState;
	private GatheringQueryState gatheringQueryState;
	private BusinessFlowState businessFlowState;
	private RefundOrderPageBusinessFlowState refundOrderPageBusinessFlowState;
	private ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState> majorStageState;
	//页面状态与数据库状态的对应关系
	public static Map<RefundPageOrderState, List<RefundFlightOrderItemState> > pageOrderStateMappingtTable = new HashMap<RefundPageOrderState, List<RefundFlightOrderItemState>>();
	static{
	    for(RefundPageOrderState pageOrderState : RefundPageOrderState.values())
	    	pageOrderStateMappingtTable.put(pageOrderState, new ArrayList<RefundFlightOrderItemState>());
	    for(RefundFlightOrderItemState orderItemState : RefundFlightOrderItemState.values())
	    	pageOrderStateMappingtTable.get(orderItemState.getPageOrderState()).add(orderItemState);
	}

	private RefundFlightOrderItemState(String desc, RefundPageOrderState pageOrderState, ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState> majorStageState,AllQueryState allQueryState,ReviewQueryState reviewQueryState,IssueQueryState issueQueryState,GatheringQueryState gatheringQueryState,BusinessFlowState businessFlowState,RefundOrderPageBusinessFlowState refundOrderPageBusinessFlowState) {
		this.desc = desc;
		this.pageOrderState = pageOrderState;
		this.allQueryState = allQueryState;
		this.reviewQueryState = reviewQueryState;
		this.issueQueryState = issueQueryState;
		this.gatheringQueryState = gatheringQueryState;
		this.businessFlowState = businessFlowState;
		this.refundOrderPageBusinessFlowState = refundOrderPageBusinessFlowState;
		this.majorStageState = majorStageState;
	}

	public String getDesc() {
		return desc;
	}

	public RefundPageOrderState getPageOrderState() {
		return pageOrderState;
	}

	public ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState> getMajorStageState() {
		return majorStageState;
	}

	public void setMajorStageState(ImmutablePair<RefundFlightOrderItemMajorStage, FlightOrderItemMajorStageState> majorStageState) {
		this.majorStageState = majorStageState;
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

    public GatheringQueryState getGatheringQueryState() {
        return gatheringQueryState;
    }

    public void setGatheringQueryState(GatheringQueryState gatheringQueryState) {
        this.gatheringQueryState = gatheringQueryState;
    }
    
    public static List<RefundFlightOrderItemState>  getItemStates(AllQueryState allQueryState){
        List<RefundFlightOrderItemState> result = new ArrayList<RefundFlightOrderItemState>();
        for(RefundFlightOrderItemState state : RefundFlightOrderItemState.values())
            if(allQueryState == state.getAllQueryState())
                result.add(state);
        return result;
        
    }
    public static List<RefundFlightOrderItemState>  getItemReviewStates(ReviewQueryState reviewQueryState){
        List<RefundFlightOrderItemState> result = new ArrayList<RefundFlightOrderItemState>();
        for(RefundFlightOrderItemState state : RefundFlightOrderItemState.values())
            if(reviewQueryState == state.getReviewQueryState())
                result.add(state);
        return result;
        
    }

    public static List<RefundFlightOrderItemState>  getItemIssueStates(IssueQueryState issueQueryState){
         List<RefundFlightOrderItemState> result = new ArrayList<RefundFlightOrderItemState>();
         for(RefundFlightOrderItemState state : RefundFlightOrderItemState.values())
             if(issueQueryState == state.getIssueQueryState())
                 result.add(state);
         return result;
         
     }
    public static List<RefundFlightOrderItemState>  getItemGatheringStates(GatheringQueryState gatheringQueryState){
        List<RefundFlightOrderItemState> result = new ArrayList<RefundFlightOrderItemState>();
        for(RefundFlightOrderItemState state : RefundFlightOrderItemState.values())
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

    public RefundOrderPageBusinessFlowState getRefundOrderPageBusinessFlowState() {
        return refundOrderPageBusinessFlowState;
    }

    public void setRefundOrderPageBusinessFlowState(RefundOrderPageBusinessFlowState refundOrderPageBusinessFlowState) {
        this.refundOrderPageBusinessFlowState = refundOrderPageBusinessFlowState;
    }
    
}
