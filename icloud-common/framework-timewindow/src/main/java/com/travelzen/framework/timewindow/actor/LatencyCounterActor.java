package com.travelzen.framework.timewindow.actor;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.apache.commons.lang3.mutable.MutableLong;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.icloud.framework.core.util.TZUtil;
import com.travelzen.datastructure.list.CircularArrayList;
import com.travelzen.framework.timewindow.TimeWindowConstant;
import com.travelzen.framework.timewindow.frame.String2LongListFrame;
import com.travelzen.framework.timewindow.request.LatencyCounterRequest;

/**
 * 过去5分钟内 a ->123次 b -> 456次
 * 
 * expire the whole frame on every evcit operation
 * 
 */
public class LatencyCounterActor extends JLPCActor {

	private static Logger logger = LoggerFactory
			.getLogger(LatencyCounterActor.class);
	private static final int CLEAR_DIRTY_TIMES = 100000;// N次插入/查询次数后清理数据
	private static final long CLEAR_DIRTY_DELAY = 1000 * 60 * 60 * 24;// 清理一天以上的数据
	private int times = 0;
	long windowLengthMilliSec;
	long frameLengthMilliSec;
	int maxMagebytes4frame;
	int frameCnt;
	String name;
	CircularArrayList<String2LongListFrame> framelist;
	private Map<String, MutableLong> started = new HashMap<String, MutableLong>();

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
		if (maxMagebytes4frame < 1)
			maxMagebytes4frame = 1;
		// max size is frameCnt*2, when exceed, expired the oldest;
		framelist = new CircularArrayList<String2LongListFrame>(frameCnt * 2);

