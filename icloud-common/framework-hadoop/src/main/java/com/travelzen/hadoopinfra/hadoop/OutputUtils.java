package com.travelzen.hadoopinfra.hadoop;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;

public class OutputUtils {
	
	public static KeyValue<Text, Text> setKeyValueText(byte[] key, byte[] value) {
		Text outkey = null;
		Text outvalue = null;
		if (key != null) {
			outkey = new Text();
			outkey.set(key);
		}
		
		if (value != null) {
			outvalue = new Text();
			outvalue.set(value);
		}
		KeyValue<Text, Text> kv = new KeyValue<Text, Text>();
		kv.set(outkey, outvalue);
		return kv;
	}
	
	public static KeyValue<Text, BytesWritable> setKeyValueBytes(byte[] key, byte[] value) {
		Text outkey = null;
		BytesWritable outvalue = null;
		if (key != null) {
			outkey = new Text();
			outkey.set(key);
		}
		
		if (value != null) {
			outvalue = new BytesWritable();
			outvalue.set(value, 0, value.length);
		}
		KeyValue<Text, BytesWritable> kv = new KeyValue<Text, BytesWritable>();
		kv.set(outkey, outvalue);
		return kv;
	}
	
}
