package com.icloud.framework.dao.rdbms;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import sun.misc.Unsafe;
/**
 * 基于Cas乐观锁实现的id生成器,经过测试未发现效率有明显提高
 * @author wangmeng
 *
 */
public class SequenceGeneratorCas {
	private static Map<String,SequencePool> seqMap = new HashMap<String,SequencePool>();
	private BatchSequenceDaoImpl batchSequenceDao;
	private static final Object seqPoolLock = new Object();
	
	//每次请求分配的序列数个数
	private final int allotment = 100;
	public String getNextSeq(String sequenceName, int width) throws Exception{
		
		 
		if(seqMap.get(sequenceName) == null || seqMap.get(sequenceName).isEmpty()){
			synchronized(seqPoolLock){
				if(seqMap.get(sequenceName) == null || seqMap.get(sequenceName).isEmpty()){
					
					SequencePool pool = refillPool(sequenceName);
					seqMap.put(sequenceName, pool);
				}
			}
		}
		SequencePool pool = seqMap.get(sequenceName);
		for(;;){
			long current = pool.getLow();
			if(pool.casLow(current)){
				return formatSequence(String.valueOf(pool.getLow()),width);
			}
			
		}
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

        boolean casLow(long expect) {
            return UNSAFE.compareAndSwapLong(this, lowOffset, expect,new Long(this.getLow()+1));
        }

        // Unsafe mechanics

        private static final sun.misc.Unsafe UNSAFE;
        private static final long lowOffset;
        private static final long highOffset;

        static {
            try {
            	Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            	theUnsafeField.setAccessible(true);
                UNSAFE = (Unsafe) theUnsafeField.get(null);
                Class<SequencePool> k = SequencePool.class;
                lowOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("low"));
                highOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("high"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
		private volatile long low;
		private volatile long high;
		public SequencePool(long low, long high){
			this.low = low;
			this.high = high;
		}
		public long getLow(){
			return this.low;
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
