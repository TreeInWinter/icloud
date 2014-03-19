package com.travelzen.framework.core.timewindow;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableLong;
import org.javatuples.Pair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelzen.framework.core.time.DateTimeUtil;
import com.travelzen.framework.timewindow.TimeWindowFactory;
import com.travelzen.framework.timewindow.impl.TimeSectionAggregateCounter;

public class TestTimewindow {

	final Logger logger = LoggerFactory.getLogger(TestTimewindow.class);

	@Test
	public void test() {

//		final int windowTimeLen = 1;// minutes
//		final int frameTimeLen = 10;// seconds

		final int windowLengthSec = 20;
		final int frameCnt = 10;// seconds
		final int maxMagebytes = 10;

		TimeWindowFactory factory = new TimeWindowFactory(1000);

		final TimeSectionAggregateCounter counter = factory
				.createTimeSectionAggregateCounter("test", windowLengthSec,
						frameCnt, maxMagebytes);

		final int ITEM_CNT = 1000;

		for (int i = 0; i < 10; i++) {

			Runnable r = new Runnable() {

				@Override
				public void run() {

					for (int k = 0; k < ITEM_CNT; k++) {
						for (int j = 1; j < 10; j++) {
							Pair<String, MutableLong> entry = Pair.with("" + j,
									new MutableLong(1));
							counter.processStringNumberPair(entry);

						}
						DateTimeUtil.sleep(100);
					}

				}

			};

			Thread t = new Thread(r);
			t.start();
			DateTimeUtil.sleep(10);

		}






		Runnable r = new Runnable() {
			@Override
			public void run() {

				while (true) {
					List<Map<String, Long>> retall = counter
							.getAllFrameResult();

					;


					logger.info("frame count:{}",retall.size());
					logger.info(StringUtils.join(retall,"\n"));

					DateTimeUtil.SleepSec(2);
				}
			}
		};

		Thread t = new Thread(r);
		t.start();

		// List< Map<String, Long>> retall = counter.getAllFrameResult();
		// logger.info(retall.toString());

		Map<String, Long> ret = counter.getLastFrameResult();
		logger.info(ret.toString());

		DateTimeUtil.SleepSec(100000);

		// String2LongCounterActor actor = new String2LongCounterActor();

		// final String2IntTimeWindowFactory winFactory = new
		// String2IntTimeWindowFactory(
		// windowTimeLen, frameTimeLen);
		//
		// Range<Integer> range = Ranges.closed(1, 12);
		//
		// final String[] urls = new String[] { "a.com", "b.com", "c.com" };
		//
		// int idx = 0;
		// for (int i : range.asSet(DiscreteDomains.integers())) {
		//
		// String url = urls[idx % urls.length];
		// idx++;
		//
		// Pair<String, Integer> obj = Pair.with(url, 1);
		// winFactory.process(obj);
		// DateTimeUtil.sleep(500);
		// }
		// ;
		//
		// assertTrue(winFactory.getWindows().size() == 3);
		// fail("Not yet implemented");

	}
	@Test
	public void test_getFrameResultAtTime() throws Exception{

		final int windowLengthSec = 20;
		final int frameCnt = 10;// seconds
		final int maxMagebytes = 10;

		TimeWindowFactory factory = new TimeWindowFactory(1000);
		long beforeTimeStamp = System.currentTimeMillis();
//		Thread.sleep(10);
		final TimeSectionAggregateCounter counter = factory.createTimeSectionAggregateCounter("test", windowLengthSec, frameCnt, maxMagebytes);
		long middleTimeStamp = 0;
		for (int j = 1; j < 10; j++) {
			Pair<String, MutableLong> entry = Pair.with("" + j, new MutableLong(1));
			counter.processStringNumberPair(entry);
			if(j == 5){
				middleTimeStamp = System.currentTimeMillis();
			    Thread.sleep(1000 * 3);
			}else{
				Thread.sleep(1000);
			}
		}
		logger.info(counter.getFrameResultAtTime(beforeTimeStamp).toString());
		logger.info(counter.getFrameResultAtTime(middleTimeStamp).toString());
		logger.info(counter.getFrameResultAtTime(middleTimeStamp - 1000).toString());
		logger.info(counter.getFrameResultAtTime(middleTimeStamp - 3000).toString());
	}
}
