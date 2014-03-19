package com.travelzen.framework.timewindow.actor;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.List;

import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.travelzen.framework.core.util.TZUtil;
import com.travelzen.framework.timewindow.TimeWindowConstant;
import com.travelzen.framework.timewindow.frame.EhcacheFrame;
import com.travelzen.framework.timewindow.request.String2StringRequest;

/**
 *
 *
 *	baseClass of HolderActor
 */
public class String2StringListHolderActor extends JLPCActor {

	private static Logger logger = LoggerFactory
			.getLogger(String2StringListHolderActor.class);

	long windowLengthMilliSec;
	
	long frameLengthMilliSec;

	int maxMagebytes4frame;

	int frameCnt;

	String name;

	List<EhcacheFrame> framelist;

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

		framelist = Lists.newArrayListWithExpectedSize(frameCnt);

		long now = System.currentTimeMillis();
		for (int i = 0; i < frameCnt; i++) {
			EhcacheFrame frame = new EhcacheFrame(name, maxMagebytes4frame, now + i
					* frameLengthMilliSec, frameLengthMilliSec);
			framelist.add(frame);
		}
	}
/*
	private void processEntry(Pair<String, String> entry, long timestamp) {
		checkState(framelist.size() > 0, "window.size() should not 0 ");

		
		boolean processOK =false;
		for (int i = 0; i < framelist.size(); i++) {

			EhcacheFrame f = framelist.get(i);

			//find the suitable frame
			if (f.getFrameEndTimestamp() >= timestamp) {
//				f.processStringNumberPair(entry);
				break;
			}

		}
		
		//not find suitable frame, create a new frame 
		if(!processOK){
			long frameStartTimestamp = timestamp +  frameLengthMilliSec;
			EhcacheFrame f = new EhcacheFrame(name, maxMagebytes4frame,
					frameStartTimestamp, frameLengthMilliSec);
			framelist.add(f);
			
//			f.processStringNumberPair(entry);
			return ;
			
		}

//		if (framelist.size() < frameCnt) {
//			for (int i = 0; i < frameCnt - framelist.size(); i++) {
//
//				long frameStartTimestamp = timestamp + i * frameLengthMilliSec;
//				Frame frame = new Frame(name, maxMagebytes4frame,
//						frameStartTimestamp, frameLengthMilliSec);
//				framelist.add(frame);
//			}
//		}

	}
*/
	
	private boolean isFrameExpired(EhcacheFrame frame){
		long now = System.currentTimeMillis();
		return frame.getFrameEndTimestamp() + windowLengthMilliSec < now;
	}

	private void evict() {
		checkState(framelist.size() > 0, "framelist.size() should not 0 ");

	
		
		int currentFrameCnt = framelist.size();
		int expiredFrame =0;
		EVICT: for(int i=0;i<currentFrameCnt;i++){
			
			EhcacheFrame  frame = framelist.get(i);
			
			if(isFrameExpired(frame)){
				expiredFrame++;
			}else{
				frame.evictExpiredElements();
				break EVICT ;
			}
		}
		
		//remove all expired frame
		framelist = framelist.subList(expiredFrame, currentFrameCnt);
		
		//clean storage( only need when the framelist is build by RandomAccessList)
		if(framelist instanceof ArrayList){
			((ArrayList<EhcacheFrame>)framelist).trimToSize();
		}
		
		
		long now = System.currentTimeMillis();
		for(int i=0;i< expiredFrame;i++){
			EhcacheFrame newframe = new EhcacheFrame(name, maxMagebytes4frame, now
					+ 1*frameLengthMilliSec, frameLengthMilliSec);
			framelist.add(newframe);
		}
		
		
//
//		Op.on(framelist).map(new IFunction<Frame, Void>() {
//			public Void execute(final Frame frame, final ExecCtx ctx)
//					throws Exception {
//
//				frame.evictExpiredElements();
//
//				return null;
//			}
//		});
	}

	public void processRequest(String2StringRequest request, RP<?> rp)
			throws Exception {

		switch (request.requestType) {
		case ADD:
//			processEntry(request.entry, request.timestamp);
			break;

		case EVICT:
			evict();

		}

		// find the suitable frame and process the record

		// logger.debug("processRequest:{}",request.getUrl().getUrl());

		// commonBatchDAO.batchInsert(JProxyDAOConst.INSERT_HOTURL,
		// request.getUrl());
		//
		// itemCnt.adjustOrPutValue(JProxyDAOConst.INSERT_HOTURL, 1, 1);
		// rp.processResponse("Hello world!");
	}
}
