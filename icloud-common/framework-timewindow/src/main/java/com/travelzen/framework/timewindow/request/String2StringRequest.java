package com.travelzen.framework.timewindow.request;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

import com.travelzen.framework.timewindow.actor.String2LongCounterActor;
import com.travelzen.framework.timewindow.actor.String2StringListHolderActor;


public class String2StringRequest extends Request<String, String2StringListHolderActor> {

	
	public RequestType requestType;
	
	public  String  entry ;
	
	public long timestamp;

	public String2StringRequest( RequestType requestType, String  entry, long timestamp ) {
		this.entry  = entry ;
		this.requestType = requestType;
		this.timestamp = timestamp;
	}
	
	public String2StringRequest( RequestType requestType) {
		this.requestType  = requestType ;
	}
	

	@Override
	@SuppressWarnings({ "rawtypes" })
	public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
		String2StringListHolderActor a = (String2StringListHolderActor) targetActor;
		a.processRequest(this, rp);
	}

	@Override
	public boolean isTargetType(Actor targetActor) {
		return targetActor instanceof String2LongCounterActor;
	}

 

 
}