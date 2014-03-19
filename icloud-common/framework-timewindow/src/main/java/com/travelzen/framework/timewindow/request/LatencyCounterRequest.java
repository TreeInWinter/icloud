package com.travelzen.framework.timewindow.request;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

import com.travelzen.framework.timewindow.actor.LatencyCounterActor;
import com.travelzen.framework.timewindow.actor.String2LongCounterActor;

public class LatencyCounterRequest extends
		Request<String, String2LongCounterActor> {

	public RequestType requestType;
	public String name;
	public long timestamp;

	public LatencyCounterRequest(RequestType requestType, String name,
			long timestamp) {
		this.name = name;
		this.requestType = requestType;
		this.timestamp = timestamp;
	}

	public LatencyCounterRequest(RequestType requestType, String name) {
		this.name = name;
		this.requestType = requestType;
	}

	public LatencyCounterRequest(RequestType requestType) {
		this.requestType = requestType;
	}

	public LatencyCounterRequest(RequestType requestType, long timestamp) {
		this.requestType = requestType;
		this.timestamp = timestamp;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
		LatencyCounterActor a = (LatencyCounterActor) targetActor;
		a.processRequest(this, rp);
	}

	@Override
	public boolean isTargetType(Actor targetActor) {
		return targetActor instanceof LatencyCounterActor;
	}

}