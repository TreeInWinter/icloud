package com.icloud.tops.thrift.server;

import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import service.demo.Hello;

import com.icloud.framework.thrift.server.TThreadedSelectorServer;
import com.icloud.framework.util.TZCuratorFrameworkNoProperty;

public class HelloServer {
	public static void startServer() {

	}

	private static Logger log = LoggerFactory.getLogger(HelloServer.class);

	public static HelloHandler handler;

	public static Hello.Processor processor;

	public static void main(String[] args) {
		try {
			// final int port = Integer.parseInt(args[0]);
			final int port = 9893;
			final String ip = "localhost";
			handler = new HelloHandler();
			processor = new Hello.Processor(handler);
			Runnable simple = new Runnable() {
				public void run() {
					simple(processor, ip, port);
				}
			};
			new Thread(simple).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void simple(Hello.Processor processor, String ip, int port) {
		try {
			log.info("start simple server.");
			// TServerTransport serverTransport = new TServerTransport(port);
			TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);
			// Use this for a multithreaded server
			TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(serverTransport).processor(processor);
			// args.maxWorkerThreads(10);
			args.workerThreads(10);
			// args.protocolFactory(new TBinaryProtocol.Factory());
			args.transportFactory(new TFramedTransport.Factory());
			TThreadedSelectorServer server = new TThreadedSelectorServer(args);
			System.out.println("Starting the simple server...");
			/**
			 * 注册一个节点
			 */
			// TZCuratorFramework.registerRpc(ip + ":" + port);
			// System.out.println("registerRpc: " + ip);
			TZCuratorFrameworkNoProperty.registerRpc(ip + ":" + port, "/test/hello", "Hello", "0", "1");
			server.serve();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
