package com.travelzen.framework.timewindow.actor;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.RP;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.travelzen.framework.core.util.TZUtil;
import com.travelzen.framework.timewindow.TimeWindowConstant;
import com.travelzen.framework.timewindow.frame.String2ListFrame;
import com.travelzen.framework.timewindow.request.String2StringListRequest;

/*
 *
 * 处理如下场景：
 *
 * 按 frame 整帧过期 , expire whole frame every timewindow
 *
 *
 * 帧内是 CircularFifoBuffer, 可以指定每个key上最多保留多少条记录
 * 广告主a 最近5分钟点击过的创意名 -> （abc, def, ghj ..,）,
 * 广告主b 最近5分钟点击过的创意名 -> （abc, def, ghj ..,）,
 * 广告主c 最近5分钟点击过的创意名 -> （abc, def, ghj ..,）,
 *
 */
public class String2StringListTimeLimitHolderActor extends
		String2StringListHolderActor {

	private static Logger logger = LoggerFactory
			.getLogger(String2StringListTimeLimitHolderActor.class);

	long windowLengthMilliSec;
	long frameLengthMilliSec;
	int maxMagebytes4frame;
	int frameCnt;
	String name;
	List<String2ListFrame<String>> framelist;

	@Override
	public void initilizeJactor() {

		MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
		Mailbox mailbox = mailboxFactory.createMailbox();

		try {
			this.initialize(mailbox);
			this.setInitialBufferCapacity(TimeWindowConstant.INITIALBUFFERCAPACITY);
		} catch (Exception e) {
			logger.error(TZUtil.stringifyException(e));
			mailboxFactory.close();
			throw Throwables.propagate(e);

		}

		this.setExceptionHandler(new ExceptionHandler() {
			@Override
			public void process(Exception e) throws Exception {
				logger.error(TZUtil.stringifyException(e));
				throw Throwables.propagate(e);
			}
		});
	}

	@Override
	public void initilizeTimeWindow(final String name, int windowLengthSec,
			int frameCnt, int maxMagebytes, int evictMillisec) {
		this.name = name;
		this.frameCnt = frameCnt;
		// caculate frameLengthMillSec
		this.frameLengthMilliSec = (windowLengthSec * 1000) / frameCnt;
		this.windowLengthMilliSec = windowLengthSec * 1000;
		checkArgument(evictMillisec < frameLengthMilliSec,
				"evictMillisec (%s) should small than  frameLengthMilliSec",
				evictMillisec);
		maxMagebytes4frame = maxMagebytes / frameCnt;
		framelist = Lists.newArrayListWithExpectedSize(frameCnt);
		long now = System.currentTimeMillis();
		for (int i = 0; i < frameCnt; i++) {
			String2ListFrame<String> frame = new String2ListFrame<String>(name,
					maxMagebytes4frame, now + i * frameLengthMilliSec,
					frameLengthMilliSec);
			framelist.add(frame);
		}
	}

	private boolean isFrameExpired(String2ListFrame<String> frame) {
		long now = System.currentTimeMillis();
		return frame.getFrameEndTimestamp() + windowLengthMilliSec < now;
	}

	private void evict() {
		checkState(framelist.size() > 0, "framelist.size() should not 0 ");

		int currentFrameCnt = framelist.size();
		int expiredFrame = 0;
		EVICT: for (int i = 0; i < currentFrameCnt; i++) {

			String2ListFrame<String> frame = framelist.get(i);

			if (isFrameExpired(frame)) {
				expiredFrame++;
			} else {
				frame.evictExpiredElements();
				break EVICT;
			}
		}

		// remove all expired frame
		framelist = framelist.subList(expiredFrame, currentFrameCnt);

		// clean storage( only need when the framelist is build by
		// RandomAccessList)
		if (framelist instanceof ArrayList) {
			((ArrayList<String2ListFrame<String>>) framelist).trimToSize();
		}

		long now = System.currentTimeMillis();
		for (int i = 0; i < expiredFrame; i++) {
			String2ListFrame<String> newframe = new String2ListFrame<String>(
					name, maxMagebytes4frame, now + 1 * frameLengthMilliSec,
					frameLengthMilliSec);
			framelist.add(newframe);
		}
	}

	public void processRequest(String2StringListRequest request, RP<String> rp)
			throws Exception {

		switch (request.requestType) {
		case ADD:
			add(request.name, request.value, request.timestamp);
			break;
		case GET_RESULT:
			get(request.name, rp);
			break;

		case EVICT:
			evict();
			break;
		case GET_LAST_FRAME_RESULT:
			getLastFrameResult(rp);
			break;
		case GET_FRAME_RESULT_AT_TIME:
			getFrameResultAtTime(request.timestamp, rp);
			break;
		case GET_ALL_FRAME_RESULT:
			getAllFrameResult(rp);
		default:
			rp.processResponse("unknown command");
			break;
		}
	}

	private void add(String key, String value, long timestamp) {
		checkState(framelist.size() > 0, "window.size() should not 0 ");
		boolean processOK = false;
		for (int i = 0; i < framelist.size(); i++) {
			String2ListFrame<String> f = framelist.get(i);
			// find the suitable frame
			if (f.getFrameEndTimestamp() >= timestamp) {
				f.add(key, value);
				processOK = true;
				break;
			}
		}

		// not find suitable frame, create a new frame
		if (!processOK) {
			long frameStartTimestamp = timestamp + frameLengthMilliSec;
			String2ListFrame<String> f = new String2ListFrame<String>(name,
					maxMagebytes4frame, frameStartTimestamp,
					frameLengthMilliSec);
			f.add(key, value);
			framelist.add(f);
			return;
		}
	}

	private void get(String key, RP<String> rp) throws Exception {
		ArrayList<String> result = new ArrayList<String>();
		if (StringUtils.isEmpty(key)) {
			for (int i = 0; i < framelist.size(); i++) {
				String2ListFrame<String> frame = framelist.get(i);
				Map<String, ArrayList<String>> data = frame.getAll();
				for (ArrayList<String> d : data.values()) {
					result.addAll(d);
				}
			}
		} else {
			// 按照插入顺序？
			for (int i = 0; i < framelist.size(); i++) {
				String2ListFrame<String> frame = framelist.get(i);
				result.addAll(frame.get(key));
			}
		}

		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
				.create();
		String json = gson.toJson(result);
		rp.processResponse(json);
	}

	public void getFrameResultAtTime(long timestamp, RP<String> rp) throws Exception {
		Map<String, ArrayList<String>> ret = new HashMap<String, ArrayList<String>>();
		outer: for (int i = 0; i < framelist.size(); i++) {
			String2ListFrame<String> frame = framelist.get(i);
			if (frame.getFrameStartTimestamp() > timestamp)
				break outer;
			if (timestamp < frame.getFrameEndTimestamp()) {// find the frame
				Map<String, ArrayList<String>> item = frame.getAll();
				ret.putAll(item);
				break outer;
			}
		}
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
				.create();
		String json = gson.toJson(ret);
		logger.info("json response = " + json);
		rp.processResponse(json);
	}

	public void getLastFrameResult(RP<String> rp) throws Exception {
		String2ListFrame<String> frame = framelist.get(framelist.size() - 1);
		Map<String, ArrayList<String>> ret = frame.getAll();
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
				.create();
		rp.processResponse(gson.toJson(ret));
	}

	public void getAllFrameResult(RP<String> rp) throws Exception {
		List<Map<String, ArrayList<String>>> ret = new ArrayList<Map<String, ArrayList<String>>>();
		for (int i = 0; i < framelist.size(); i++) {
			String2ListFrame<String> frame = framelist.get(i);
			Map<String, ArrayList<String>> item = frame.getAll();
			ret.add(item);
		}
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
				.create();
		rp.processResponse(gson.toJson(ret));
	}
}
