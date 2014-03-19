package com.travelzen.framework.hadoop.hbase;

import org.apache.hadoop.hbase.KeyValue;


public class HBaseForceTrueKeyComparator  extends KeyValue.KeyComparator{

	@Override
	public int compare(byte[] o1, byte[] o2) {
		return -1;
	}

	@Override
	public int compare(byte[] arg0, int arg1, int arg2, byte[] arg3, int arg4,
			int arg5) {
		return -1;
	}

}
