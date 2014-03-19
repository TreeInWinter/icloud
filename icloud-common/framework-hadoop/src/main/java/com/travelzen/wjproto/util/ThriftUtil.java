package com.travelzen.wjproto.util;

import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;

@SuppressWarnings("rawtypes")
public class ThriftUtil {
	
	private static final Factory factory = new TBinaryProtocol.Factory();
	public static byte[] base2bytes(TBase base) throws TException {
		return new TSerializer(factory).serialize(base);
	}

	public static void bytes2base(TBase base, byte[] bytes) throws TException {
		base.clear();
		new TDeserializer(factory).deserialize(base, bytes);
	}

}
