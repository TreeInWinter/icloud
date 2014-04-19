package com.icloud.framework.core.dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;


/**
 * 正常订单状态
 */
public enum FlightOrderItemState {
	init_waiting_for_pay("新建,等待支付", PageOrderState.waiting_for_pay, true, true, AllQueryState.booked, null, IssueQueryState.unprcossed, PayQueryState.notPaid, GatheringQueryState.notGathering,BusinessFlowState.all,NomalOrderPageBusinessFlowState.init) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(init_waiting_for_pay, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.init, FlightOrderItemMajorStageState.complete));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.waiting_for_pay).add(this);
		}

	},
	waiting_for_review("等待审核", PageOrderState.waiting_for_review, true, false, AllQueryState.submitted, ReviewQueryState.unprocess, IssueQueryState.unprcossed, PayQueryState.notPaid, GatheringQueryState.notGathering,BusinessFlowState.review,NomalOrderPageBusinessFlowState.waiting_for_review) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(waiting_for_review, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.init, FlightOrderItemMajorStageState.complete));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.waiting_for_review).add(this);
		}
	},
	reviewing("审核中", PageOrderState.reviewing,  true, false, AllQueryState.processing, ReviewQueryState.processing, IssueQueryState.unprcossed, PayQueryState.notPaid, GatheringQueryState.notGathering,BusinessFlowState.review,NomalOrderPageBusinessFlowState.reviewing) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(reviewing, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.review, FlightOrderItemMajorStageState.processing));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.reviewing).add(this);
		}

	},
	review_suspend("审核挂起", PageOrderState.review_suspend, true, false, AllQueryState.processing, ReviewQueryState.processing, IssueQueryState.unprcossed, PayQueryState.notPaid, GatheringQueryState.notGathering,BusinessFlowState.review,NomalOrderPageBusinessFlowState.review_suspend) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(review_suspend, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.review, FlightOrderItemMajorStageState.processing));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.review_suspend).add(this);
		}
	},
	review_success("审核通过", PageOrderState.review_success,  true, true, AllQueryState.processed, ReviewQueryState.reviwePassed, IssueQueryState.unprcossed, PayQueryState.notPaid, GatheringQueryState.notGathering,BusinessFlowState.review,NomalOrderPageBusinessFlowState.review_success) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(review_success, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.review, FlightOrderItemMajorStageState.complete));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.review_success).add(this);
		}
	},
	review_fail("审核未通过", PageOrderState.review_fail,  true, true, AllQueryState.returned, ReviewQueryState.reviewReturned, IssueQueryState.unprcossed, PayQueryState.notPaid, GatheringQueryState.notGathering,BusinessFlowState.review,NomalOrderPageBusinessFlowState.review_fail) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(review_fail, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.review, FlightOrderItemMajorStageState.exception));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.review_fail).add(this);
		}
	},
	cancel("取消", PageOrderState.cancel,  false, false,null, null, null, null, null,BusinessFlowState.all,NomalOrderPageBusinessFlowState.cancel) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(cancel, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(FlightOrderItemMajorStage.cancel,
					FlightOrderItemMajorStageState.complete));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.cancel).add(this);
		}
	},
	req_frozen("请求冻结", PageOrderState.paying, true, true, null, null, IssueQueryState.unprcossed, PayQueryState.notPaid, GatheringQueryState.notGathering,null,null) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(req_frozen, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(FlightOrderItemMajorStage.pay,
					FlightOrderItemMajorStageState.processing));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.paying).add(this);
		}
	},
	frozen_success("冻结成功", PageOrderState.waiting_for_issue,  true, false, null, null,IssueQueryState.unprcossed, PayQueryState.paid, GatheringQueryState.notGathering,null,null) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(frozen_success, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.pay, FlightOrderItemMajorStageState.complete));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.waiting_for_issue).add(this);
		}
	},
	frozen_fail("冻结失败", PageOrderState.pay_fail, true, true, null, null, IssueQueryState.unprcossed, PayQueryState.notPaid, GatheringQueryState.notGathering,null,null) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(frozen_fail, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.pay, FlightOrderItemMajorStageState.exception));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.pay_fail).add(this);
		}
	},
	issuing("出票中", PageOrderState.issuing, false, false, AllQueryState.processing, null, IssueQueryState.processing, PayQueryState.paid, GatheringQueryState.notGathering,BusinessFlowState.issue,NomalOrderPageBusinessFlowState.issuing) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(issuing, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.issue_ticket, FlightOrderItemMajorStageState.processing));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.issuing).add(this);
		}
	},
	platform_issuing("平台出票中", PageOrderState.issuing, false, false, AllQueryState.processing, null, IssueQueryState.processing, PayQueryState.paid, GatheringQueryState.notGathering,BusinessFlowState.issue,NomalOrderPageBusinessFlowState.issuing) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(platform_issuing, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.issue_ticket, FlightOrderItemMajorStageState.processing));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.issuing).add(this);
		}
	},
	auto_issuing("自动出票中", PageOrderState.issuing, false, false, AllQueryState.processing, null, IssueQueryState.processing, PayQueryState.paid, GatheringQueryState.notGathering,BusinessFlowState.issue,NomalOrderPageBusinessFlowState.issuing) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(platform_issuing, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.issue_ticket, FlightOrderItemMajorStageState.processing));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.issuing).add(this);
		}
	},
	req_unfrozen_causedBy_reject("拒绝出票引发的请求解冻", PageOrderState.reject, false, false, AllQueryState.returned, null, IssueQueryState.rejectProcess, PayQueryState.notPaid, GatheringQueryState.notGathering,BusinessFlowState.issue,NomalOrderPageBusinessFlowState.reject) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(req_unfrozen_causedBy_reject, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.issue_ticket, FlightOrderItemMajorStageState.exception));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.reject).add(this);
		}
	},
	unfrozen_success_causedBy_reject("拒绝出票引发的解冻成功", PageOrderState.reject,  false, false, AllQueryState.returned, null, IssueQueryState.rejectProcess, PayQueryState.notPaid, GatheringQueryState.notGathering,BusinessFlowState.issue,NomalOrderPageBusinessFlowState.reject) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(unfrozen_success_causedBy_reject, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.issue_ticket, FlightOrderItemMajorStageState.exception));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.reject).add(this);
		}
	},
	unfrozen_fail_causedBy_reject("拒绝出票引发的解冻失败", PageOrderState.reject, false, false, AllQueryState.returned, null, IssueQueryState.rejectProcess, PayQueryState.notPaid, GatheringQueryState.notGathering,BusinessFlowState.issue,NomalOrderPageBusinessFlowState.reject) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(unfrozen_fail_causedBy_reject, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.issue_ticket, FlightOrderItemMajorStageState.exception));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.reject).add(this);
		}
	},
	waiting_for_issue("待出票", PageOrderState.waiting_for_issue, true, false, AllQueryState.submitted, null, IssueQueryState.unprcossed, PayQueryState.paid, GatheringQueryState.notGathering,BusinessFlowState.issue,NomalOrderPageBusinessFlowState.waiting_for_issue) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(waiting_for_issue, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.issue_ticket, FlightOrderItemMajorStageState.not_begin));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.issuing).add(this);
		}
	},
	waiting_for_auto_issue("待自动出票", PageOrderState.waiting_for_issue, true, false, AllQueryState.submitted, null, IssueQueryState.unprcossed, PayQueryState.paid, GatheringQueryState.notGathering,BusinessFlowState.issue,NomalOrderPageBusinessFlowState.waiting_for_issue) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(waiting_for_issue, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.issue_ticket, FlightOrderItemMajorStageState.not_begin));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.issuing).add(this);
		}
	},
	suspend("出票挂起", PageOrderState.suspend,  true, false, AllQueryState.processing, null, IssueQueryState.processing, PayQueryState.paid, GatheringQueryState.notGathering,BusinessFlowState.issue,NomalOrderPageBusinessFlowState.suspend) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(suspend, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.issue_ticket, FlightOrderItemMajorStageState.processing));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.suspend).add(this);
		}
	},
	req_gathering("请求收银", PageOrderState.tk_success, true, false, AllQueryState.processed, null, IssueQueryState.complete, PayQueryState.paid, GatheringQueryState.notGathering,BusinessFlowState.issue,NomalOrderPageBusinessFlowState.tk_success) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(req_gathering, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.complete, FlightOrderItemMajorStageState.complete));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.tk_success).add(this);
		}
	},
	gathering_success("收银成功", PageOrderState.tk_success, true, false, AllQueryState.processed, null, IssueQueryState.complete, PayQueryState.paid, GatheringQueryState.gathered,BusinessFlowState.issue,NomalOrderPageBusinessFlowState.tk_success) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(gathering_success, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.complete, FlightOrderItemMajorStageState.complete));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.tk_success).add(this);
		}
	},
	gathering_fail("收银失败", PageOrderState.tk_success, true, false, AllQueryState.processed, null, IssueQueryState.complete, PayQueryState.paid, GatheringQueryState.notGathering,BusinessFlowState.issue,NomalOrderPageBusinessFlowState.tk_success) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(gathering_fail, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.complete, FlightOrderItemMajorStageState.complete));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.tk_success).add(this);
		}
	},
	tk_success("出票成功", PageOrderState.tk_success,  true, false, AllQueryState.processed, null, IssueQueryState.complete, PayQueryState.paid, GatheringQueryState.notGathering,BusinessFlowState.issue,NomalOrderPageBusinessFlowState.tk_success) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(tk_success, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.issue_ticket, FlightOrderItemMajorStageState.complete));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.tk_success).add(this);
		}
	},
	reject("拒绝出票", PageOrderState.reject,  false, false, AllQueryState.returned, null, IssueQueryState.rejectProcess, PayQueryState.notPaid, GatheringQueryState.notGathering,BusinessFlowState.issue,NomalOrderPageBusinessFlowState.reject) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(reject, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.issue_ticket, FlightOrderItemMajorStageState.exception));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.reject).add(this);
		}
	},
	tmp_reject_when_reviewing("暂不能出票", PageOrderState.tmp_reject,  true, true, AllQueryState.returned, ReviewQueryState.reviewReturned, IssueQueryState.canNotProcess, PayQueryState.notPaid, GatheringQueryState.notGathering, BusinessFlowState.review, NomalOrderPageBusinessFlowState.tmp_reject) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(tmp_reject_when_reviewing, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.review, FlightOrderItemMajorStageState.exception));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.tmp_reject).add(this);
		}
	},
	tmp_reject_when_issuing("暂不能出票", PageOrderState.tmp_reject,  true, false, AllQueryState.returned, null, IssueQueryState.canNotProcess, PayQueryState.paid, GatheringQueryState.notGathering,BusinessFlowState.issue,NomalOrderPageBusinessFlowState.tmp_reject) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(tmp_reject_when_issuing, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.issue_ticket, FlightOrderItemMajorStageState.exception));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.tmp_reject).add(this);
		}
	},
	tmp_reject_handing("处理暂不能出票中", PageOrderState.tmp_reject,  true, false, AllQueryState.returned, null, IssueQueryState.canNotProcess, PayQueryState.paid, GatheringQueryState.notGathering,BusinessFlowState.issue,NomalOrderPageBusinessFlowState.tmp_reject) {
		@Override
		public void addToMajorStageMappingtTable() {
			majorStageMappingtTable.put(tmp_reject_handing, new ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>(
					FlightOrderItemMajorStage.issue_ticket, FlightOrderItemMajorStageState.exception));
		}

		@Override
		public void addToPageOrderStateMappingtTable() {
			pageOrderStateMappingtTable.get(PageOrderState.tmp_reject).add(this);
		}
	};

	private String desc;
	private PageOrderState pageOrderState;
	private boolean orderProcessingWhenOnThisState;
	private boolean cancelPnrWhenOnThisStateAndPayExpired;
	private AllQueryState allQueryState;
	private ReviewQueryState reviewQueryState;
	private IssueQueryState issueQueryState;
	private PayQueryState payQueryState;
	private GatheringQueryState gatheringQueryState;
	private BusinessFlowState businessFlowState;
	private NomalOrderPageBusinessFlowState nomalOrderPageBusinessFlowState;

	// 数据库订单状态与主流程的对应关系
	public static Map<FlightOrderItemState, ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>> majorStageMappingtTable = new HashMap<FlightOrderItemState, ImmutablePair<FlightOrderItemMajorStage, FlightOrderItemMajorStageState>>();
	// 页面订单状态与数据库订单状态的对应关系
	public static Map<PageOrderState, List<FlightOrderItemState>> pageOrderStateMappingtTable = new HashMap<PageOrderState, List<FlightOrderItemState>>();
	// 支付超时的状态
	public static List<FlightOrderItemState> PayExpiredState = new ArrayList<FlightOrderItemState>();
	static {
		for (PageOrderState pageOrderState : PageOrderState.values())
			pageOrderStateMappingtTable.put(pageOrderState, new ArrayList<FlightOrderItemState>());
		for (FlightOrderItemState state : FlightOrderItemState.values()) {
			if (state.canCancelPnrWhenOnThisStateAndPayExpired())
				PayExpiredState.add(state);
		}
		for (FlightOrderItemState state : FlightOrderItemState.values()) {
			state.addToMajorStageMappingtTable();
			state.addToPageOrderStateMappingtTable();
		}
	}

	/**
	 * 将自己跟主流程映射起来
	 */
	public abstract void addToMajorStageMappingtTable();

	/**
	 * 将自己跟页面订单状态映射起来
	 */
	public abstract void addToPageOrderStateMappingtTable();

	private FlightOrderItemState(String desc, PageOrderState pageOrderState,  boolean orderProcessingWhenOnThisState,
			boolean cancelPnrWhenOnThisStateAndPayExpired, AllQueryState allQueryState, ReviewQueryState reviewQueryState, IssueQueryState issueQueryState,
			PayQueryState payQueryState, GatheringQueryState gatheringQueryState,BusinessFlowState businessFlowState,NomalOrderPageBusinessFlowState nomalOrderPageBusinessFlowState) {
		this.desc = desc;
		this.pageOrderState = pageOrderState;
		this.allQueryState = allQueryState;
		this.reviewQueryState = reviewQueryState;
		this.issueQueryState = issueQueryState;
		this.payQueryState = payQueryState;
		this.gatheringQueryState = gatheringQueryState;
		this.businessFlowState = businessFlowState;
		this.nomalOrderPageBusinessFlowState = nomalOrderPageBusinessFlowState;
		this.orderProcessingWhenOnThisState = orderProcessingWhenOnThisState;
		this.cancelPnrWhenOnThisStateAndPayExpired = cancelPnrWhenOnThisStateAndPayExpired;
	}

	public String getDesc() {
		return desc;
	}

	public PageOrderState getPageOrderState() {
		return pageOrderState;
	}


	/**
	 * 订单是否仍在处理中
	 * 
	 * @return
	 */
	public boolean isOrderProcessingWhenOnThisState() {
		return orderProcessingWhenOnThisState;
	}

	/**
	 * 当白屏创建的订单位于该状态并且支付超时时，是否可以删除pnr
	 * 
	 * @return
	 */
	public boolean canCancelPnrWhenOnThisStateAndPayExpired() {
		return cancelPnrWhenOnThisStateAndPayExpired;
	}

	/**
	 * 返回支付超时处理时检查的订单状态
	 * 
	 * @return
	 */
	public static FlightOrderItemState[] getPayExpiredState() {
		return PayExpiredState.toArray(new FlightOrderItemState[0]);
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
	  
	
	public static List<FlightOrderItemState>  getItemStates(AllQueryState allQueryState){
	    List<FlightOrderItemState> result = new ArrayList<FlightOrderItemState>();
	    for(FlightOrderItemState state : FlightOrderItemState.values())
	        if(allQueryState == state.getAllQueryState())
	            result.add(state);
        return result;
	    
	}
	public static List<FlightOrderItemState>  getItemReviewStates(ReviewQueryState reviewQueryState){
        List<FlightOrderItemState> result = new ArrayList<FlightOrderItemState>();
        for(FlightOrderItemState state : FlightOrderItemState.values())
            if(reviewQueryState == state.getReviewQueryState())
                result.add(state);
        return result;
        
    }
	
	   public static List<FlightOrderItemState>  getItemIssueStates(IssueQueryState issueQueryState){
	        List<FlightOrderItemState> result = new ArrayList<FlightOrderItemState>();
	        for(FlightOrderItemState state : FlightOrderItemState.values())
	            if(issueQueryState == state.getIssueQueryState())
	                result.add(state);
	        return result;
	        
	    }
	   
	   public static List<FlightOrderItemState>  getItemPayStates(PayQueryState payQueryState){
           List<FlightOrderItemState> result = new ArrayList<FlightOrderItemState>();
           for(FlightOrderItemState state : FlightOrderItemState.values())
               if(payQueryState == state.getPayQueryState())
                   result.add(state);
           return result;
           
       }
	   public static List<FlightOrderItemState>  getItemGatheringStates(GatheringQueryState gatheringQueryState){
           List<FlightOrderItemState> result = new ArrayList<FlightOrderItemState>();
           for(FlightOrderItemState state : FlightOrderItemState.values())
               if(gatheringQueryState == state.getGatheringQueryState())
                   result.add(state);
           return result;
           
       }

public NomalOrderPageBusinessFlowState getNomalOrderPageBusinessFlowState() {
    return nomalOrderPageBusinessFlowState;
}

public void setNomalOrderPageBusinessFlowState(NomalOrderPageBusinessFlowState nomalOrderPageBusinessFlowState) {
    this.nomalOrderPageBusinessFlowState = nomalOrderPageBusinessFlowState;
}

public BusinessFlowState getBusinessFlowState() {
    return businessFlowState;
}

public void setBusinessFlowState(BusinessFlowState businessFlowState) {
    this.businessFlowState = businessFlowState;
}
       
   }
   
   

