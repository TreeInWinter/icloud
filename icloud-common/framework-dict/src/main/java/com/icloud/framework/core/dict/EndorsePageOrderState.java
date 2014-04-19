package com.icloud.framework.core.dict;


public enum EndorsePageOrderState {
	init("新建"),
	waiting_for_review("待审核"),
	reviewing("审核中"),
	review_success("审核通过"),
	review_fail("审核未通过"),
	cancel("取消"),
	waiting_for_process("未处理"),
	processing("改签处理中"),
	process_suspend("处理挂起"),
	review_suspend("审核挂起"),
	endorse_complete("改签完成"),
	returned("退回");

	private String desc;

	private EndorsePageOrderState(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	
	public static EndorsePageOrderState[] getEndorseReviewRelatedState(){
        return new EndorsePageOrderState[]{waiting_for_review,reviewing};
    }
    
    public static EndorsePageOrderState[] getEndorseProcessRelatedState(){
        return new EndorsePageOrderState[]{waiting_for_process,processing};
    }
}
