package com.travelzen.framework.timewindow.request;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

import com.travelzen.framework.timewindow.actor.LatencyCounterActor;
import com.travelzen.framework.timewindow.actor.String2StringListTimeLimitHolderActor;

public class String2StringListRequest extends
		Request<String, String2StringListTimeLimitHolderActor> {

	public RequestType requestType;
	public String name;
	public String value;
	public long timestamp;

	public String2StringListRequest(RequestType requestType, String name,
			String value, long timestamp) {
		this.requestType = requestType;
		this.name = name;
		this.value = value;
		this.timestamp = timestamp;
	}

	public String2StringListRequest(RequestType requestType, long timestamp) {
		this.requestType = requestType;
		this.timestamp = timestamp;
	}

	public String2StringListRequest(RequestType requestType, String name) {
		this.requestType = requestType;
		this.name = name;
	}

	public String2StringListRequest(RequestType requestType) {
		this.requestType = requestType;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
		String2StringListTimeLimitHolderActor a = (String2StringListTimeLimitHolderActor) targetActor;
		a.processRequest(this, rp);
	}

	@Override
	public boolean isTargetType(Actor targetActor) {
		return targetActor instanceof LatencyCounterActor;
	}
}