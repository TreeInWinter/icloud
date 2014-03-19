package com.travelzen.framework.thrift.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TTransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelzen.framework.thrift.protocol.RIThriftProtocolFactory;
import com.travelzen.framework.thrift.server.TThreadedSelectorServer.Args.AcceptPolicy;

public class ThriftServerFacade {
	static public Logger logger = LoggerFactory
			.getLogger(ThriftServerFacade.class);
	private static final int processorsOfCPU = Runtime.getRuntime()
			.availableProcessors();

	/**
	 * 获取一个异步 Server.
	 *
	 * @param socketAddr
	 *            server address.
	 * @param socketPort
	 *            server port.
	 * @param workerThreadsMin
	 *            最小处理线程数.
	 * @param workerThreadsMax
	 *            最大处理线程数.
	 * @param selectorThreads
	 *            selector线程数.
	 * @param processor
	 *            处理者.
	 * @param serverName
	 *            server name.
	 * @return
	 */
	public static TServer getThreadedSelectorServer(InetAddress socketAddr, int socketPort, int workerThreadsMin, int workerThreadsMax,
			int selectorThreads, TProcessor processor, String serverName) {

		// Protocol factory
		int tBinaryProtocolReadLengthInBytes = 64 * 1024 * 1024;
		TProtocolFactory tProtocolFactory = new TBinaryProtocol.Factory(true, true, tBinaryProtocolReadLengthInBytes);
		logger.info("Using TBinaryProtocol with a max read length of {} bytes.", tBinaryProtocolReadLengthInBytes);


		return getThreadedSelectorServer(socketAddr, socketPort, workerThreadsMin, workerThreadsMax, selectorThreads, processor,
				serverName, tProtocolFactory, tProtocolFactory);
	}

	/**
	 * 获取一个异步 RIThriftServer.
	 *
	 * @param socketAddr
	 *            server address.
	 * @param socketPort
	 *            server port.
	 * @param workerThreadsMin
	 *            最小处理线程数.
	 * @param workerThreadsMax
	 *            最大处理线程数.
	 * @param selectorThreads
	 *            selector线程数.
	 * @param processor
	 *            处理者.
	 * @param serverName
	 *            server name.
	 * @return
	 */
	public static TServer getThreadedSelectorRIThriftServer(InetAddress socketAddr, int socketPort, int workerThreadsMin, int workerThreadsMax,
			int selectorThreads, TProcessor processor, String serverName) {

		// Protocol factory
		int tBinaryProtocolReadLengthInBytes = 64 * 1024 * 1024;
		TProtocolFactory tProtocolFactory = new RIThriftProtocolFactory(true, true, tBinaryProtocolReadLengthInBytes);
		logger.info("Using TBinaryProtocol with a max read length of {} bytes.", tBinaryProtocolReadLengthInBytes);


		return getThreadedSelectorServer(socketAddr, socketPort, workerThreadsMin, workerThreadsMax, selectorThreads, processor,
				serverName, tProtocolFactory, tProtocolFactory);
	}

