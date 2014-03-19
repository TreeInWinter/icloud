package com.travelzen.framework.logger.ri;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import com.travelzen.framework.core.util.TZUtil;


public class RequestIdentityLogger implements Logger {

	private Logger logger;

	private static Map<String, RequestIdentityLogger> LOGGER_CACHE = new ConcurrentHashMap<>();

	public static Logger getLogger(Class<?> clz) {
		String key = clz.getName();
		RequestIdentityLogger topsLogger = LOGGER_CACHE.get(key);
		if (topsLogger == null) {
			topsLogger = new RequestIdentityLogger(key);
			LOGGER_CACHE.put(key, topsLogger);
		}
		return topsLogger;
	}

	private RequestIdentityLogger(String clz) {
		logger = LoggerFactory.getLogger(clz);
	}

	private static String addPrefix(String msg) {
		String ri = RequestIdentityHolder.get();
		return TZUtil.isEmpty(ri) ? msg : '<' + ri + '>' + ' ' + msg;
	}

	@Override
	public String getName() {
		return logger.getName();
	}

	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public void trace(String msg) {
		logger.trace(addPrefix(msg));
	}

	@Override
	public void trace(String format, Object arg) {
		logger.trace(addPrefix(format), arg);
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		logger.trace(addPrefix(format), arg1, arg2);
	}

	@Override
	public void trace(String format, Object... args) {
		logger.trace(addPrefix(format), args);
	}

	@Override
	public void trace(String msg, Throwable t) {
		logger.trace(addPrefix(msg), t);
	}

	@Override
	public boolean isTraceEnabled(Marker marker) {
		return logger.isTraceEnabled(marker);
	}

	@Override
	public void trace(Marker marker, String msg) {
		logger.trace(marker, addPrefix(msg));
	}

	@Override
	public void trace(Marker marker, String format, Object arg) {
		logger.trace(marker, addPrefix(format), arg);
	}

	@Override
	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		logger.trace(marker, addPrefix(format), arg1, arg2);
	}

	@Override
	public void trace(Marker marker, String format, Object... argArray) {
		logger.trace(marker, addPrefix(format), argArray);
	}

	@Override
	public void trace(Marker marker, String msg, Throwable t) {
		logger.trace(marker, addPrefix(msg), t);
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(String msg) {
		logger.debug(addPrefix(msg));
	}

	@Override
	public void debug(String format, Object arg) {
		logger.debug(addPrefix(format), arg);
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		logger.debug(addPrefix(format), arg1, arg2);
	}

	@Override
	public void debug(String format, Object... args) {
		logger.debug(addPrefix(format), args);
	}

	@Override
	public void debug(String msg, Throwable t) {
		logger.debug(addPrefix(msg), t);
	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		return logger.isDebugEnabled(marker);
	}

	@Override
	public void debug(Marker marker, String msg) {
		logger.debug(marker, addPrefix(msg));
	}

	@Override
	public void debug(Marker marker, String format, Object arg) {
		logger.debug(marker, addPrefix(format), arg);
	}

	@Override
	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		logger.debug(marker, addPrefix(format), arg1, arg2);
	}

	@Override
	public void debug(Marker marker, String format, Object... argArray) {
		logger.debug(marker, addPrefix(format), argArray);
	}

	@Override
	public void debug(Marker marker, String msg, Throwable t) {
		logger.debug(marker, addPrefix(msg), t);
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(String msg) {
		logger.info(addPrefix(msg));
	}

	@Override
	public void info(String format, Object arg) {
		logger.info(addPrefix(format), arg);
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		logger.info(addPrefix(format), arg1, arg2);
	}

	@Override
	public void info(String format, Object... args) {
		logger.info(addPrefix(format), args);
	}

	@Override
	public void info(String msg, Throwable t) {
		logger.info(addPrefix(msg), t);
	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		return logger.isInfoEnabled(marker);
	}

	@Override
	public void info(Marker marker, String msg) {
		logger.info(marker, addPrefix(msg));
	}

	@Override
	public void info(Marker marker, String format, Object arg) {
		logger.info(marker, addPrefix(format), arg);
	}

	@Override
	public void info(Marker marker, String format, Object arg1, Object arg2) {
		logger.info(marker, addPrefix(format), arg1, arg2);
	}

	@Override
	public void info(Marker marker, String format, Object... argArray) {
		logger.info(marker, addPrefix(format), argArray);
	}

	@Override
	public void info(Marker marker, String msg, Throwable t) {
		logger.info(marker, addPrefix(msg), t);
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(String msg) {
		logger.warn(addPrefix(msg));
	}

	@Override
	public void warn(String format, Object arg) {
		logger.warn(addPrefix(format), arg);
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		logger.warn(addPrefix(format), arg1, arg2);
	}

	@Override
	public void warn(String format, Object... args) {
		logger.warn(addPrefix(format), args);
	}

	@Override
	public void warn(String msg, Throwable t) {
		logger.warn(addPrefix(msg), t);
	}

	@Override
	public boolean isWarnEnabled(Marker marker) {
		return logger.isWarnEnabled(marker);
	}

	@Override
	public void warn(Marker marker, String msg) {
		logger.warn(marker, addPrefix(msg));
	}

	@Override
	public void warn(Marker marker, String format, Object arg) {
		logger.warn(marker, addPrefix(format), arg);
	}

	@Override
	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		logger.warn(marker, addPrefix(format), arg1, arg2);
	}

	@Override
	public void warn(Marker marker, String format, Object... argArray) {
		logger.warn(marker, addPrefix(format), argArray);
	}

	@Override
	public void warn(Marker marker, String msg, Throwable t) {
		logger.warn(marker, addPrefix(msg), t);
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void error(String msg) {
		logger.error(addPrefix(msg));
	}

	@Override
	public void error(String format, Object arg) {
		logger.error(addPrefix(format), arg);
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		logger.error(addPrefix(format), arg1, arg2);
	}

	@Override
	public void error(String format, Object... args) {
		logger.error(addPrefix(format), args);
	}

	@Override
	public void error(String msg, Throwable t) {
		logger.error(addPrefix(msg), t);
	}

	@Override
	public boolean isErrorEnabled(Marker marker) {
		return logger.isErrorEnabled(marker);
	}

	@Override
	public void error(Marker marker, String msg) {
		logger.error(marker, addPrefix(msg));
	}

	@Override
	public void error(Marker marker, String format, Object arg) {
		logger.error(marker, addPrefix(format), arg);
	}

	@Override
	public void error(Marker marker, String format, Object arg1, Object arg2) {
		logger.error(marker, addPrefix(format), arg1, arg2);
	}

	@Override
	public void error(Marker marker, String format, Object... argArray) {
		logger.error(marker, addPrefix(format), argArray);
	}

	@Override
	public void error(Marker marker, String msg, Throwable t) {
		logger.error(marker, addPrefix(msg), t);
	}

}
