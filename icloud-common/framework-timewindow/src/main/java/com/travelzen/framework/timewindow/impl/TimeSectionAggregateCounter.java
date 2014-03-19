package com.travelzen.framework.timewindow.impl;

import java.util.List;
import java.util.Map;

import org.agilewiki.jactor.JAFuture;
import org.apache.commons.lang3.mutable.MutableLong;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.travelzen.framework.core.util.TZUtil;
import com.travelzen.framework.timewindow.ITimeWindow;
import com.travelzen.framework.timewindow.actor.String2LongCounterActor;
import com.travelzen.framework.timewindow.request.RequestType;
import com.travelzen.framework.timewindow.request.String2LongCounterRequest;

public class TimeSectionAggregateCounter implements ITimeWindow {

	final Logger logger = LoggerFactory
			.getLogger(TimeSectionAggregateCounter.class);

	String2LongCounterActor mapTimeWindowActor = new String2LongCounterActor();

	// "evictMillisec" should small than "frameLengthMilliSec"
	// for example: windowLengthSec=300, frameCnt=10, evictMillisec=1000
	public TimeSectionAggregateCounter(final String name, int windowLengthSec,
			int frameCnt, int maxMagebytes, int evictMillisec) {

		mapTimeWindowActor.initilizeJactor();

		mapTimeWindowActor.initilizeTimeWindow(name, windowLengthSec, frameCnt,
				maxMagebytes, evictMillisec);
	}

	/**
	 * throws : IllegalArgumentException ,IllegalStateException
	 * 
	 * @param obj
	 */
	// fina a proper frame to process
	public void processStringNumberPair(Pair<String, MutableLong> entry) {

		long now = System.currentTimeMillis();

		String2LongCounterRequest req = new String2LongCounterRequest(
				RequestType.ADD, entry, now);

		try {
			req.sendEvent(mapTimeWindowActor);
		} catch (Exception e) {
			logger.error(TZUtil.stringifyException(e));
			throw Throwables.propagate(e);
		}

	}

	/**
	 * translate the json return value to a map
	 * 
	 * @return
	 */
	public Map<String, Long> getLastFrameResult() {

		JAFuture future = new JAFuture();

		String2LongCounterRequest req = new String2LongCounterRequest(
				RequestType.GET_LAST_FRAME_RESULT);

		try {
			String retJson = req.send(future, mapTimeWindowActor);

			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.create();

			Map<String, Long> retMap = gson.fromJson(retJson,
					new TypeToken<Map<String, Long>>() {
					}.getType());
			
			return retMap;

		} catch (Exception e) {
			logger.error(TZUtil.stringifyException(e));
			throw Throwables.propagate(e);
		}


	}
	/**
	 * translate the json return value to a map. 获取包含time这一刻数据的帧数据
	 * 
	 * @return
	 */
	public Map<String, Long> getFrameResultAtTime(long time) {

		JAFuture future = new JAFuture();

		String2LongCounterRequest req = new String2LongCounterRequest(
				RequestType.GET_FRAME_RESULT_AT_TIME, time);

		try {
			String retJson = req.send(future, mapTimeWindowActor);

			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.create();

			Map<String, Long> retMap = gson.fromJson(retJson,
					new TypeToken<Map<String, Long>>() {
					}.getType());
			
			return retMap;

		} catch (Exception e) {
			logger.error(TZUtil.stringifyException(e));
			throw Throwables.propagate(e);
		}


	}
	
	public List<Map<String, Long>> getAllFrameResult() {

		JAFuture future = new JAFuture();

		String2LongCounterRequest req = new String2LongCounterRequest(
				RequestType.GET_ALL_FRAME_RESULT);

		try {
			String retJson = req.send(future, mapTimeWindowActor);
			logger.info("retJson = "+retJson);

			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.create();

			List<Map<String, Long>> retMap = gson.fromJson(retJson,
					new TypeToken<List<Map<String, Long>>>() {
					}.getType());
			
			return retMap;

		} catch (Exception e) {
			logger.error(TZUtil.stringifyException(e));
			throw Throwables.propagate(e);
		}


	}
	

	/**
	 * throws : IllegalArgumentException ,IllegalStateException
	 * 
	 * @param obj
	 */
	public void evictFrames() {

		String2LongCounterRequest req = new String2LongCounterRequest(
				RequestType.EVICT);

		try {
			req.sendEvent(mapTimeWindowActor);
		} catch (Exception e) {
			logger.error(TZUtil.stringifyException(e));
			throw Throwables.propagate(e);
		}

	}
}
