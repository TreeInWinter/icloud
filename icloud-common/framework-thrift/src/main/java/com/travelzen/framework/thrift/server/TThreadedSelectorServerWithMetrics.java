/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.travelzen.framework.thrift.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TNonblockingTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelzen.framework.thrift.server.AbstractNonblockingServer.FrameBuffer;
import com.travelzen.framework.thrift.server.TThreadedSelectorServer.AcceptThread;
import com.travelzen.framework.thrift.server.TThreadedSelectorServer.SelectorThread;

/**
 * A Half-Sync/Half-Async server with a separate pool of threads to handle
 * non-blocking I/O. Accepts are handled on a single thread, and a configurable
 * number of nonblocking selector threads manage reading and writing of client
 * connections. A synchronous worker thread pool handles processing of requests.
 * 
 * Performs better than TNonblockingServer/THsHaServer in multi-core
 * environments when the the bottleneck is CPU on the single selector thread
 * handling I/O. In addition, because the accept handling is decoupled from
 * reads/writes and invocation, the server has better ability to handle back-
 * pressure from new connections (e.g. stop accepting when busy).
 * 
 * Like TNonblockingServer, it relies on the use of TFramedTransport.
 */
public class TThreadedSelectorServerWithMetrics extends TThreadedSelectorServer {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TThreadedSelectorServerWithMetrics.class.getName());

	public TThreadedSelectorServerWithMetrics(Args args) {
		super(args);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Start the accept and selector threads running to deal with clients.
	 * 
	 * @return true if everything went ok, false if we couldn't start for some
	 *         reason.
	 */
	@Override
	protected boolean startThreads() {
		try {
			for (int i = 0; i < args.selectorThreads; ++i) {
				SelectorThread tmp = new SelectorThreadWithTimer(
						args.acceptQueueSizePerThread);
				tmp.setName("mobplate-selector-thread-" + i);
				selectorThreads.add(tmp);
			}
			acceptThread = new AcceptThread(
					(TNonblockingServerTransport) serverTransport_,
					createSelectorThreadLoadBalancer(selectorThreads));
			stopped_ = false;
			for (SelectorThread thread : selectorThreads) {
				thread.start();
			}
			acceptThread.start();
			return true;
		} catch (IOException e) {
			LOGGER.error("Failed to start threads!", e);
			return false;
		}
	}

	/**
	 * 可以记录每次读写处理时间的SelectorThread扩展
	 * 
	 * @author taosu
	 * 
	 */
	protected class SelectorThreadWithTimer extends SelectorThread {

		public SelectorThreadWithTimer() throws IOException {
			super();
		}

		public SelectorThreadWithTimer(int maxPendingAccepts)
				throws IOException {
			super(maxPendingAccepts);
		}

		public SelectorThreadWithTimer(
				BlockingQueue<TNonblockingTransport> acceptedQueue)
				throws IOException {
			super(acceptedQueue);
		}

		private HashMap<Integer, Long> timer = new HashMap<Integer, Long>();

		protected void handleWrite(SelectionKey key) {
			super.handleWrite(key);

			long startTime = timer.remove(key.hashCode());
			LOGGER.info("[OneProcessTime][{}] {}ms ", key,
					System.currentTimeMillis() - startTime);

		}

		protected void handleRead(SelectionKey key) {
			timer.put(key.hashCode(), System.currentTimeMillis());
			super.handleRead(key);
		}
	}

}
