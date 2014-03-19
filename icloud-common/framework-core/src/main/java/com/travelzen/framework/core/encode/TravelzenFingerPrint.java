package com.travelzen.framework.core.encode;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.google.common.primitives.UnsignedLong;

public class TravelzenFingerPrint {

	static public UnsignedLong fingerPrint4UnsignedLong(String text) {
		return UnsignedLong.fromLongBits(fingerPrint4Long(text));
	}

	static public long fingerPrint4Long(String text) {
		byte[] value = new byte[0];
		try {
			value = text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		long hash = MurmurHash.hash64(value, value.length, 19820125);
		
		return hash;
	}

	static public byte[] fingerPrint(String text) {
		// String text ="123";
		// String text ="一二三";

		long hash = fingerPrint4Long(text);

		byte[] bytes = ByteBuffer.allocate(8).putLong(hash).array();
		
		return bytes;

		// byte input[] = {(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff,
		// (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff}; // 64 bits, signed
		// long = -1
		
//		BigInteger signed = new BigInteger(bytes);
//		BigInteger unsigned = new BigInteger(1, bytes);
		
		// System.out.println("Max Long : " + Long.MAX_VALUE);
		// System.out.println("Min Long : " + Long.MIN_VALUE);
		// System.out.println("Signed   : " + signed);
		// System.out.println("Unsigned : " + unsigned);

		// System.out.println("Unsigned > Long.MAX_VALUE ? " +
		// ((unsigned.compareTo(BigInteger.valueOf(Long.MAX_VALUE))==1) ? "true"
		// : "false"));
		// System.out.println("Signed == -1 ? " +
		// (signed.compareTo(BigInteger.valueOf(-1)) == 0 ? "true" : "false"));

		//

		// System.out.println(hash);
		//
		// System.out.println(ByteUtil.longToByte(hash));

	}

	public static void main(String[] args) {
		long fingerPrint = fingerPrint4Long("http://weibo.com/1827370530/yzXVjaVJ5");
		UnsignedLong ul = UnsignedLong.fromLongBits(fingerPrint);
		System.out.println(fingerPrint);
		System.out.println(ul);
	}
}
