package com.travelzen.hadoopinfra.hadoop.mapred;

import java.io.IOException;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public abstract class SSTableMapper<K extends WritableComparable<? super K> , V extends WritableComparable<? super V>> extends MapReduceBase implements Mapper<BytesWritable, BytesWritable, K, V> {

	protected Reporter reporter;
	protected OutputCollector<K, V> output;

	@Override
	public void map(BytesWritable key, BytesWritable value,
			OutputCollector<K, V> output, Reporter reporter) throws IOException {
		
		this.output = output;
		this.reporter = reporter;

		byte[] inputkey = new byte[key.getLength()];
		Bytes.putBytes(inputkey, 0, key.getBytes(), 0, key.getLength());
		
		byte[] inputvalue = new byte[value.getLength()];
		Bytes.putBytes(inputvalue, 0, value.getBytes(), 0, value.getLength());

		runmap(inputkey, inputvalue);
	}

	protected abstract void runmap(byte[] inputkey, byte[] inputvalue) throws IOException;

}
