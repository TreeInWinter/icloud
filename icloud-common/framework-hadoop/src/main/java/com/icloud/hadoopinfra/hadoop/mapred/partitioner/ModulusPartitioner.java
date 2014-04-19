package com.icloud.hadoopinfra.hadoop.mapred.partitioner;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Partitioner;


public class ModulusPartitioner extends MapReduceBase implements Partitioner<Text, Writable> {
	
	private int effectFactor = 1;
	
	@Override  
	public int getPartition(Text key, Writable value, int numPartitions) {
		long l = Long.parseLong(key.toString());
		l = l / effectFactor;
		return Math.abs((int)(l % numPartitions));
	}
	
	public void configure(JobConf job) {
		String factor = job.get("mapred.part.efactor");
		effectFactor = Integer.parseInt(factor);
	}
	

} 
