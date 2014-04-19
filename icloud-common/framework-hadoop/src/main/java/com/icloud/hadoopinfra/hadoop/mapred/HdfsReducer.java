package com.icloud.hadoopinfra.hadoop.mapred;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import com.icloud.hadoopinfra.hadoop.BytesIterator;

public abstract class HdfsReducer<K extends WritableComparable<? super K> , V extends WritableComparable<? super V>, T extends Writable> extends MapReduceBase implements Reducer<Text, T, K, V> {
	

	protected OutputCollector<K, V> output;
	protected Reporter reporter;

	@Override
	public void reduce(Text key, Iterator<T> values,OutputCollector<K, V> output, Reporter reporter) throws IOException {

		this.reporter = reporter;
		this.output = output;
		
		// inputkey
		byte[] inputkey = new byte[key.getLength()];			
		Bytes.putBytes(inputkey, 0, key.getBytes(), 0, key.getLength());
		
		runreduceDef(inputkey, values);	
	}
	
	protected void runreduceDef(byte[] inputkey, Iterator<T> values) throws IOException {
		BytesIterator xit = new BytesIterator(values, reporter);
		runreduce(inputkey, xit);
	}
	
	protected abstract void runreduce(byte[] inputkey, Iterator<byte[]> inputvalue) throws IOException;

}