	/**
	 * 获取一个异步 Server.
	 *
	 * @param socketAddr
	 *            server address.
	 * @param socketPort
	 *            server port.
	 * @param workerThreadsMin
	 *            最小处理线程数.
	 * @param workerThreadsMax
	 *            最大处理线程数.
	 * @param selectorThreads
	 *            selector线程数.
	 * @param processor
	 *            处理者.
	 * @param serverName
	 *            server name.
	 * @param inProtocolFactory
	 * 			输入协议工厂
	 * @param outProtocolFactory
	 * 			输出协议工厂
	 * @return
	 */
	public static TServer getThreadedSelectorServer(InetAddress socketAddr, int socketPort, int workerThreadsMin, int workerThreadsMax,
			int selectorThreads, TProcessor processor, String serverName, TProtocolFactory inProtocolFactory, TProtocolFactory outProtocolFacotry) {

		boolean socketKeepalive = true;
		int socketSendBuffSizeInBytes = 10 * 1024 * 1024;
		int socketRecvBuffSizeInBytes = 10 * 1024 * 1024;

		TNonblockingServerTransport serverTransport;
		try {
			serverTransport = new TCustomNonblockingServerSocket(new InetSocketAddress(socketAddr, socketPort), socketKeepalive,
					socketSendBuffSizeInBytes, socketRecvBuffSizeInBytes);
		} catch (TTransportException e) {
			throw new RuntimeException(String.format("Unable to create thrift socket to %s:%s", socketAddr, socketPort), e);
		}

		logger.info(String.format("Binding thrift service to %s:%s", socketAddr, socketPort));

		// Transport factory
		int tFramedTransportMaxLengthInBytes = 30 * 1024 * 1024;
		TTransportFactory transportFactory = new TFramedTransport.Factory(tFramedTransportMaxLengthInBytes);
		logger.info("Using TFramedTransport with a max frame size of {} bytes.", tFramedTransportMaxLengthInBytes);

		// executor
		workerThreadsMin = workerThreadsMin <= 0 ? processorsOfCPU : workerThreadsMin;
		workerThreadsMax = workerThreadsMax <= 0 ? processorsOfCPU : workerThreadsMax;
		selectorThreads = selectorThreads <= 0 ? processorsOfCPU : selectorThreads;
		logger.info("Using worker threads: min {} , max {}. selector threads: {}", new Object[] { workerThreadsMin, workerThreadsMax,
				selectorThreads });

		BasicThreadFactory factory = new BasicThreadFactory.Builder().namingPattern(serverName + "-%d").build();
		LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		ExecutorService invoker = new ThreadPoolExecutor(processorsOfCPU, processorsOfCPU, 0, TimeUnit.SECONDS, queue, factory);

		return getThreadedSelectorServer(serverTransport, transportFactory, transportFactory, inProtocolFactory, outProtocolFacotry,
				processor, invoker, selectorThreads);
	}

	/**
	 * 获取一个异步 Server.
	 *
	 * @param serverTransport
	 *            连接设置
	 * @param inTransportFactory
	 *            输入传输类工厂
	 * @param outTransportFactory
	 *            输出传输类工厂
	 * @param inProtocolFactory
	 *            输入协议类工厂
	 * @param outProtocolFactory
	 *            输出协议类工厂
	 * @param processor
	 *            处理者.
	 * @param invoker
	 *            线程执行类
	 * @param selectorThreads
	 *            selector个数
	 * @return
	 */
	public static TServer getThreadedSelectorServer(TNonblockingServerTransport serverTransport, TTransportFactory inTransportFactory,
			TTransportFactory outTransportFactory, TProtocolFactory inProtocolFactory, TProtocolFactory outProtocolFactory,
			TProcessor processor, ExecutorService invoker, int selectorThreads) {

		TThreadedSelectorServer.Args serverArgs = new TThreadedSelectorServer
				.Args(serverTransport)
				.inputTransportFactory(inTransportFactory).outputTransportFactory(outTransportFactory)
				.inputProtocolFactory(inProtocolFactory).outputProtocolFactory(outProtocolFactory)
				.processor(processor)
				.executorService(invoker)
				.acceptQueueSizePerThread(1000)
				.acceptPolicy(AcceptPolicy.FAIR_ACCEPT)
				.selectorThreads(selectorThreads);

		return new TThreadedSelectorServer(serverArgs);

	}

