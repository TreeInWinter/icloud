package com.icloud.search.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class MD5Util {
	// static BASE64Encoder base64en = new BASE64Encoder();
	public static final int LIMIT = 2000;
	static MessageDigest md5;
	static {
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getMD5ForPhoto(byte[] content) {
		byte[] partdata = getPartDate(content, LIMIT);
		return encryptJDKMD5ByByte(partdata);
	}

	public static byte[] getPartDate(byte[] content, int limit) {
		if (content == null || limit < 1 || content.length < limit) {
			return null;
		}
		byte[] partdata = new byte[2 * limit];
		for (int i = 0; i < limit; i++) {
			partdata[i] = content[i];
		}
		int len = content.length;
		int start = len - limit;
		for (int i = 0; i < limit; i++) {
			partdata[limit + i] = content[start + i];
		}
		return partdata;
	}

	public static String ConvertMD5(String str)
			throws UnsupportedEncodingException {
		md5.update(str.getBytes("utf-8"));
		byte[] tmp = md5.digest();
		// System.out.println(Base64.decodeInteger(tmp));
		// System.out.println(Base64.encodeBase64(Base64.decodeBase64("9492087")));
		String newstr = Base64.encodeBase64URLSafeString(tmp);
		// String hello = "9492087";
		// BigInteger in = new BigInteger(hello);
		// System.out.println(Base64.encodeBase64String(Base64.encodeInteger(in)));
		// return Base64.encodeBase64URLSafeString(tmp);
		// String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
		return newstr;
	}
	

	public static BigInteger ConvertTitleMD5(String str)
			throws UnsupportedEncodingException {
		md5.update(str.getBytes("utf-8"));
		byte[] tmp = md5.digest();
		BigInteger bigInt = Base64.decodeInteger(tmp);
		return bigInt;
	}

	public static String encryptJDKMD5ByByte(byte[] str) {
		StringBuffer buf = new StringBuffer("");
		if (str == null) {
			return null;
		}
		try {
			md5.update(str);
			byte b[] = md5.digest();
			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				// System.out.println(Integer.toHexString(i));
				buf.append(Integer.toHexString(i));
			}

		} catch (Exception e) {
			System.out.println("----encryptJDKMD5 is error : " + e);
		}
		return buf.toString();
	}

	public static String encryptJDKMD5(String str) {
		StringBuffer buf = new StringBuffer("");
		if (null == str) {
			return str;
		}
		try {
			// MessageDigest md = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes("utf-8"));
			byte b[] = md5.digest();

			// System.out.println(b.length);
			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				// System.out.println(i);
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				// System.out.println(Integer.toHexString(i));
				buf.append(Integer.toHexString(i));
			}

		} catch (Exception e) {
			System.out.println("----encryptJDKMD5 is error : " + e);
		}
		return buf.toString();
	}

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		// TODO Auto-generated method stub
		// MessageDigest md5 = MessageDigest.getInstance("MD5");
		// String url = "�й��ѧԺ���㼼���о���fasfasdfasdfasdfal����÷��İ�";
		String url2 = "非主流美女性感图片afsdsdfsadfsadfasdfasdf";
		// 7e5a05646fe8ebeb770c9dd9c00783e1
		// 23f7162173bb0a636b8b377f171bc22b
		// ���ܺ���ַ�
		String newstr = ConvertMD5(url2);
		System.out.println(newstr);
		// System.out.println(getJDKMD5(newstr));
		String newstr2 = encryptJDKMD5(url2);
		// System.out.println(newstr);
		System.out.println(newstr2);
	}
}
