package com.travelzen.framework.thrift.protocol;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TTransport;

public class RIThriftProtocolFactory implements TProtocolFactory {

	private static final long serialVersionUID = -1818838844032038424L;

	protected boolean strictRead_ = false;
	protected boolean strictWrite_ = true;
	protected int readLength_;

    public RIThriftProtocolFactory() {
      this(false, true);
    }

    public RIThriftProtocolFactory(boolean strictRead, boolean strictWrite) {
      this(strictRead, strictWrite, 0);
    }

    public RIThriftProtocolFactory(boolean strictRead, boolean strictWrite, int readLength) {
      this.strictRead_ = strictRead;
      this.strictWrite_ = strictWrite;
      this.readLength_ = readLength;
    }

	@Override
	public TProtocol getProtocol(TTransport trans) {
	      TBinaryProtocol proto = new RIThriftProtocol(trans, strictRead_, strictWrite_);
	      if (readLength_ != 0) {
	        proto.setReadLength(readLength_);
	      }
	      return proto;
	}


}
