package com.travelzen.framework.core.dict;

import java.util.ArrayList;
import java.util.List;


public enum PageOrderState {
//	init("新建"), 
	waiting_for_pay("待支付"),
	waiting_for_review("待审核"),
	reviewing("审核中"), 
	review_suspend("审核挂起"),
	review_success("审核通过"),
	review_fail("审核未通过"), 
	cancel("取消"), 
	paying("支付中"), 
	pay_fail("支付失败"),
	waiting_for_issue("未出票"), 
	suspend("挂起"),
	issuing("出票中"), 
	tk_success("完成出票"), 
	reject("拒绝出票"), 
	tmp_reject("暂不出票");
	
	private String desc;
	
	private PageOrderState(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	public static List<PageOrderState> getPageOrderStateListForDomesticGeneralQuery(){
	    List<PageOrderState> pageOrderStateList = new ArrayList<PageOrderState>();
	    for(PageOrderState state : PageOrderState.values()){
	        if(state != suspend && state != review_suspend)
	            pageOrderStateList.add(state);
	    }
	    return pageOrderStateList;
	}
	/**
	 * 出票相关的状态
	 * @return
	 */
	public static PageOrderState[] getIssueRelatedState() {
		return new PageOrderState[]{waiting_for_issue, tmp_reject, issuing};
	}
}
