package com.travelzen.framework.core.util;

import java.nio.ByteBuffer;

public class ByteUtil {

	public static byte[] longToByte(long number) {
		
		byte[] bytes = ByteBuffer.allocate(8).putLong(number).array();
		
		return bytes;
		
//		long temp = number;
//		byte[] b = new byte[8];
//		for (int i = 0; i < b.length; i++) {
//			b[i] = new Long(temp & 0xff).byteValue();//
//			temp = temp >> 8; // 向右移8位
//		}
//		return b;
	}

	// byte数组转成long
	public static long byteToLong(byte[] b) {
		
		ByteBuffer bb = ByteBuffer.wrap( b);
		long l = bb.getLong();
		return l;
		
//		long s = 0;
//		long s0 = b[0] & 0xff;// 最低位
//		long s1 = b[1] & 0xff;
//		long s2 = b[2] & 0xff;
//		long s3 = b[3] & 0xff;
//		long s4 = b[4] & 0xff;// 最低位
//		long s5 = b[5] & 0xff;
//		long s6 = b[6] & 0xff;
//		long s7 = b[7] & 0xff;
//
//		s1 <<= 8;
//		s2 <<= 16;
//		s3 <<= 24;
//		s4 <<= 8 * 4;
//		s5 <<= 8 * 5;
//		s6 <<= 8 * 6;
//		s7 <<= 8 * 7;
//		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
//		return s;
	}

	/**
	 * 注释：int到字节数组的转换！
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] intToByte(int number) {
		int temp = number;
		byte[] b = new byte[4];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Integer(temp & 0xff).byteValue();//
			temp = temp >> 8; // 向右移8位
		}
		return b;
	}

	/**
	 * 注释：字节数组到int的转换！
	 * 
	 * @param b
	 * @return
	 */
	public static int byteToInt(byte[] b) {
		int s = 0;
		int s0 = b[0] & 0xff;// 最低位
		int s1 = b[1] & 0xff;
		int s2 = b[2] & 0xff;
		int s3 = b[3] & 0xff;
		s3 <<= 24;
		s2 <<= 16;
		s1 <<= 8;
		s = s0 | s1 | s2 | s3;
		return s;
	}

	/**
	 * 注释：short到字节数组的转换！
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] shortToByte(short number) {
		int temp = number;
		byte[] b = new byte[2];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Integer(temp & 0xff).byteValue();
			//
			temp = temp >> 8; // 向右移8位
		}
		return b;
	}

	/**
	 * 注释：字节数组到short的转换！
	 * 
	 * @param b
	 * @return
	 */
	public static short byteToShort(byte[] b) {
		short s = 0;
		short s0 = (short) (b[0] & 0xff);// 最低位
		short s1 = (short) (b[1] & 0xff);
		s1 <<= 8;
		s = (short) (s0 | s1);
		return s;
	}

}
