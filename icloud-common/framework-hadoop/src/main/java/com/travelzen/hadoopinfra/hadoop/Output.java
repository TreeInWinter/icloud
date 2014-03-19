package com.travelzen.hadoopinfra.hadoop;

import java.io.IOException;

public interface Output<K, V> {

	void output(K key, V value) throws IOException;
	
}
