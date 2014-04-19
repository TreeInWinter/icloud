package com.travelzen.framework.core.timewindow;

import org.apache.commons.lang3.mutable.MutableLong;
import org.javatuples.Pair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icloud.framework.core.time.DateTimeUtil;
import com.travelzen.framework.timewindow.TimeWindowFactory;
import com.travelzen.framework.timewindow.impl.TimeSectionAggregateCounter;

public class TestTimewindowPerformance {

	final Logger logger = LoggerFactory.getLogger(TestTimewindowPerformance.class);

	@Test
	public void test() {

		final int windowLengthSec = 20;
		final int frameCnt = 10;// seconds
		final int maxMagebytes = 10;

		TimeWindowFactory factory = new TimeWindowFactory(1000);

		final TimeSectionAggregateCounter counter = factory
				.createTimeSectionAggregateCounter("test", windowLengthSec,
						frameCnt, maxMagebytes);


		
		final int ITEM_CNT = 10000*50;

		
		logger.info("thread  start :{}",  DateTimeUtil.datetime14());

		final long start  = System.currentTimeMillis();
		final MutableLong end = new MutableLong(0);
		
		for (int i = 0; i < 2; i++) {

			  Runnable r = new Runnable() {

				@Override
				public void run() {

					for (int k = 0; k < ITEM_CNT; k++) {
							Pair<String, MutableLong> entry = Pair.with("" + k,
									new MutableLong(1));
							counter.processStringNumberPair(entry);
					}
					
					
					end.setValue(System.currentTimeMillis());
					
					//logger.info("thread  finish,{}",  DateTimeUtil.datetime14());

					end.subtract(start);
					logger.info("thread  finish,{}",  end); ;
				}

			};
			
			Thread t = new Thread(r);
			t.start();

		}
		
		DateTimeUtil.sleep(Integer.MAX_VALUE);

	}

}
