package com.travelzen.search.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {
	public static byte[] descSerialize(Object object) {
		if (object == null)
			return null;
		ByteArrayOutputStream obj = new ByteArrayOutputStream();
		try {
			ObjectOutputStream out = new ObjectOutputStream(obj);
			out.writeObject(object);
			return obj.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object serialize(byte[] bytes) {
		if (bytes == null)
			return null;
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		try {
			ObjectInputStream obin = new ObjectInputStream(bin);
			Object obj = obin.readObject();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
