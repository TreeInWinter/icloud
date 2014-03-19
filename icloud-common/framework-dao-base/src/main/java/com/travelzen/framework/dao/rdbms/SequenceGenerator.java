package com.travelzen.framework.dao.rdbms;

import java.util.HashMap;
import java.util.Map;

public class SequenceGenerator {
	private static Map<String,SequencePool> seqMap = new HashMap<String,SequencePool>();
	private BatchSequenceDaoImpl batchSequenceDao;
	//每次请求分配的序列数个数
	private final int allotment = 100;
	
	public synchronized String getNextSeq(String sequenceName, int width) throws Exception{
		SequencePool pool = seqMap.get(sequenceName);
		if(seqMap.get(sequenceName) == null || pool.isEmpty()){
			pool = refillPool(sequenceName);
			seqMap.put(sequenceName, pool);
		}
		return formatSequence(String.valueOf(pool.next()),width);
	}
	
	public synchronized String getNextSeq(String sequenceName) throws Exception{
		SequencePool pool = seqMap.get(sequenceName);
		if(seqMap.get(sequenceName) == null || pool.isEmpty()){
			pool = refillPool(sequenceName);
			seqMap.put(sequenceName, pool);
		}
		return String.valueOf(pool.next());
	}
	
	private SequencePool refillPool(String sequenceName) throws Exception{
		long nextSeq = batchSequenceDao.getNextSeq(sequenceName, allotment);
//		long nextSeq = 0;
		return new SequencePool(nextSeq, nextSeq + allotment -1); 
	}
	/**
	 * 
	 * description: 将传入的字符串的长度限制为一定值
	 * @param is
	 * @param width
	 * @return
	 */
	private static String formatSequence(String is, int width) {
		if (is.length() < width)
			for (; is.length() < width; is = "0" + is);
		else
			is = is.substring(is.length() - width, is.length());
		return is;
	}
	private static class SequencePool{
		private long low;
		private long high;
		public SequencePool(long low, long high){
			this.low = low;
			this.high = high;
		}
		public long next() {
			return low++;
		}
		public boolean isEmpty(){
			return low > high;
		}
	}
	public BatchSequenceDaoImpl getBatchSequenceDao() {
		return batchSequenceDao;
	}
	public void setBatchSequenceDao(BatchSequenceDaoImpl batchSequenceDao) {
		this.batchSequenceDao = batchSequenceDao;
	}
}
