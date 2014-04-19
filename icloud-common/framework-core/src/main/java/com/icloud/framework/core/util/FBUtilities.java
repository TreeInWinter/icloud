/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.icloud.framework.core.util;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;

public class FBUtilities {
	private static Logger logger = LoggerFactory.getLogger(FBUtilities.class);

	public static final BigInteger TWO = new BigInteger("2");

	public static final ByteBuffer EMPTY_BYTE_BUFFER = ByteBuffer
			.wrap(ArrayUtils.EMPTY_BYTE_ARRAY);

	public static final int MAX_UNSIGNED_SHORT = 0xFFFF;

	/**
	 * Parses a string representing either a fraction, absolute value or
	 * percentage.
	 */
	public static double parseDoubleOrPercent(String value) {
		if (value.endsWith("%")) {
			return Double.parseDouble(value.substring(0, value.length() - 1)) / 100;
		} else {
			return Double.parseDouble(value);
		}
	}

	public static String getIp() {
		String localip = null;// 本地IP，如果没有配置外网IP则返回它
		String netip = null;// 外网IP
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface
					.getNetworkInterfaces();
			InetAddress ip = null;
			boolean finded = false;// 是否找到外网IP
			while (netInterfaces.hasMoreElements() && !finded) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> address = ni.getInetAddresses();
				while (address.hasMoreElements()) {
					ip = address.nextElement();
 
					if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
							&& ip.getHostAddress().indexOf(":") == -1) {// 外网IP
						netip = ip.getHostAddress();
						finded = true;
						break;
					} else if (ip.isSiteLocalAddress()
							&& !ip.isLoopbackAddress()
							&& ip.getHostAddress().indexOf(":") == -1) {// 内网IP
						localip = ip.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		if (netip != null && !"".equals(netip)) {
			return netip;
		} else {
			return localip;
		}
	}

	/**
	 * @param fractOrAbs
	 *            A double that may represent a fraction or absolute value.
	 * @param total
	 *            If fractionOrAbs is a fraction, the total to take the fraction
	 *            from
	 * @return An absolute value which may be larger than the total.
	 */
	public static long absoluteFromFraction(double fractOrAbs, long total) {
		if (fractOrAbs < 0)
			throw new UnsupportedOperationException(
					"unexpected negative value " + fractOrAbs);

		if (0 < fractOrAbs && fractOrAbs <= 1) {
			// fraction
			return Math.max(1, (long) (fractOrAbs * total));
		}

		// absolute
		assert fractOrAbs >= 1 || fractOrAbs == 0;
		return (long) fractOrAbs;
	}

	public static ByteBuffer toByteBuffer(int i) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) ((i >>> 24) & 0xFF);
		bytes[1] = (byte) ((i >>> 16) & 0xFF);
		bytes[2] = (byte) ((i >>> 8) & 0xFF);
		bytes[3] = (byte) (i & 0xFF);
		return ByteBuffer.wrap(bytes);
	}

	public static int byteBufferToInt(ByteBuffer bytes) {
		if (bytes.remaining() < 4) {
			throw new IllegalArgumentException(
					"An integer must be 4 bytes in size.");
		}
		int n = 0;
		for (int i = 0; i < 4; ++i) {
			n <<= 8;
			n |= bytes.array()[bytes.position() + bytes.arrayOffset() + i] & 0xFF;
		}
		return n;
	}

	public static int compareUnsigned(byte[] bytes1, byte[] bytes2,
			int offset1, int offset2, int len1, int len2) {
		if (bytes1 == null) {
			return bytes2 == null ? 0 : -1;
		}
		if (bytes2 == null)
			return 1;

		int minLength = Math.min(len1 - offset1, len2 - offset2);
		for (int x = 0, i = offset1, j = offset2; x < minLength; x++, i++, j++) {
			if (bytes1[i] == bytes2[j])
				continue;
			// compare non-equal bytes as unsigned
			return (bytes1[i] & 0xFF) < (bytes2[j] & 0xFF) ? -1 : 1;
		}
		if ((len1 - offset1) == (len2 - offset2))
			return 0;
		else
			return ((len1 - offset1) < (len2 - offset2)) ? -1 : 1;
	}

	/**
	 * @return The bitwise XOR of the inputs. The output will be the same length
	 *         as the longer input, but if either input is null, the output will
	 *         be null.
	 */
	public static byte[] xor(byte[] left, byte[] right) {
		if (left == null || right == null)
			return null;
		if (left.length > right.length) {
			byte[] swap = left;
			left = right;
			right = swap;
		}

		// left.length is now <= right.length
		byte[] out = Arrays.copyOf(right, right.length);
		for (int i = 0; i < left.length; i++) {
			out[i] = (byte) ((left[i] & 0xFF) ^ (right[i] & 0xFF));
		}
		return out;
	}

	public static void writeByteArray(ByteBuffer bytes, DataOutput out)
			throws IOException {
		out.writeInt(bytes.remaining());
		out.write(bytes.array(), bytes.position() + bytes.arrayOffset(),
				bytes.remaining());
	}

	public static ByteBuffer readByteArray(DataInput in) throws IOException {
		int length = in.readInt();
		if (length < 0) {
			throw new IOException("Corrupt (negative) value length encountered");
		}

		ByteBuffer bb = ByteBuffer.allocate(length);
		if (length > 0) {
			in.readFully(bb.array(), bb.position(), bb.remaining());
		}
		return bb;
	}

	public static void writeShortByteArray(ByteBuffer name, DataOutput out) {
		int length = name.remaining();
		assert 0 <= length && length <= MAX_UNSIGNED_SHORT;
		try {
			out.writeByte((length >> 8) & 0xFF);
			out.writeByte(length & 0xFF);
			out.write(name.array(), name.position() + name.arrayOffset(),
					name.remaining());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/** @return An unsigned short in an integer. */
	private static int readShortLength(DataInput in) throws IOException {
		int length = (in.readByte() & 0xFF) << 8;
		return length | (in.readByte() & 0xFF);
	}

	public static ByteBuffer readShortByteArray(DataInput in)
			throws IOException {
		int length = readShortLength(in);
		ByteBuffer bb = ByteBuffer.allocate(length);
		in.readFully(bb.array(), bb.position(), bb.remaining());
		return bb;
	}

	/** @return null */
	public static byte[] skipShortByteArray(DataInput in) throws IOException {
		int skip = readShortLength(in);
		while (skip > 0) {
			int skipped = in.skipBytes(skip);
			if (skipped == 0)
				throw new EOFException();
			skip -= skipped;
		}
		return null;
	}

	public static byte[] hexToBytes(String str) {
		if (str.length() % 2 == 1)
			str = "0" + str;
		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2),
					16);
		}
		return bytes;
	}

	public static String bytesToHex(byte... bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			int bint = b & 0xff;
			if (bint <= 0xF)
				// toHexString does not 0 pad its results.
				sb.append("0");
			sb.append(Integer.toHexString(bint));
		}
		return sb.toString();
	}

	public static String bytesToHex(ByteBuffer bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = bytes.position() + bytes.arrayOffset(); i < bytes.limit()
				+ bytes.arrayOffset(); i++) {
			int bint = bytes.array()[i] & 0xff;
			if (bint <= 0xF)
				// toHexString does not 0 pad its results.
				sb.append("0");
			sb.append(Integer.toHexString(bint));
		}
		return sb.toString();
	}

	public static void atomicSetMax(AtomicInteger atomic, int i) {
		while (true) {
			int j = atomic.get();
			if (j >= i || atomic.compareAndSet(j, i))
				break;
		}
	}

	public static void atomicSetMax(AtomicLong atomic, long i) {
		while (true) {
			long j = atomic.get();
			if (j >= i || atomic.compareAndSet(j, i))
				break;
		}
	}

