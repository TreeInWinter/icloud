/**
 * 
 * Description: 
 * Copyright (c) 2013
 * Company:真旅网
 * @author renshui
 * @version 1.0
 * @date 2013-7-25
 */
package com.travelzen.framework.core.util;

import org.junit.Test;

public class ValidationUtilTest {

	@Test
	public void test_validateTicketNo() {
		ValidationUtil.validatePattern(ValidationUtil.RULE_TICKETNO, "0123456789", ValidationUtil.MESSAGE_PATTERN_TICKETNO);
		ValidationUtil.validatePattern(ValidationUtil.RULE_TICKETNO, "9990123456789", ValidationUtil.MESSAGE_PATTERN_TICKETNO);
		ValidationUtil.validatePattern(ValidationUtil.RULE_TICKETNO, "999-0123456789", ValidationUtil.MESSAGE_PATTERN_TICKETNO);
	}
	@Test(expected=Exception.class)
	public void test_validateTicketNo_error() {
		ValidationUtil.validatePattern(ValidationUtil.RULE_TICKETNO, "012345678a", ValidationUtil.MESSAGE_PATTERN_TICKETNO);
		ValidationUtil.validatePattern(ValidationUtil.RULE_TICKETNO, "999012345678", ValidationUtil.MESSAGE_PATTERN_TICKETNO);
		ValidationUtil.validatePattern(ValidationUtil.RULE_TICKETNO, "990123456789", ValidationUtil.MESSAGE_PATTERN_TICKETNO);
	}
	public void test_validateConsecutiveSeqNo(){
		ValidationUtil.validatePattern(ValidationUtil.RULE_CONSECUTIVESEQNO, "C1", ValidationUtil.MESSAGE_PATTERN_CONSECUTIVESEQNO);
		ValidationUtil.validatePattern(ValidationUtil.RULE_CONSECUTIVESEQNO, "C2", ValidationUtil.MESSAGE_PATTERN_CONSECUTIVESEQNO);
		ValidationUtil.validatePattern(ValidationUtil.RULE_CONSECUTIVESEQNO, "C10", ValidationUtil.MESSAGE_PATTERN_CONSECUTIVESEQNO);
		ValidationUtil.validatePattern(ValidationUtil.RULE_CONSECUTIVESEQNO, "C100", ValidationUtil.MESSAGE_PATTERN_CONSECUTIVESEQNO);
	}
	@Test(expected=Exception.class)
	public void test_validateConsecutiveSeqNo_error(){
		ValidationUtil.validatePattern(ValidationUtil.RULE_CONSECUTIVESEQNO, "C01", ValidationUtil.MESSAGE_PATTERN_CONSECUTIVESEQNO);
		ValidationUtil.validatePattern(ValidationUtil.RULE_CONSECUTIVESEQNO, "abcd", ValidationUtil.MESSAGE_PATTERN_CONSECUTIVESEQNO);
	}
	@Test
	public void test_innerMemo(){
		ValidationUtil.validatePattern(ValidationUtil.RULE_INNERMEMO, "abc\r\nabc", ValidationUtil.MESSAGE_PATTERN_INNERMEMO);
	}
	@Test
	public void validatePattern(){
		ValidationUtil.validatePattern(ValidationUtil.RULE_PNR, "ABCDEF", ValidationUtil.MESSAGE_PATTERN_PNR);
	}
	@Test
	public void validateRequired(){
		ValidationUtil.validateRequired("a", ValidationUtil.MESSAGE_REQUIRED_PNR);
	}
}
