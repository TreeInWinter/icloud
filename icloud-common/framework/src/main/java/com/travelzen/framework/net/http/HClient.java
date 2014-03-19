package com.travelzen.framework.net.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelzen.framework.net.http.entity.HttpClientConfig;
import com.travelzen.framework.util.FileUtils;

/**
 * 
 * @author jianyesun
 * 
 */
public class HClient {

	/** send protocol */
	public static final String PROTOCOL = "http";

	private static Logger LOG = LoggerFactory.getLogger(HClient.class);

	/** send host */
	private String ip;

	/** send port */
	private int port;

	/** send target */
	private String target = "/wj/";

	/** send ContentMap */
	private Map<String, String> content = new HashMap<String, String>();

	/** send HeaderMap */
	private Map<String, String> header = new HashMap<String, String>();

	/** send simpleContent */
	private String simpleContent;

	private File file;

	/** send contentType */
	private String contentType = "application/octet-stream";

	/** send charset */
	private static final String CHARSET = "UTF-8";

	private HttpMethod method;

	private InputStream in = null;

	private int timeout = 5000;

	private HttpClient client = null;

	private byte[] byteArr;

	public HClient() {
		client = new HttpClient();
	}

	/**
	 * send by Post mode
	 * 
	 * @return
	 */
	public boolean executePost() {
		method = getPostMethod();
		return core();
	}

	/**
	 * send by Get mode
	 * 
	 * @return
	 */
	public boolean executeGet() {
		method = getGetMethod();
		return core();
	}

	/**
	 * Core
	 * 
	 * @param method
	 * @return
	 */
	private boolean core() {
		client.getHostConfiguration().setHost(ip, port, PROTOCOL);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
		client.getHttpConnectionManager().getParams().setSoTimeout(50000);
		long timeout = client.getHttpConnectionManager().getParams().getConnectionTimeout();
		LOG.info("set timeout to {}", timeout);

		try {
			if (header.size() != 0) {
				for (Entry<String, String> entry : header.entrySet()) {
					method.setRequestHeader(entry.getKey(), entry.getValue());
				}
			}
			print(client);
			client.executeMethod(method);
			LOG.info("ResponseStatus:{}", method.getStatusLine());
			if (method.getStatusCode() != HttpServletResponse.SC_OK) {
				return false;
			}
			in = method.getResponseBodyAsStream();
		} catch (Exception e) {
			LOG.error("", e);
			return false;
		}
		return true;
	}

	/**
	 * get the PostMethod
	 * 
	 * @return
	 */
	public HttpMethod getPostMethod() {
		PostMethod post = new PostMethod(target);

		if (simpleContent != null) {
			try {
				StringRequestEntity requestEntity = new StringRequestEntity(simpleContent, contentType, CHARSET);
				post.setRequestEntity(requestEntity);

			} catch (UnsupportedEncodingException e) {
				LOG.error("", e);
			}
		} else if (content.size() != 0) {
			post.setRequestBody(getNameValuePairArray());

		} else if (file != null) {
			try {
				if (!file.exists()) {
					throw new IllegalAccessError("specified file dose not exist.");
				}
				if (file.isDirectory()) {
					throw new IllegalAccessError("specified file is a directory, must be a file.");
				}
				post.setRequestEntity(new FileRequestEntity(file, contentType));
			} catch (Exception e) {
				LOG.error("", e);
			}

		} else if (byteArr != null && byteArr.length > 0) {
			post.setRequestEntity(new ByteArrayRequestEntity(byteArr, contentType));
		}
		return post;
	}

	public void uploadFile(File file) {
		this.file = file;
	}

	/**
	 * get the GetMethod
	 * 
	 * @return
	 */
	public HttpMethod getGetMethod() {
		GetMethod get = new GetMethod(target);
		get.setQueryString(getNameValuePairArray());
		return get;
	}

	/**
	 * get the NameValuePair array
	 * 
	 * @return
	 */
	private NameValuePair[] getNameValuePairArray() {
		if (content.size() == 0)
			return new NameValuePair[0];
		NameValuePair[] nvp = new NameValuePair[content.size()];
		int i = 0;
		for (Entry<String, String> entry : content.entrySet()) {
			NameValuePair n = new NameValuePair();
			n.setName(entry.getKey());
			n.setValue(entry.getValue());
			nvp[i] = n;
			i++;
		}
		return nvp;
	}

	public String getStatus() {
		return getHeader("Status");
	}

	public String getHeader(String name) {
		Header header = method.getResponseHeader(name);
		return header == null ? null : header.getValue();
	}

