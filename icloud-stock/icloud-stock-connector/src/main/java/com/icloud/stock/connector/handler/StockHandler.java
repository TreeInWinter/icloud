package com.icloud.stock.connector.handler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import com.icloud.stock.connector.parser.Parser;
import com.icloud.stock.connector.parser.ParserFactory;
import com.icloud.stock.connector.parser.StockParseFactory;
import com.icloud.framework.net.http.TZHttpClient;

public abstract class StockHandler<T> implements BaseHandler<T> {
	protected final static Logger LOGGER = LoggerFactory
			.getLogger(StockHandler.class);
	protected static ParserFactory factory = new StockParseFactory();
	protected Class<T> domainClass;
	protected String url;
	protected Map<String, String> params;

	private boolean isGet = false;

	protected StockHandler() {
		init();
	}

	protected StockHandler(boolean isGet) {
		this();
		this.isGet = isGet;
	}

	protected void init() {
		/**
		 * 获得泛型参数类型
		 */
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		domainClass = (Class) params[0];
	}

	public StockHandler(String url, Map<String, String> params)
			throws NoSuchFieldException, SecurityException,
			NoSuchMethodException {
		this();
		this.url = url;
		this.params = params;
	}

	public StockHandler(String url, Map<String, String> params, boolean isGet)
			throws NoSuchFieldException, SecurityException,
			NoSuchMethodException {
		this(isGet);
		this.url = url;
		this.params = params;
	}

	public StockHandler(String url) {
		this();
		this.url = url;
		this.params = new HashMap<String, String>();
	}

	public abstract void apply();

	public T getHttpData() {
		StopWatch stopwatch = new StopWatch(domainClass.getName());
		stopwatch.start("调用http接口");
		Map<String, String> params = new HashMap<String, String>();
		TZHttpClient client = new TZHttpClient(url, params);
		String str = null;
		if (this.isGet) {
			str = client.sendGetRequest();
		} else {
			str = client.sendPostRequest();
		}
		stopwatch.stop();
		stopwatch.start("解析数据");
		Parser<T> parser = (Parser<T>) factory.getParser(domainClass);
		T result = parser.parse(str, null);
		stopwatch.stop();
		LOGGER.info("{}:{}", stopwatch.getLastTaskName(), stopwatch.toString());
		return result;
	}
}
