package com.travelzen.hadoopinfra.hadoop;

import java.io.IOException;
import java.util.Iterator;



public abstract class AbstractMapReduceLogic implements MapReduceLogic<byte[], byte[], byte[], byte[]> {
	
	private Context ctx;

	public void setContext(Context ctx) {
		this.ctx = ctx;
	}
	
	public Context context() {
		return ctx;
	}
	
	@Override
	public void combineLogic(byte[] key, Iterator<byte[]> values, Output<byte[], byte[]> output) throws IOException {
		while(values.hasNext()) {
			output.output(key, values.next());
		}
	}
	
}
