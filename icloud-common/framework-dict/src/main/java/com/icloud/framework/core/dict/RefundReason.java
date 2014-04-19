/**
 *
 * Description:
 * Copyright (c) 2013
 * Company:真旅网
 * @author renshui
 * @version 1.0
 * @date 2013-7-7
 */
package com.icloud.framework.core.dict;

import java.util.ArrayList;
import java.util.List;

public enum RefundReason {
	refundAtSaleDate("当日作废"),
	refundOnPassengerDemand("按客票规定自愿退票"),
	refundBecauseOfAirPlainProblem("民航原因（取消/延误）"),
	changeTicket("客票换开"),
	fullRefundBecauseOfDisease("因病全退"),
	onlyRefundTaxPayment("只退税款"),
	other("其它原因"),
	;

	private String desc;

	private RefundReason(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public static List<RefundReason> getAllRefundReasons(boolean isDomestic) {
		List<RefundReason> allRefundReasons = new ArrayList<RefundReason>();
		allRefundReasons.add(refundOnPassengerDemand);
		allRefundReasons.add(refundBecauseOfAirPlainProblem);
		allRefundReasons.add(changeTicket);
		allRefundReasons.add(fullRefundBecauseOfDisease);
		if(!isDomestic)
			allRefundReasons.add(onlyRefundTaxPayment);
		allRefundReasons.add(other);
		return allRefundReasons;
	}
}
