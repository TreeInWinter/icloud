package com.icloud.framework.thrift.standardserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.icloud.framework.core.util.FBUtilities;
import com.icloud.framework.thrift.server.RPCConfig;
import com.icloud.framework.thrift.server.ThriftServerFacade;



public abstract class StandardRPCDaemon {
	static public Logger logger = LoggerFactory
			.getLogger(StandardRPCDaemon.class);

	protected InetAddress listenAddr;
	protected int listenPort;

	public int getMIN_WORKER_THREADS() {
		return MIN_WORKER_THREADS;
	}

	public void setMIN_WORKER_THREADS(int mIN_WORKER_THREADS) {
		MIN_WORKER_THREADS = mIN_WORKER_THREADS;
	}

	public int MIN_WORKER_THREADS = 64;

	// private final static String DEFAULT_CONFIGURATION =
	// "standard-rpc-server.yaml";

	static int processors = Runtime.getRuntime().availableProcessors();
	Executor taskExecutor = Executors.newFixedThreadPool(processors * 8);

	private TServer serverEngine;

	private static RPCConfig conf;
	protected String serverName;

	protected abstract String getServerConfigFileName();

	private void setupConf() throws ConfigurationException {

		String configUrl = getServerConfigFileName();

		logger.info("Loading settings from " + configUrl);

		InputStream input =
				this.getClass().getClassLoader()
				.getResourceAsStream(configUrl);

		Constructor constructor = new Constructor(RPCConfig.class);

		Yaml yaml = new Yaml(constructor);
		conf = (RPCConfig) yaml.load(input);

	}

	protected abstract TProcessor getProcessor();

	protected void setup() throws IOException {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				logger.error("Fatal exception in thread " + t, e);
				if (e instanceof OutOfMemoryError) {
					System.exit(100);
				}
			}
		});

		logger.info("Heap size: {}/{}", Runtime.getRuntime().totalMemory(),
				Runtime.getRuntime().maxMemory());
		CLibrary.tryMlockall();

		try {
			setupConf();
		} catch (ConfigurationException e1) {
			e1.printStackTrace();
			logger.error(e1.getLocalizedMessage());
		}

		InetAddress listenAddr;
		int listenPort;

		listenPort = conf.rpc_port;
		listenAddr = InetAddress.getByName(conf.rpc_address);

		/*
		 * If ThriftAddress was left completely unconfigured, then assume the
		 * same default as ListenAddress
		 */
		if (listenAddr == null)
			listenAddr = InetAddress.getByName(FBUtilities.getIp());

		serverEngine = ThriftServerFacade.getThreadedSelectorServer(listenAddr,
				listenPort, conf.server_wokrer_threads_min,
				conf.server_wokrer_threads_max, conf.server_selector_threads,
				getProcessor(), conf.server_name);

	}

	/** hook for JSVC */
	public void start() {
		logger.info("Listening for thrift clients...");

//		try {
//			YRCuratorFramework.getInstance().registerRpc(NetworkUtil.getLocalHostName() + ":"
//					+ listenPort);
//		} catch (Exception e) {
//			logger.error("向zookeeper注册rpc失败", e);
//		}

		serverEngine.serve();
	}

	/** hook for JSVC */
	public void stop() {
		// this doesn't entirely shut down Cassandra, just the Thrift server.
		// jsvc takes care of taking the rest down
		logger.info("Cassandra shutting down...");
		serverEngine.stop();
	}

	public static void main(String[] args) {
		// ZooKeeperUtil.regeditZookeeper(
		// ZooKeeperUtil.getClient(conf.zookeeper_connect_string, "test"),
		// FBUtilities.getIp());
		// new StandardServerDaemon("test").activate();
	}

	/**
	 * A convenience method to initialize and start the daemon in one shot.
	 */
	public void activate() {
		String pidFile = System.getProperty("rpc-pidfile");

		try {
			setup();

			if (pidFile != null) {
				new File(pidFile).deleteOnExit();
			}

			start();
			logger.info("server successed started ...");

		} catch (Throwable e) {
			String msg = "Exception encountered during startup.";
			logger.error(msg, e);

			// try to warn user on stdout too, if we haven't already detached
			System.out.println(msg);
			e.printStackTrace();

			System.exit(3);
		}
	}

}
