package com.travelzen.framework.thrift.protocol;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMessage;
import org.apache.thrift.transport.TTransport;

import com.travelzen.framework.core.util.TZUtil;
import com.travelzen.framework.logger.ri.RequestIdentityHolder;

public class RIThriftProtocol extends TBinaryProtocol {

	protected static final char DELIMETER = '|';

	public RIThriftProtocol(TTransport trans) {
		super(trans);
	}

	public RIThriftProtocol(TTransport trans, boolean strictRead, boolean strictWrite) {
		super(trans, strictRead, strictWrite);
	}

	@Override
	public void writeMessageBegin(TMessage message) throws TException {
		String ri = RequestIdentityHolder.get();
		if (TZUtil.isEmpty(ri)) {
			super.writeMessageBegin(message);
		} else {
			String fixedName = message.name + DELIMETER + RequestIdentityHolder.get();
			TMessage tm = new TMessage(fixedName, message.type, message.seqid);
			super.writeMessageBegin(tm);
		}
	}

	@Override
	public void writeMessageEnd() {
		super.writeMessageEnd();
	}

	@Override
	public TMessage readMessageBegin() throws TException {
		TMessage tm = super.readMessageBegin();
		int idx = tm.name.lastIndexOf(DELIMETER);
		if (idx < 0) {
			return tm;
		} else {
			String reqId = tm.name.substring(idx + 1);
			RequestIdentityHolder.set(reqId);
			return new TMessage(tm.name.substring(0, idx), tm.type, tm.seqid);
		}
	}

	@Override
	public void readMessageEnd() {
		super.readMessageEnd();
	}

}
