package com.icloud.framework.thrift.util;

import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;

public class SerializationUtil {
	private static Logger logger = LoggerFactory
			.getLogger(SerializationUtil.class);

	private static final Factory factory = new TBinaryProtocol.Factory();

	/**
	 * throws TException 对对象base进行序列化，保存到bytes中
	 */
	public static byte[] base2bytes(TBase base) {
		try {
			return new TSerializer(factory).serialize(base);
		} catch (TException e) {
			logger.error(e.getMessage());
			throw Throwables.propagate(e);
		}
	}

	/**
	 * throws TException
	 * 
	 * 对bytes中保存的对象进行反序列化，保存到base中
	 */
	public static void bytes2base(TBase base, byte[] bytes) {
		try {
			new TDeserializer(factory).deserialize(base, bytes);
		} catch (TException e) {
			logger.error(e.getLocalizedMessage(), e);
			throw Throwables.propagate(e);
		}
	}
}