	/**
	 * 获取一个异步 Server.(带有每次IO处理时间的log输出
	 *
	 * @param socketAddr
	 *            server address.
	 * @param socketPort
	 *            server port.
	 * @param workerThreadsMin
	 *            最小处理线程数.
	 * @param workerThreadsMax
	 *            最大处理线程数.
	 * @param selectorThreads
	 *            selector线程数.
	 * @param processor
	 *            处理者.
	 * @param serverName
	 *            server name.
	 * @return
	 */
	public static TServer getThreadedSelectorServerWithMetrics(
			InetAddress socketAddr, int socketPort, int workerThreadsMin,
			int workerThreadsMax, int selectorThreads, TProcessor processor,
			String serverName) {

		boolean socketKeepalive = true;
		int socketSendBuffSizeInBytes = 10 * 1024 * 1024;
		int socketRecvBuffSizeInBytes = 10 * 1024 * 1024;
		int tBinaryProtocolReadLengthInBytes = 64 * 1024 * 1024;
		int tFramedTransportMaxLengthInBytes = 30 * 1024 * 1024;

		TNonblockingServerTransport serverTransport;
		try {
			serverTransport = new TCustomNonblockingServerSocket(
					new InetSocketAddress(socketAddr, socketPort),
					socketKeepalive, socketSendBuffSizeInBytes,
					socketRecvBuffSizeInBytes);
		} catch (TTransportException e) {
			throw new RuntimeException(String.format(
					"Unable to create thrift socket to %s:%s", socketAddr,
					socketPort), e);
		}

		logger.info(String.format("Binding thrift service to %s:%s",
				socketAddr, socketPort));

		// Protocol factory
		TProtocolFactory tProtocolFactory = new TBinaryProtocol.Factory(true,
				true, tBinaryProtocolReadLengthInBytes);
		logger.info(
				"Using TBinaryProtocol with a max read length of {} bytes.",
				tBinaryProtocolReadLengthInBytes);

		// Transport factory
		TTransportFactory inTransportFactory, outTransportFactory;

		inTransportFactory = new TFramedTransport.Factory(
				tFramedTransportMaxLengthInBytes);
		outTransportFactory = new TFramedTransport.Factory(
				tFramedTransportMaxLengthInBytes);

		logger.info(
				"Using TFramedTransport with a max frame size of {} bytes.",
				tFramedTransportMaxLengthInBytes);

		workerThreadsMin = workerThreadsMin <= 0 ? processorsOfCPU
				: workerThreadsMin;
		workerThreadsMax = workerThreadsMax <= 0 ? processorsOfCPU
				: workerThreadsMax;
		selectorThreads = selectorThreads <= 0 ? processorsOfCPU
				: selectorThreads;
		logger.info(
				"Using worker threads: min {} , max {}. selector threads: {}",
				new Object[] { workerThreadsMin, workerThreadsMax,
						selectorThreads });

		BasicThreadFactory factory = new BasicThreadFactory.Builder()
				.namingPattern(serverName + "-%d").build();
		LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		ExecutorService invoker = new ThreadPoolExecutor(workerThreadsMin,
				workerThreadsMax, 0, TimeUnit.SECONDS, queue, factory);

		TThreadedSelectorServer.Args serverArgs = new TThreadedSelectorServer.Args(
				serverTransport).inputTransportFactory(inTransportFactory)
				.outputTransportFactory(outTransportFactory)
				.inputProtocolFactory(tProtocolFactory)
				.outputProtocolFactory(tProtocolFactory).processor(processor)
				.executorService(invoker).acceptQueueSizePerThread(1000)
				.acceptPolicy(AcceptPolicy.FAIR_ACCEPT)
				.selectorThreads(selectorThreads);

		return new TThreadedSelectorServerWithMetrics(serverArgs);

	}

	/**
	 * 获取一个基于线程池的 Server.
	 *
	 * @param socketAddr
	 *            server address.
	 * @param socketPort
	 *            server port.
	 * @param workerThreadsMin
	 *            最小处理线程数.
	 * @param workerThreadsMax
	 *            最大处理线程数.
	 * @param processor
	 *            处理者.
	 *
	 * @return
	 */
	public static TServer getThreadPoolServer(InetAddress socketAddr,
			int socketPort, int workerThreadsMin, int workerThreadsMax,
			TProcessor processor) {
		boolean socketKeepalive = true;
		int socketSendBuffSizeInBytes = 10 * 1024 * 1024;
		int socketRecvBuffSizeInBytes = 10 * 1024 * 1024;
		int tBinaryProtocolReadLengthInBytes = 64 * 1024 * 1024;
		int tFramedTransportMaxLengthInBytes = 30 * 1024 * 1024;

		TServerTransport serverTransport = null;
		try {
			serverTransport = new TCustomServerSocket(new InetSocketAddress(
					socketAddr, socketPort), socketKeepalive,
					socketSendBuffSizeInBytes, socketRecvBuffSizeInBytes);
		} catch (TTransportException e) {
			throw new RuntimeException(String.format(
					"Unable to create thrift socket to %s:%s", socketAddr,
					socketPort), e);
		}

		logger.info(String.format("Binding thrift service to %s:%s",
				socketAddr, socketPort));

		// Protocol factory
		TProtocolFactory tProtocolFactory = new TBinaryProtocol.Factory(true,
				true, tBinaryProtocolReadLengthInBytes * 1024 * 1024);
		logger.info(
				"Using TBinaryProtocol with a max read length of {} bytes.",
				tBinaryProtocolReadLengthInBytes);

		// Transport factory
		TTransportFactory inTransportFactory, outTransportFactory;

		inTransportFactory = new TFramedTransport.Factory(
				tFramedTransportMaxLengthInBytes * 1024 * 1024);
		outTransportFactory = new TFramedTransport.Factory(
				tFramedTransportMaxLengthInBytes * 1024 * 1024);

		logger.info(
				"Using TFramedTransport with a max frame size of {} bytes.",
				tFramedTransportMaxLengthInBytes);

		workerThreadsMin = workerThreadsMin <= 0 ? processorsOfCPU
				: workerThreadsMin;
		workerThreadsMax = workerThreadsMax <= 0 ? processorsOfCPU
				: workerThreadsMax;
		logger.info("Using worker threads: min {} , max {} .", new Object[] {
				workerThreadsMin, workerThreadsMax });

		TThreadPoolServer.Args serverArgsThreadPool = new TThreadPoolServer.Args(
				serverTransport).inputTransportFactory(inTransportFactory)
				.outputTransportFactory(outTransportFactory)
				.inputProtocolFactory(tProtocolFactory)
				.outputProtocolFactory(tProtocolFactory).processor(processor)
				.maxWorkerThreads(workerThreadsMax)
				.minWorkerThreads(workerThreadsMin);

		return new TThreadPoolServer(serverArgsThreadPool);
	}

