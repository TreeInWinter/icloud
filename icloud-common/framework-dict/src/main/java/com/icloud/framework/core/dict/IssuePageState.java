package com.icloud.framework.core.dict;

import java.util.ArrayList;
import java.util.List;

public enum IssuePageState {
	waiting_for_issue("待出票"), issuing("出票中"), ;
	private String desc;

	private IssuePageState(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static List<String> getRelatedWaitingForIssueState() {
		List<String> list = new ArrayList<String>();
		list.add(FlightOrderItemState.waiting_for_issue.name());
		list.add(EndorseFlightOrderItemState.waiting_for_process.name());
		return list;
	}

	public static List<String> getRelatedInternationalIssuingState() {
		List<String> list = new ArrayList<String>();
		list.add(FlightOrderItemState.issuing.name());
		list.add(FlightOrderItemState.suspend.name());
		list.add(EndorseFlightOrderItemState.processing.name());
		list.add(EndorseFlightOrderItemState.process_suspend.name());
		return list;
	}

	public static List<String> getRelatedInternationalIssueState() {
		List<String> list = new ArrayList<String>();
		list.add(FlightOrderItemState.issuing.name());
		list.add(FlightOrderItemState.suspend.name());
		list.add(FlightOrderItemState.waiting_for_issue.name());
		list.add(EndorseFlightOrderItemState.waiting_for_process.name());
		list.add(EndorseFlightOrderItemState.processing.name());
		list.add(EndorseFlightOrderItemState.process_suspend.name());
		return list;
	}

	public static List<String> getRelatedDomesticIssueState() {
		List<String> list = new ArrayList<String>();
		list.add(FlightOrderItemState.waiting_for_issue.name());
		list.add(FlightOrderItemState.issuing.name());
		list.add(FlightOrderItemState.platform_issuing.name());
		return list;
	}

}
