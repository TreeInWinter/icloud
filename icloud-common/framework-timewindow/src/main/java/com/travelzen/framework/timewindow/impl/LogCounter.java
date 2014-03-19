package com.travelzen.framework.timewindow.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.agilewiki.jactor.JAFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.travelzen.framework.core.util.TZUtil;
import com.travelzen.framework.timewindow.ITimeWindow;
import com.travelzen.framework.timewindow.actor.String2StringListTimeLimitHolderActor;
import com.travelzen.framework.timewindow.request.RequestType;
import com.travelzen.framework.timewindow.request.String2StringListRequest;

public class LogCounter implements ITimeWindow {

	final Logger logger = LoggerFactory.getLogger(LogCounter.class);

	String2StringListTimeLimitHolderActor actor = new String2StringListTimeLimitHolderActor();

	// "evictMillisec" should small than "frameLengthMilliSec"
	// for example: windowLengthSec=300, frameCnt=10, evictMillisec=1000
	public LogCounter(final String name, int windowLengthSec, int frameCnt,
			int maxMagebytes, int evictMillisec) {

		actor.initilizeJactor();

		actor.initilizeTimeWindow(name, windowLengthSec, frameCnt,
				maxMagebytes, evictMillisec);
	}

	/**
	 * 记入日志
	 * 
	 * @param uid
	 *            用户id（sinaid）
	 * @param log
	 *            自定义格式的日志
	 */
	public void log(String uid, String log) {
		long now = System.currentTimeMillis();
		String2StringListRequest request = new String2StringListRequest(
				RequestType.ADD, uid, log, now);

		try {
			request.sendEvent(actor);
		} catch (Exception e) {
			logger.error(TZUtil.stringifyException(e));
			throw Throwables.propagate(e);
		}
	}

	/**
	 * 获得某用户最近的点击日志
	 * 
	 * @param uid
	 *            用户id
	 * @return 日志列表
	 */
	public ArrayList<String> get(String uid) {
		JAFuture future = new JAFuture();
		String2StringListRequest request = new String2StringListRequest(
				RequestType.GET_RESULT, uid, null, 0);

		try {
			String retJson = request.send(future, actor);
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.create();

			ArrayList<String> resultList = gson.fromJson(retJson,
					new TypeToken<ArrayList<String>>() {
					}.getType());
			return resultList;

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
	public Map<String, ArrayList<String>> getLastFrameResult() {

		JAFuture future = new JAFuture();

		String2StringListRequest req = new String2StringListRequest(
				RequestType.GET_LAST_FRAME_RESULT);

		try {
			String retJson = req.send(future, actor);

			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.create();

			Map<String, ArrayList<String>> retMap = gson.fromJson(retJson,
					new TypeToken<Map<String, ArrayList<String>>>() {
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
	public Map<String, ArrayList<String>> getFrameResultAtTime(long time) {

		JAFuture future = new JAFuture();
		String2StringListRequest request = new String2StringListRequest(
				RequestType.GET_FRAME_RESULT_AT_TIME, time);
		try {
			// String retJson = req.send(future, actor);
			String retJson = future.send(actor, request).toString();
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.create();
			Map<String, ArrayList<String>> retMap = gson.fromJson(retJson,
					new TypeToken<Map<String, ArrayList<String>>>() {
					}.getType());
			return retMap;
		} catch (Exception e) {
			logger.error(TZUtil.stringifyException(e));
			throw Throwables.propagate(e);
		}
	}

	public List<Map<String, ArrayList<String>>> getAllFrameResult() {

		JAFuture future = new JAFuture();
		String2StringListRequest req = new String2StringListRequest(
				RequestType.GET_ALL_FRAME_RESULT);
		try {
			String retJson = req.send(future, actor);
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
					.create();
			List<Map<String, ArrayList<String>>> retMap = gson.fromJson(
					retJson,
					new TypeToken<List<Map<String, ArrayList<String>>>>() {
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
		String2StringListRequest req = new String2StringListRequest(RequestType.EVICT);
		try {
			req.sendEvent(actor);
		} catch (Exception e) {
			logger.error(TZUtil.stringifyException(e));
			throw Throwables.propagate(e);
		}
	}

}