	/**
	 * 快速获取一个半同步半异步 Server.
	 *
	 * @param socketAddr
	 *            server address.
	 * @param socketPort
	 *            server port.
	 * @param workerThreadsMin
	 *            最小处理线程数.
	 * @param workerThreadsMax
	 *            最大处理线程数.
	 * @param processor
	 *            处理者.
	 * @param serverName
	 *            server name.
	 * @return
	 */
	public static TServer getTHsHaServer(InetAddress socketAddr,
			int socketPort, int workerThreadsMin, int workerThreadsMax,
			TProcessor processor, String serverName) {

		boolean socketKeepalive = true;
		int socketSendBuffSizeInBytes = 10 * 1024 * 1024;
		int socketRecvBuffSizeInBytes = 10 * 1024 * 1024;
		int tBinaryProtocolReadLengthInBytes = 64 * 1024 * 1024;
		int tFramedTransportMaxLengthInBytes = 30 * 1024 * 1024;

		TNonblockingServerTransport serverTransport;
		try {
			serverTransport = new TCustomNonblockingServerSocket(
					new InetSocketAddress(socketAddr, socketPort),
					socketKeepalive, socketSendBuffSizeInBytes,
					socketRecvBuffSizeInBytes);
		} catch (TTransportException e) {
			throw new RuntimeException(String.format(
					"Unable to create thrift socket to %s:%s", socketAddr,
					socketPort), e);
		}

		logger.info(String.format("Binding thrift service to %s:%s",
				socketAddr, socketPort));

		// Protocol factory
		TProtocolFactory tProtocolFactory = new TBinaryProtocol.Factory(true,
				true, tBinaryProtocolReadLengthInBytes * 1024 * 1024);
		logger.info(
				"Using TBinaryProtocol with a max read length of {} bytes.",
				tBinaryProtocolReadLengthInBytes);

		// Transport factory
		TTransportFactory inTransportFactory, outTransportFactory;

		inTransportFactory = new TFramedTransport.Factory(
				tFramedTransportMaxLengthInBytes * 1024 * 1024);
		outTransportFactory = new TFramedTransport.Factory(
				tFramedTransportMaxLengthInBytes * 1024 * 1024);

		logger.info(
				"Using TFramedTransport with a max frame size of {} bytes.",
				tFramedTransportMaxLengthInBytes);

		workerThreadsMin = workerThreadsMin <= 0 ? processorsOfCPU
				: workerThreadsMin;
		workerThreadsMax = workerThreadsMax <= 0 ? processorsOfCPU
				: workerThreadsMax;

		logger.info("Using worker threads: min {} , max {}. ", new Object[] {
				workerThreadsMin, workerThreadsMax });

		BasicThreadFactory factory = new BasicThreadFactory.Builder()
				.namingPattern(serverName + "-%d").build();

		LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		ExecutorService invoker = new ThreadPoolExecutor(workerThreadsMin,
				workerThreadsMax, 60, TimeUnit.SECONDS, queue, factory);

		THsHaServer.Args serverArgs = new THsHaServer.Args(serverTransport)
				.inputTransportFactory(inTransportFactory)
				.outputTransportFactory(outTransportFactory)
				.inputProtocolFactory(tProtocolFactory)
				.outputProtocolFactory(tProtocolFactory).processor(processor)
				.executorService(invoker);

		return new THsHaServer(serverArgs);

	}

}
