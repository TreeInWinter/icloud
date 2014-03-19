/**
 * 
 * Description: 日志工具类, 每个请求输出的所有日志由同一个rpid标识
 * Copyright: Copyright (c) 2009
 * Company:云壤
 * @author 任水
 * @version 1.0
 * @date Oct 7, 2009
 */
package com.travelzen.framework.core.util;

//import org.apache.log4j.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RPIDLogger {
	
	private static ThreadLocal<String> rpid = new ThreadLocal<String>();
	
	
	private static Logger log = LoggerFactory.getLogger(RPIDLogger.class);
	private static Logger shortLog = LoggerFactory.getLogger("mobplate_api_service_short_log");
//	private static Logger log = Logger.getRootLogger();
	/**
	 * 工具类日志
	 */
	/**
	 * 
	 * description: 记录系统的错误日志
	 * @param e
	 * 
	 */
	public static void error(Throwable e) {
//		e.printStackTrace();
//		System.out.println(e.getLocalizedMessage());
		error("",e);
	}
	/**
	 * 
	 * description: 记录系统的错误日志
	 * @param msg
	 * @param e
	 */
	public static void error(String msg, Throwable e) {
		log.error("["+ getRpid() + "]:" + msg, e);
	}
	/**
	 * 
	 * description: 记录系统的错误日志
	 * @param msg
	 */
	public static void error(String msg) {
		log.error("["+ getRpid() + "]:" + msg);
	}
	/**
	 * 
	 * description: 以debug方式记录日志
	 * @param msg
	 */
	public static void debug(String msg) {
			log.debug("["+ getRpid() + "]:" + msg);
	}
	/**
	 * 
	 * description: 以info方式记录日志
	 * @param msg
	 */
	public static void info(String msg) {
		log.info("["+ getRpid() + "]:" + msg);
	}
	/**
	 * 输出简要日志
	 * @param msg
	 */
	public static void shortLog(String msg){
		shortLog.info("["+ getRpid() + "]:" + msg);
	}
	public static String getRpid() {
		if(rpid.get() == null)
			setRpid(RandomUtil.getRandomStr(10));
		return rpid.get();
	}
	public static void setRpid(String rpid) {
		RPIDLogger.rpid.set(rpid);
	}
}

