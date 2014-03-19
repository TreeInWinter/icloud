package com.travelzen.framework.logger.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

public class MessagePrefixFilter extends AbstractMatcherFilter<ILoggingEvent> {

	private String prefix;

	@Override
	public FilterReply decide(ILoggingEvent event) {
		String message = event.getMessage();
        if (!isStarted()) {
        	return FilterReply.NEUTRAL;
        } else if (message.startsWith(prefix)) {
        	return super.getOnMatch();
        } else {
        	return super.getOnMismatch();
        }
	}

	public void setPrefix(String arg) {
		this.prefix = arg;
	}

	public void start() {
		if (this.prefix != null && this.prefix.length() > 0) {
			super.start();
		}
	}

}