	/**
	 * close the connection
	 * 
	 */
	public void close() {
		try {
			if (in != null)
				in.close();
			method.releaseConnection();
		} catch (IOException e) {
			LOG.error("", e);
		}
	}

	/**
	 * response a String
	 * 
	 * @return
	 */
	public String responseAsString() {
		String result = null;
		try {
			result = method.getResponseBodyAsString();
		} catch (IOException e) {
			LOG.error("", e);
		}
		return result;
	}

	/**
	 * response a InputStream
	 * 
	 * @return
	 */
	public InputStream responseAsStream() {
		return in;
	}

	/**
	 * response a byte[]
	 * 
	 * @param b
	 * @return
	 */
	public int responseAsByte(byte[] b) {
		int len = -1;
		try {
			if (in != null) {
				len = in.read(b);
			}
		} catch (IOException e) {
			LOG.error("", e);
		}
		return len;
	}

	/**
	 * response a byte[]
	 * 
	 * @param b
	 * @return
	 */
	public byte[] responseAsByte() {
		try {
			return method.getResponseBody();
		} catch (IOException e) {
			LOG.error("", e);
		}
		return null;
	}

	/**
	 * set the host
	 * 
	 * @param host
	 * @param port
	 * @param target
	 */
	public HClient setHost(String ip, int port, String target) {
		this.ip = ip;
		this.port = port;
		this.target = target;
		return this;
	}

	/**
	 * set the host by HttpClientConfig
	 * 
	 * @param config
	 */
	public HClient setHost(HttpClientConfig config) {
		return setHost(config.getIP(), config.getPort(), config.getTarget());
	}

	/**
	 * set the content by the map
	 * 
	 * @param content
	 */
	public void setContent(Map<String, String> content) {
		this.content.putAll(content);
	}

	/**
	 * set the content by (key|value)
	 * 
	 * @param key
	 * @param value
	 */
	public void setContent(String key, String value) {
		this.content.put(key, value);
	}

	/**
	 * set the content by (key|File)
	 * 
	 * @param key
	 * @param file
	 */
	public void setContent(String key, File file) {
		FileUtils fu = new FileUtils(file);
		String value = "";
		if (fu.exists())
			value = fu.readString();
		setContent(key, value);
	}

	/**
	 * set the simpleContent by the string
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.simpleContent = content;
	}

	/**
	 * set the simpleContent by the file
	 * 
	 * @param file
	 */
	public void setContent(File file) {
		setContent(new FileUtils(file).readString());
	}

	/**
	 * set the header by Map
	 * 
	 * @param header
	 */
	public void setHeader(Map<String, String> header) {
		this.header.putAll(header);
	}

	/**
	 * set the header by (key|value)
	 * 
	 * @param key
	 * @param value
	 */
	public void setHeader(String key, String value) {
		this.header.put(key, value);
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setByteArr(byte[] byteArr) {
		this.byteArr = byteArr;
	}

	/**
	 * print
	 * 
	 * @param client
	 */
	private void print(HttpClient client) {
		String url = client.getHostConfiguration().getHostURL();
		String target = method.getPath();
		String querystring = method.getQueryString();
		url = url + target + (querystring == null ? "" : "?" + querystring);
		LOG.info("connection - >> {}", url);
	}

	public void clearContent() {
		this.content.clear();
		this.simpleContent = null;
	}

	public void clearHeader() {
		this.header.clear();
	}

	public void clearHost() {
		this.ip = null;
		this.port = 0;
		this.target = null;
	}

	public void clearFile() {
		this.file = null;
	}

	public void reset() {
		this.clearContent();
		this.clearHeader();
		this.clearHost();
		this.clearFile();
	}

	@SuppressWarnings("deprecation")
	public String GetHttp(String url) {
		System.out.println(url);
		// 构造HttpClient的实例
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		connectionManager.setMaxTotalConnections(1000);
		connectionManager.setMaxConnectionsPerHost(500);
		HttpClient httpClient = new HttpClient(connectionManager);
		httpClient.getParams().setParameter(HttpClientParams.SO_TIMEOUT, 5000);
		httpClient.getParams().setParameter(HttpClientParams.HTTP_CONTENT_CHARSET, "UTF-8");
		// 创建GET方法的实例
		GetMethod getMethod = new GetMethod(url);
		// 使用系统提供的默认的恢复策略
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

		try {
			// 执行getMethod
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOG.error("GetMothod Error url:", url);
			}
			// 读取内容
			String responseBody = getMethod.getResponseBodyAsString();
			// 处理内容
			return responseBody;
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			LOG.error("please check provided http address!", e);
			return null;
		} catch (IOException e) {
			// 发生网络异常
			LOG.error("", e);
			return null;
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
	}

}
