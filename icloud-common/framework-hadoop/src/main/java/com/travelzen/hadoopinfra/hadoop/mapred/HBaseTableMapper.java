package com.travelzen.hadoopinfra.hadoop.mapred;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapred.TableMap;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

@SuppressWarnings("deprecation")
public abstract class HBaseTableMapper<K extends WritableComparable<? super K>, V extends Writable> extends MapReduceBase implements TableMap<K, V> {

	protected OutputCollector<K, V> output;
	protected Reporter reporter;
	
	@Override
	public void map(ImmutableBytesWritable key, Result value,OutputCollector<K, V> output, Reporter reporter) throws IOException {
		
		this.output = output;
		this.reporter = reporter;
		byte[] inputkey = new byte[key.getLength()];
		Bytes.putBytes(inputkey, 0, key.get(), 0, key.getLength());
		runmap(inputkey, value);

	}

	protected abstract void runmap(byte[] inputkey, Result inputvalue) throws IOException;

}
