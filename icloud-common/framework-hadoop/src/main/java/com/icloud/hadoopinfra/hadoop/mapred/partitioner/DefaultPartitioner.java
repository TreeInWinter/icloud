package com.icloud.hadoopinfra.hadoop.mapred.partitioner;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Partitioner;


public class DefaultPartitioner<T> extends MapReduceBase implements Partitioner<T, Writable> {  

	private static java.util.Random random = new java.util.Random();
	
	@Override  
	public int getPartition(T key, Writable value, int numPartitions) { 
		return randomPartition(numPartitions);
	}

	protected int randomPartition(int numPartitions) {
		int intkey = getRandom();  	
		return Math.abs((int)(intkey % numPartitions));
	}

	protected int getRandom() {
		return random.nextInt();
	}

} 
