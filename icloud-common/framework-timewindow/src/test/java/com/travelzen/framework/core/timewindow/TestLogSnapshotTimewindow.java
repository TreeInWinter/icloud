package com.travelzen.framework.core.timewindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableLong;
import org.javatuples.Pair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icloud.framework.core.time.DateTimeUtil;
import com.travelzen.framework.timewindow.TimeWindowFactory;
import com.travelzen.framework.timewindow.impl.LogCounter;
import com.travelzen.framework.timewindow.impl.TimeSectionAggregateCounter;

public class TestLogSnapshotTimewindow {

	final Logger logger = LoggerFactory
			.getLogger(TestLogSnapshotTimewindow.class);

	@Test
	public void test() {

//		final int windowTimeLen = 1;// minutes
//		final int frameTimeLen = 10;// seconds

		final int windowLengthSec = 20;
		final int frameCnt = 10;// seconds
		final int maxMagebytes = 10;

		TimeWindowFactory factory = new TimeWindowFactory(1000);

		final LogCounter counter = factory.createLogCounter("test",
				windowLengthSec, frameCnt, maxMagebytes);

		final int ITEM_CNT = 10;

		for (int i = 0; i < 10; i++) {
			Runnable r1 = new Runnable() {
				@Override
				public void run() {
					Random random = new Random();
					random.setSeed(System.currentTimeMillis());
					for (int k = 0; k < ITEM_CNT; k++) {
						for (int j = 1; j < 10; j++) {
							String name = Thread.currentThread().getName();
							String log = name + "n" + j + "-"
									+ random.nextInt(50);
							counter.log(name, log);
							logger.info("send: name = "+name+",log="+log);
							DateTimeUtil.sleep(random.nextInt(50));
						}
						DateTimeUtil.sleep(100);
					}
				}
			};
			Thread t1 = new Thread(r1, "t" + i);
			t1.start();
			DateTimeUtil.sleep(10);
		}

		Runnable r = new Runnable() {
			@Override
			public void run() {

				long count = 100000;
				while (--count > 0) {
					List<Map<String, ArrayList<String>>> retall = counter
							.getAllFrameResult();

					logger.info("frame count:{}\n{}", retall.size(),
							StringUtils.join(retall, "\n"));

					for (int i = 0; i < 10; i++) {
						//for (int j = 0; j < 10; j++) {
							String name = "t" + i;
							ArrayList<String> recent = counter.get(name);
							logger.info("[TEST] " + name + "'s click log = "
									+ recent);
						//}
					}
					DateTimeUtil.SleepSec(2);
				}
			}
		};

		Thread t = new Thread(r);
		t.start();

		// List< Map<String, Long>> retall = counter.getAllFrameResult();
		// logger.info(retall.toString());

		Map<String, ArrayList<String>> ret = counter.getLastFrameResult();
		logger.info("getLastFrameResult = " + ret.toString());

		DateTimeUtil.SleepSec(100000);
	}

	@Test
	public void test_getFrameResultAtTime() throws Exception {

		final int windowLengthSec = 20;
		final int frameCnt = 10;// seconds
		final int maxMagebytes = 10;

		TimeWindowFactory factory = new TimeWindowFactory(1000);
		long beforeTimeStamp = System.currentTimeMillis();
		// Thread.sleep(10);
		final TimeSectionAggregateCounter counter = factory
				.createTimeSectionAggregateCounter("test", windowLengthSec,
						frameCnt, maxMagebytes);
		long middleTimeStamp = 0;
		for (int j = 1; j < 10; j++) {
			Pair<String, MutableLong> entry = Pair.with("" + j,
					new MutableLong(1));
			counter.processStringNumberPair(entry);
			if (j == 5) {
				middleTimeStamp = System.currentTimeMillis();
				Thread.sleep(1000 * 3);
			} else {
				Thread.sleep(1000);
			}
		}
		logger.info(counter.getFrameResultAtTime(beforeTimeStamp).toString());
		logger.info(counter.getFrameResultAtTime(middleTimeStamp).toString());
		logger.info(counter.getFrameResultAtTime(middleTimeStamp - 1000)
				.toString());
		logger.info(counter.getFrameResultAtTime(middleTimeStamp - 3000)
				.toString());
	}
}
