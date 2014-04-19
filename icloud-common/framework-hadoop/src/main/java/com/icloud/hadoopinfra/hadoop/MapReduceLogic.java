package com.icloud.hadoopinfra.hadoop;

import java.io.IOException;
import java.util.Iterator;


public interface MapReduceLogic<K, V, K2, V2> {
	
	public static final String LOGIC_CLASS = "logicClass";
	
	void setContext(Context ctx);
	
	Context context();
	
	void mapLogic(byte[] key, byte[] value, Output<K, V> output) throws IOException;
	
	void reduceLogic(byte[] key, Iterator<byte[]> values, Output<K2, V2> output) throws IOException;
	
	void combineLogic(byte[] key, Iterator<byte[]> values, Output<K, V> output) throws IOException;

}
