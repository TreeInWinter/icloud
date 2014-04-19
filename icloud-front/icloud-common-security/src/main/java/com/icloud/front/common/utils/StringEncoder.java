package com.icloud.front.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringEncoder {
    private static MessageDigest msgDigest;
    private static final String SALT = "aj#e;PWro>*iWtT_u";

    private static byte[] encodeSHA256(byte[] target) {
        try {
            if (msgDigest == null) {
                msgDigest = MessageDigest.getInstance("SHA-256");
            }
            msgDigest.update(target);
            byte[] result = msgDigest.digest();
            msgDigest.reset();
            return result;
        } catch (NoSuchAlgorithmException e) {
            // exception of this type will never be thrown, because "SHA-256" is correct
            return null;
        }
    }

    public static byte[] simpleConvert(byte[] org) {
        byte[] dest = new byte[org.length];
        int destIndex = 0;
        for (int odd = 1; odd < org.length; odd = odd + 2) {
            dest[destIndex++] = org[odd];
        }
        for (int even = 0; even < org.length; even = even + 2) {
            dest[destIndex++] = org[even];
        }
        return dest;
    }

    public static byte[] simpleRevert(byte[] org) {
    	byte[] dest = new byte[org.length];
    	int delta = org.length / 2;
    	int destIndex = 0;
    	for (int i = 0; i < org.length / 2; i++) {
    		dest[destIndex++] = org[i + delta];
    		dest[destIndex++] = org[i];
    	}
    	if (org.length % 2 != 0 ) {
    		dest[destIndex++] = org[org.length - 1];
    	}
    	return dest;
    }

    public static String encrypt(String str) {
    	if (str == null) {
    		return null;
    	}
        byte[] target = (str + SALT).getBytes();
        target = simpleConvert(target);
        target = encodeSHA256(target);
        target = simpleConvert(target);
        return new String(encode(target));
    }

    private static final char[] HEX = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    private static char[] encode(byte[] bytes) {
        final int nBytes = bytes.length;
        char[] result = new char[2*nBytes];

        int j = 0;
        for (int i=0; i < nBytes; i++) {
            // Char for top 4 bits
            result[j++] = HEX[(0xF0 & bytes[i]) >>> 4 ];
            // Bottom 4
            result[j++] = HEX[(0x0F & bytes[i])];
        }

        return result;
    }
}
