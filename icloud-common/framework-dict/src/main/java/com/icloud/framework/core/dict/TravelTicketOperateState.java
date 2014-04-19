package com.icloud.framework.core.dict;

public enum TravelTicketOperateState {
	inHouse("入库"),//log
	inHouseCancel("取消入库"),
	applying("申请中"),
	applyCancel("取消申请"),
	use("使用"),
	distribute("分发"),//log
	distributeCancel("分发退回"),//log
	distributeConfirm("分发确认"),//log
	distributeHand("主动分发"),//log
	extractApply("抽调申请"),//log
	extractApplyCancel("抽调申请取消"),//log
	extractCancel("抽调退回"),//log
	extractConfirm("抽调确认"),//log
	invalid("作废"),//log
	invalidCancel("取消作废"),//log
	getReturn("回收"),//log
	allDistribute("已全部分发")//log
	;
	
	private String state;
	
	private TravelTicketOperateState(String state) {
		this.state = state;
	}

	public String getDesc() {
		return state;
	}
}
