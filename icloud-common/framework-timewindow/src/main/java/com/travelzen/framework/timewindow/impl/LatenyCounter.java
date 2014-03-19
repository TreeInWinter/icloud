package com.travelzen.framework.timewindow.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.agilewiki.jactor.JAFuture;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.mutable.MutableLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.travelzen.framework.core.util.TZUtil;
import com.travelzen.framework.timewindow.ITimeWindow;
import com.travelzen.framework.timewindow.actor.LatencyCounterActor;
import com.travelzen.framework.timewindow.request.LatencyCounterRequest;
import com.travelzen.framework.timewindow.request.RequestType;

public class LatenyCounter implements ITimeWindow {

	final Logger logger = LoggerFactory
			.getLogger(LatenyCounter.class);

	LatencyCounterActor mapTimeWindowActor = new LatencyCounterActor();

	// "evictMillisec" should small than "frameLengthMilliSec"
	// for example: windowLengthSec=300, frameCnt=10, evictMillisec=1000
	public LatenyCounter(final String name, int windowLengthSec,
			int frameCnt, int maxMagebytes, int evictMillisec) {

		mapTimeWindowActor.initilizeJactor();

		mapTimeWindowActor.initilizeTimeWindow(name, windowLengthSec, frameCnt,
				maxMagebytes, evictMillisec);
	}

	// fina a proper frame to process
	public void startWatch(String name) {
		long now = System.currentTimeMillis();
		LatencyCounterRequest req = new LatencyCounterRequest(
				RequestType.START_WATCH, name, now);

		try {
			req.sendEvent(mapTimeWindowActor);
		} catch (Exception e) {
			logger.error(TZUtil.stringifyException(e));
			throw Throwables.propagate(e);
		}
	}
	
	// fina a proper frame to process
	public void stopWatch(String name) {
		long now = System.currentTimeMillis();
		LatencyCounterRequest req = new LatencyCounterRequest(
				RequestType.STOP_WATCH, name, now);

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
	public Map<String, ArrayList<MutableLong>> getLastFrameResult() {

		JAFuture future = new JAFuture();

		LatencyCounterRequest req = new LatencyCounterRequest(
				RequestType.GET_LAST_FRAME_RESULT);

		try {
			String retJson = req.send(future, mapTimeWindowActor);

			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.create();

			Map<String, ArrayList<MutableLong>> retMap = gson.fromJson(retJson,
					new TypeToken<Map<String, ArrayList<MutableLong>>>() {
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
	public Map<String, ArrayList<MutableLong>> getFrameResultAtTime(long time) {

		JAFuture future = new JAFuture();
		LatencyCounterRequest request = new LatencyCounterRequest(
				RequestType.GET_FRAME_RESULT_AT_TIME, time);
		try {
			// String retJson = req.send(future, mapTimeWindowActor);
			String retJson = future.send(mapTimeWindowActor, request)
					.toString();
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.create();
			Map<String, ArrayList<MutableLong>> retMap = gson.fromJson(retJson,
					new TypeToken<Map<String, ArrayList<MutableLong>>>() {
					}.getType());
			return retMap;
		} catch (Exception e) {
			logger.error(TZUtil.stringifyException(e));
			throw Throwables.propagate(e);
		}
	}
	
	public List<Map<String, ArrayList<MutableLong>>> getAllFrameResult() {

		JAFuture future = new JAFuture();
		LatencyCounterRequest req = new LatencyCounterRequest(
				RequestType.GET_ALL_FRAME_RESULT);
		try {
			String retJson = req.send(future, mapTimeWindowActor);
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.create();
			List<Map<String, ArrayList<MutableLong>>> retMap = gson.fromJson(retJson,
					new TypeToken<List<Map<String, ArrayList<MutableLong>>>>() {
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

		LatencyCounterRequest req = new LatencyCounterRequest(
				RequestType.EVICT);

		try {
			req.sendEvent(mapTimeWindowActor);
		} catch (Exception e) {
			logger.error(TZUtil.stringifyException(e));
			throw Throwables.propagate(e);
		}
	}
	
	public long getMaxLatency(String name) {
		JAFuture future = new JAFuture();
		LatencyCounterRequest request = new LatencyCounterRequest(
				RequestType.MAX_LATENCY, name);
		try {
			String retJson = future.send(mapTimeWindowActor, request)
					.toString();
			long maxLatency = NumberUtils.toLong(retJson);
			return maxLatency;
		} catch (Exception e) {
			logger.error(TZUtil.stringifyException(e));
			throw Throwables.propagate(e);
		}
	}
	
	public long getAverageLatency(String name) {
		JAFuture future = new JAFuture();
		LatencyCounterRequest request = new LatencyCounterRequest(
				RequestType.AVERAGE_LATENCY, name);
		try {
			String retJson = future.send(mapTimeWindowActor, request)
					.toString();
			long avgLatency = NumberUtils.toLong(retJson);
			return avgLatency;
		} catch (Exception e) {
			logger.error(TZUtil.stringifyException(e));
			throw Throwables.propagate(e);
		}
	}
}
