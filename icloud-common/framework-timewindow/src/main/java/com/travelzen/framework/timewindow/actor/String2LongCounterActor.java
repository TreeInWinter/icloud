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
import org.agilewiki.jactor.lpc.JLPCActor;
import org.apache.commons.lang3.mutable.MutableLong;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.travelzen.datastructure.list.CircularArrayList;
import com.travelzen.framework.core.util.TZUtil;
import com.travelzen.framework.dict.DataDict;
import com.travelzen.framework.timewindow.TimeWindowConstant;
import com.travelzen.framework.timewindow.frame.EhcacheFrame;
import com.travelzen.framework.timewindow.request.String2LongCounterRequest;

/**
 * 过去5分钟内 a ->123次 b -> 456次
 * 
 * expire the whole frame on every evcit operation
 * 
 */
public class String2LongCounterActor extends JLPCActor {

	private static Logger logger = LoggerFactory
			.getLogger(String2LongCounterActor.class);

	long windowLengthMilliSec;

	long frameLengthMilliSec;

	int maxMagebytes4frame;

	int frameCnt;

	String name;

	CircularArrayList<EhcacheFrame> framelist;

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
		
		if(maxMagebytes4frame < 1)
			maxMagebytes4frame = 1;

		// max size is frameCnt*2, when exceed, expired the oldest;
		framelist = new CircularArrayList<EhcacheFrame>(frameCnt * 2);

		long now = System.currentTimeMillis();
		for (int i = 0; i < frameCnt; i++) {
			EhcacheFrame frame = new EhcacheFrame(name, maxMagebytes4frame, now
					+ i * frameLengthMilliSec, frameLengthMilliSec);
			framelist.add(frame);
		}
	}

	private void processEntry(Pair<String, MutableLong> entry, long timestamp) {
		
		checkState(framelist.size() > 0, "window.size() should not 0 ");

		boolean processOK = false;
		for (int i = 0; i < framelist.size(); i++) {
			EhcacheFrame f = framelist.get(i);
			
			if(timestamp < f.getFrameStartTimestamp()) // 已经不在此window中了
				return;
			// find the suitable frame
			if (f.getFrameEndTimestamp() > timestamp) {
				f.processStringLongPair(entry);
				processOK = true;
				break;
			}
		}

		// not find suitable frame, create a new frame
		if (!processOK) {
			EhcacheFrame f = null;
			long nextFrameStartTimeStamp = framelist.get(framelist.size()-1).getFrameEndTimestamp();
			while(nextFrameStartTimeStamp < timestamp){
				//if too many frame has exists, remove the oldeset one
				if (framelist.size() >= framelist.capacity()) {
					framelist.remove(0);
				}
				f = new EhcacheFrame(name, maxMagebytes4frame, nextFrameStartTimeStamp, frameLengthMilliSec);
				framelist.add(f);
				nextFrameStartTimeStamp += frameLengthMilliSec;
			}
			if(f != null)
				f.processStringLongPair(entry);
			return;

		}

		// if (framelist.size() < frameCnt) {
		// for (int i = 0; i < frameCnt - framelist.size(); i++) {
		//
		// long frameStartTimestamp = timestamp + i * frameLengthMilliSec;
		// Frame frame = new Frame(name, maxMagebytes4frame,
		// frameStartTimestamp, frameLengthMilliSec);
		// framelist.add(frame);
		// }
		// }

	}

	private boolean isFrameExpired(EhcacheFrame frame) {
		long now = System.currentTimeMillis();
		return frame.getFrameEndTimestamp() + windowLengthMilliSec < now;
	}

	private void evict() {
		//checkState(framelist.size() > 0, "framelist.size() should not 0 ");

		int currentFrameCnt = framelist.size();
		int expiredFrame = 0;
		EVICT: for (int i = 0; i < currentFrameCnt; i++) {

			EhcacheFrame frame = framelist.get(i);

			if (isFrameExpired(frame)) {
				expiredFrame++;
			} else break EVICT;
		}

		for (int i = 0; i < expiredFrame; i++) {
			EhcacheFrame frame = framelist.remove(0);
			if(frame != null)
				frame.evictExpiredElements();
		}
	}

	public void processRequest(String2LongCounterRequest request, RP<String> rp)
			throws Exception {

		switch (request.requestType) {
		case ADD:
			processEntry(request.entry, request.timestamp);
			break;

		case EVICT:
			evict();
			break;

		case GET_LAST_FRAME_RESULT:
			getLastFrameResult(rp);
			break;
			
		case GET_FRAME_RESULT_AT_TIME:
			getFrameResultAtTime(request, rp);
			break;
			
		case GET_ALL_FRAME_RESULT:
			getAllFrameResult(   rp);
			break;

		}


		// find the suitable frame and process the record

		// logger.debug("processRequest:{}",request.getUrl().getUrl());

		// commonBatchDAO.batchInsert(JProxyDAOConst.INSERT_HOTURL,
		// request.getUrl());
		//
		// itemCnt.adjustOrPutValue(JProxyDAOConst.INSERT_HOTURL, 1, 1);
		// rp.processResponse("Hello world!");
	}

	public void getFrameResultAtTime(String2LongCounterRequest request, RP<String> rp) throws Exception {
		
		Map<String,Long> ret = new HashMap<String, Long>();
				
		outer: for(int i =0;i<framelist.size();i++){
			
			EhcacheFrame frame = framelist.get(i);
			
			if(frame.getFrameStartTimestamp() > request.timestamp)
				break outer;
			if(request.timestamp < frame.getFrameEndTimestamp()){//find the frame
				Map<String,Long> item = frame.getStringLongPairs();
				ret.putAll(item);
				ret.put(DataDict.FRAME_START_TIMESTAMP, frame.getFrameStartTimestamp());
				ret.put(DataDict.FRAME_END_TIMESTAMP, frame.getFrameEndTimestamp());
				break outer;
			}
			
		}		

		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
				.create();
		
		rp.processResponse(gson.toJson(ret));
		
	}
	
	public void getLastFrameResult( RP<String> rp) throws Exception {
		
		 
		EhcacheFrame frame = framelist.get(framelist.size()-1);
		
		Map<String,Long> ret = frame.getStringLongPairs();
		

		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
				.create();
		
		rp.processResponse(gson.toJson(ret));
		

	}
	
	public void getAllFrameResult( RP<String> rp) throws Exception {
		
		
		List<Map<String,Long>>  ret = new ArrayList<Map<String, Long>>();
			
		for(int i =0;i<framelist.size();i++){
			
			EhcacheFrame frame = framelist.get(i);
			
			Map<String,Long> item = frame.getStringLongPairs();
			
			ret.add(item);
		}

		Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
				.create();
		
		rp.processResponse(gson.toJson(ret));

	}

}
