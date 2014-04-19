package com.icloud.hadoopinfra.hadoop.mapred;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;

import com.icloud.hadoopinfra.hadoop.KeyValue;
import com.icloud.hadoopinfra.hadoop.MapReduceLogic;
import com.icloud.hadoopinfra.hadoop.Output;
import com.icloud.hadoopinfra.hadoop.OutputUtils;
import com.icloud.hadoopinfra.hadoop.mapred.conf.MapRedConf;

public class SimpleHdfsMapper extends HdfsMapper<Text, BytesWritable> implements Output<byte[], byte[]> {

	@SuppressWarnings("rawtypes")
	private MapReduceLogic logic;

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void runmap(byte[] inputkey, byte[] inputvalue) throws IOException {
		logic.context().setReporter(reporter);
		logic.mapLogic(inputkey, inputvalue, this);
	}

	@Override
	public void configure(JobConf job) {
		super.configure(job);
		logic = MapRedConf.configure(job);
	}

	@Override
	public void output(byte[] key, byte[] value) throws IOException {
		KeyValue<Text, BytesWritable> kv = OutputUtils.setKeyValueBytes(key, value);
		this.output.collect(kv.getKey(), kv.getValue());
	}



}