//	public static void serialize(TSerializer serializer, TBase struct,
//			DataOutput out) throws IOException {
//		assert serializer != null;
//		assert struct != null;
//		assert out != null;
//		byte[] bytes;
//		try {
//			bytes = serializer.serialize(struct);
//		} catch (TException e) {
//			throw new RuntimeException(e);
//		}
//		out.writeInt(bytes.length);
//		out.write(bytes);
//	}
//
//	public static void deserialize(TDeserializer deserializer, TBase struct,
//			DataInput in) throws IOException {
//		assert deserializer != null;
//		assert struct != null;
//		assert in != null;
//		byte[] bytes = new byte[in.readInt()];
//		in.readFully(bytes);
//		try {
//			deserializer.deserialize(struct, bytes);
//		} catch (TException ex) {
//			throw new IOException(ex);
//		}
//	}

	public static int encodedUTF8Length(String st) {
		int strlen = st.length();
		int utflen = 0;
		for (int i = 0; i < strlen; i++) {
			int c = st.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F))
				utflen++;
			else if (c > 0x07FF)
				utflen += 3;
			else
				utflen += 2;
		}
		return utflen;
	}

	public static String decodeToUTF8(ByteBuffer bytes)
			throws CharacterCodingException {
		bytes = bytes.duplicate();
		String decoded = Charsets.UTF_8.newDecoder().decode(bytes).toString();
		return decoded;
	}

	public static ByteBuffer toByteBuffer(long n) {
		byte[] bytes = new byte[8];
		ByteBuffer bb = ByteBuffer.wrap(bytes).putLong(n);
		bb.rewind();
		return bb;
	}

	public static long timestampMicros() {
		// we use microsecond resolution for compatibility with other client
		// libraries, even though
		// we can't actually get microsecond precision.
		return System.currentTimeMillis() * 1000;
	}

	public static void waitOnFutures(Iterable<Future<?>> futures) {
		for (Future<?> f : futures) {
			try {
				f.get();
			} catch (ExecutionException ee) {
				throw new RuntimeException(ee);
			} catch (InterruptedException ie) {
				throw new AssertionError(ie);
			}
		}
	}

	public static String toString(Map<?, ?> map) {
		// wtf, why isn't something like this in guava or commons collections?
		StringBuilder sb = new StringBuilder("{");
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			sb.append(toString(entry.getKey())).append(": ")
					.append(toString(entry.getValue())).append(", ");
		}
		sb.append("}");
		return sb.toString();
	}

	/** slow! */
	private static Object toString(Object o) {
		return o.getClass().isArray() ? Arrays.toString((Object[]) o) : o
				.toString();
	}
}
