package com.travelzen.framework.timewindow.request;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;
import org.apache.commons.lang3.mutable.MutableLong;
import org.javatuples.Pair;

import com.travelzen.framework.timewindow.actor.String2LongCounterActor;


public class String2LongCounterRequest extends Request<String, String2LongCounterActor> {

	
	public RequestType requestType;
	
	public Pair<String, MutableLong> entry ;
	
	
	public long timestamp;

	public String2LongCounterRequest( RequestType requestType, Pair<String, MutableLong>  entry, long timestamp ) {
		this.entry  = entry ;
		this.requestType = requestType;
		this.timestamp = timestamp;
	}
	
	public String2LongCounterRequest( RequestType requestType) {
		this.requestType  = requestType ;
	}
	
	public String2LongCounterRequest( RequestType requestType, long timestamp ) {
		this.requestType  = requestType ;
		this.timestamp = timestamp;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
		String2LongCounterActor a = (String2LongCounterActor) targetActor;
		a.processRequest(this, rp);
	}

	@Override
	public boolean isTargetType(Actor targetActor) {
		return targetActor instanceof String2LongCounterActor;
	}

 

 
}