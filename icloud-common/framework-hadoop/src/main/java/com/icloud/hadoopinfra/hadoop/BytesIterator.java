package com.icloud.hadoopinfra.hadoop;

import java.util.Iterator;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.Reporter;


public class BytesIterator implements Iterator<byte[]> {
	
	private Iterator<? extends Writable> iterator;
	Reporter reporter;
	
	public BytesIterator(Iterator<? extends Writable> iterator) {
		this.iterator = iterator;
	}
	
	public BytesIterator(Iterator<? extends Writable> iterator, Reporter reporter) {
		this.iterator = iterator;
		this.reporter = reporter;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public byte[] next() {
		Writable value = iterator.next();
		
		byte[] b = new byte[0];
		byte[] temp = null;
		int length = 0;
		
		if (value instanceof Text) {
			Text src = (Text)value;
			length = src.getLength();
			temp = src.getBytes();
			
		} else if (value instanceof BytesWritable) {
			BytesWritable src = (BytesWritable)value;
			length = src.getLength();
			temp = src.getBytes();
			
		} else if (value instanceof Result) {
			Result src = (Result)value;
			ImmutableBytesWritable ibw = src.getBytes();
			length = ibw.getLength();
			temp = ibw.get();
		}
		
		if (temp != null) {
			b = new byte[length];
			Bytes.putBytes(b, 0, temp, 0, length);
		}
		
		return b;
	}

	@Override
	public void remove() {
		iterator.remove();
	}

}
