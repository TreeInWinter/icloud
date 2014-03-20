package com.travelzen.framework.core.dict;

public enum ETicketState {
    normal("正常"),
    vt("废票"),
    refund("退票"),
    reschedule("改期"),
    upgrade("升舱"),
    endorse("改签"),
    adjust("调帐"),
    ;
	private String desc;

	private ETicketState(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	public static boolean isEndorse(ETicketState state){
		if(state == null)
			return false;
		return state == reschedule || state == upgrade || state == endorse;
	}
	public static boolean isEndorse(String state){
		if(state == null)
			return false;
		return isEndorse(ETicketState.valueOf(state));
	}
	
}
