package com.travelzen.hadoopinfra.hadoop.mapred;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public abstract class HdfsMapper<K extends WritableComparable<? super K> , V extends WritableComparable<? super V>> extends MapReduceBase implements Mapper<Text, Text, K, V> {

	protected OutputCollector<K, V> output;
	protected Reporter reporter;

	@Override
	public void map(Text key, Text value,
			OutputCollector<K, V> output, Reporter reporter) throws IOException {
		
		this.output = output;
		this.reporter = reporter;

		byte[] inputkey = key.getBytes();
		byte[] inputvalue = value.getBytes();

		runmap(inputkey, inputvalue);
	}

	protected abstract void runmap(byte[] inputkey, byte[] inputvalue) throws IOException;

}
