package com.travelzen.hadoopinfra.hadoop.mapred;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;

import com.travelzen.hadoopinfra.hadoop.KeyValue;
import com.travelzen.hadoopinfra.hadoop.MapReduceLogic;
import com.travelzen.hadoopinfra.hadoop.Output;
import com.travelzen.hadoopinfra.hadoop.OutputUtils;
import com.travelzen.hadoopinfra.hadoop.mapred.conf.MapRedConf;

public class SimpleHdfsReducer extends HdfsReducer<Text, Text, BytesWritable> implements Output<byte[], byte[]> {
	
	@SuppressWarnings("rawtypes")
	private MapReduceLogic logic;

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void runreduce(byte[] inputkey, Iterator<byte[]> inputvalue) throws IOException {	
		logic.context().setReporter(reporter);
		logic.reduceLogic(inputkey, inputvalue, this);	
	}
	
	@Override
	public void configure(JobConf job) {
		super.configure(job);
		logic = MapRedConf.configure(job);
	}

	@Override
	public void output(byte[] key, byte[] value) throws IOException {
		KeyValue<Text, Text> kv = OutputUtils.setKeyValueText(key, value);
		this.output.collect(kv.getKey(), kv.getValue());	
	}
	
}