		long now = System.currentTimeMillis();
		for (int i = 0; i < frameCnt; i++) {
			String2LongListFrame frame = new String2LongListFrame(name,
					maxMagebytes4frame, now + i * frameLengthMilliSec,
					frameLengthMilliSec);
			framelist.add(frame);
		}
	}

	private void processEntry(Pair<String, MutableLong> entry, long timestamp) {
		checkState(framelist.size() > 0, "window.size() should not 0 ");
		boolean processOK = false;
		for (int i = 0; i < framelist.size(); i++) {
			String2LongListFrame f = framelist.get(i);
			// find the suitable frame
			if (f.getFrameEndTimestamp() >= timestamp) {
				f.processStringLongPair(entry);
				processOK = true;
				break;
			}
		}

		// not find suitable frame, create a new frame
		if (!processOK) {
			// if too many frame has exists, remove the oldeset one
			if (framelist.size() >= framelist.capacity()) {
				framelist.remove(0);
			}
			long frameStartTimestamp = timestamp + frameLengthMilliSec;
			String2LongListFrame f = new String2LongListFrame(name,
					maxMagebytes4frame, frameStartTimestamp,
					frameLengthMilliSec);
			f.processStringLongPair(entry);
			framelist.add(f);
			return;
		}
	}

	private boolean isFrameExpired(String2LongListFrame frame) {
		long now = System.currentTimeMillis();
		return frame.getFrameEndTimestamp() + windowLengthMilliSec < now;
	}

	private void evict() {
		// checkState(framelist.size() > 0, "framelist.size() should not 0 ");
		int currentFrameCnt = framelist.size();
		int expiredFrame = 0;
		EVICT: for (int i = 0; i < currentFrameCnt; i++) {
			String2LongListFrame frame = framelist.get(i);
			if (isFrameExpired(frame)) {
				expiredFrame++;
			} else {
				frame.evictExpiredElements();
				break EVICT;
			}
		}
		for (int i = expiredFrame; i < expiredFrame; i++) {
			framelist.remove(0);
		}
		// too many frame
		if (currentFrameCnt > frameCnt) {

			for (int i = 0; i < currentFrameCnt - frameCnt; i++) {
				// remove head is O(0)
				framelist.remove(0);
			}
		}
		long now = System.currentTimeMillis();
		for (int i = 0; i < expiredFrame; i++) {
			String2LongListFrame newframe = new String2LongListFrame(name,
					maxMagebytes4frame, now + 1 * frameLengthMilliSec,
					frameLengthMilliSec);
			framelist.add(newframe);
		}
	}

	public void processRequest(LatencyCounterRequest request, RP<String> rp)
			throws Exception {
		switch (request.requestType) {
		case START_WATCH:
			startWatch(request.name, request.timestamp);
			break;
		case STOP_WATCH:
			stopWatch(request.name, request.timestamp);
			break;
		case MAX_LATENCY:
			getMaxLatency(request.name, rp);
			break;
		case AVERAGE_LATENCY:
			getAverageLatency(request.name, rp);
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
			break;
		default:
			break;
		}
	}

	public void getMaxLatency(String name, RP<String> rp) throws Exception {
		Long max = 0L;
		for (int i = 0; i < framelist.size(); i++) {
			String2LongListFrame frame = framelist.get(i);
			long imax = frame.getMaxLatency(name);
			if (max < imax) {
				max = imax;
			}
		}
		rp.processResponse(max.toString());
	}

	public void getAverageLatency(String name, RP<String> rp) throws Exception {
		Long sum = 0L;
		int count = 0;
		for (int i = 0; i < framelist.size(); i++) {
			String2LongListFrame frame = framelist.get(i);
			ArrayList<MutableLong> values = frame.getLatencyList(name);
			if (values == null) {
				continue;
			}
			for (MutableLong v : values) {
				sum += v.longValue();
				count++;
			}
		}
		if (count > 0) {
			sum /= count;
		}
		rp.processResponse(sum.toString());
	}
	
	public void getFrameResultAtTime(long timestamp, RP<String> rp) throws Exception {
		Map<String, ArrayList<MutableLong>> ret = new HashMap<String, ArrayList<MutableLong>>();
		outer: for (int i = 0; i < framelist.size(); i++) {
			String2LongListFrame frame = framelist.get(i);
			if (frame.getFrameStartTimestamp() > timestamp)
				break outer;
			if (timestamp < frame.getFrameEndTimestamp()) {// find the frame
				Map<String, ArrayList<MutableLong>> item = frame.getStringLongPairs();
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
		String2LongListFrame frame = framelist.get(framelist.size() - 1);
		Map<String, ArrayList<MutableLong>> ret = frame.getStringLongPairs();
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
				.create();
		rp.processResponse(gson.toJson(ret));
	}

	public void getAllFrameResult(RP<String> rp) throws Exception {
		List<Map<String, ArrayList<MutableLong>>> ret = new ArrayList<Map<String, ArrayList<MutableLong>>>();
		for (int i = 0; i < framelist.size(); i++) {
			String2LongListFrame frame = framelist.get(i);
			Map<String, ArrayList<MutableLong>> item = frame.getStringLongPairs();
			ret.add(item);
		}
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
				.create();
		rp.processResponse(gson.toJson(ret));
	}

	private void startWatch(String name, long timestamp) {
		started.put(name, new MutableLong(timestamp));
		//logger.info("startWatch: name = " + name + ", timestamp = " + timestamp);
	}

	private void stopWatch(String name, long timestamp) {
		if (started.containsKey(name)) {
			MutableLong startTime = started.remove(name);
			MutableLong latency = new MutableLong(timestamp
					- startTime.longValue());
			Pair<String, MutableLong> entry = Pair.with(name, latency);
			//logger.info("stopWatch: name = " + name + ", latency = " + latency);
			processEntry(entry, timestamp);
			// remove old unused key
			clearDirty();
		} else {
			logger.info("stopWatch: unknown name = " + name
					+ ", maybe you never start it.");
		}
	}

	private void clearDirty() {
		// 每10w次插入（start/stop）清理一次一天前的数据
		if (++times < CLEAR_DIRTY_TIMES) {
			// 清理
			long oldtime = System.currentTimeMillis() - CLEAR_DIRTY_DELAY;
			Set<String> keySet = started.keySet();
			Iterator<String> it = keySet.iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (started.get(key).longValue() < oldtime) {
					started.remove(key);
				}
			}
		}
	}
}
