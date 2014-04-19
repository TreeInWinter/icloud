package com.icloud.framework.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.AutoRetryHttpClient;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.DefaultServiceUnavailableRetryStrategy;
import org.eclipse.jetty.io.bio.StringEndPoint;
import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.parser.DocumentBuilderImpl;
import org.lobobrowser.html.parser.InputSourceImpl;
import org.lobobrowser.html.test.UUBotHtmlRendererContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;
import com.icloud.framework.core.util.TZUtil;
import com.icloud.framework.core.xml.DomUtil;
import com.icloud.framework.http.irsession.HttpConnection;
import com.icloud.framework.http.irsession.HttpConstant;
import com.icloud.framework.http.irsession.HttpResponseHandler;
import com.icloud.framework.http.irsession.HttpSession;
import com.icloud.framework.http.irsession.HttpSessionTemplate;
import com.icloud.framework.http.irsession.HttpIRSessionConf.IrRequest;
import com.icloud.framework.http.irsession.HttpIRSessionConf.IrSession;

import freemarker.template.Configuration;


public class HttpUtil {

	static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	public static byte[] getContentBytesByURL(String urlStr)
			throws ClientProtocolException, IOException {

		HttpClient httpclient = new DefaultHttpClient();

		// Prepare a request object
		HttpGet httpget = new HttpGet(urlStr);

		// Execute the request
		HttpResponse response = httpclient.execute(httpget);

		// Examine the response status
		System.out.println(response.getStatusLine());

		// Get hold of the response entity
		HttpEntity entity = response.getEntity();

		// If the response does not enclose an entity, there is no need
		// to worry about connection release
		if (entity != null) {
			InputStream instream = entity.getContent();
			try {
				return IOUtils.toByteArray(instream);
			} catch (IOException ex) {

				throw ex;

			} catch (RuntimeException ex) {
				httpget.abort();
				throw ex;

			} finally {
				instream.close();
				httpclient.getConnectionManager().shutdown();
			}

			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			// httpclient.getConnectionManager().shutdown();
		}
		return null;
	}

	// public static byte[] getContentBytesByURL(String urlStr) {
	//
	// HttpClientParams params = new HttpClientParams();
	//
	// IRVMHttpMethodRetryHandler handler = new IRVMHttpMethodRetryHandler(
	// FrameworkHttpConstant.DEFAUL_RECONNECT_CNT, true);
	//
	// params.setParameter(HttpMethodParams.RETRY_HANDLER, handler);
	//
	// params.setIntParameter(FrameworkHttpConstant.RECONNET_TIME,
	// FrameworkHttpConstant.DEFAUL_RECONNECT_TIME);
	//
	// InputStreamReader inputStreamReader = null;
	//
	// HttpMethod httpMethod = new IRVMHttpMethod(urlStr);
	//
	// int rewaitCnt = 0;
	//
	// RETRYT: while (true) {
	//
	// HttpClient client = getHttpClientInstance();
	//
	// try {
	//
	// // HttpClient是必须每个线程独享的（这行代码会抛ConnectionPoolTimeoutException异常）
	//
	// params.setConnectionManagerTimeout(client
	// .getHttpConnectionManager()
	// .getParams()
	// .getIntParameter(
	// HttpConnectionManagerParams.CONNECTION_TIMEOUT,
	// 0));
	// client.setParams(params);
	//
	// // UsernamePasswordCredentials upc = new
	// // UsernamePasswordCredentials(
	// // "wangliang", "wangliang2011");
	// // client.getState().setCredentials(AuthScope.ANY, upc);
	//
	// client.executeMethod(httpMethod);
	//
	// // 重定向
	// int statuscode = httpMethod.getStatusCode();
	//
	// if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) ||
	//
	// (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) ||
	//
	// (statuscode == HttpStatus.SC_SEE_OTHER) ||
	//
	// (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
	//
	// // 读取新的URL地址
	//
	// Header header = httpMethod.getResponseHeader("location");
	//
	// if (header != null) {
	//
	// String newuri = header.getValue();
	//
	// if ((newuri == null) || (newuri.equals("")))
	//
	// newuri = "/";
	//
	// httpMethod.releaseConnection();
	//
	// return getContentBytesByURL(newuri);
	//
	// } else
	// logger.warn("Invalid redirect");
	// }
	//
	// // String response = new
	// // String(httpMethod.getResponseBodyAsString());
	//
	// InputStream responseInputStream = httpMethod
	// .getResponseBodyAsStream();
	//
	// String contentType = "";
	// if (responseInputStream.markSupported()) {
	// responseInputStream.mark(2);
	// int id1 = responseInputStream.read();
	// int id2 = responseInputStream.read();
	// responseInputStream.reset();
	//
	// // These numbers are magic. See the RFC 1952 Gzip File
	// // Format Specification located at:
	// // http://www.gzip.org/zlib/rfc-gzip.html
	// if (id1 == 0x1F && id2 == 0x8B) {
	// // we've got gzip data
	// contentType = "application/x-gzip-compressed";
	// }
	// }
	//
	// if (contentType != null
	// && contentType
	// .startsWith("application/x-gzip-compressed")) {
	// responseInputStream = new GZIPInputStream(
	// responseInputStream);
	//
	// return IOUtils.toByteArray(responseInputStream);
	//
	// } else {
	//
	// return checkGZip(responseInputStream);
	// }
	//
	// } catch (HttpException e) {
	// e.printStackTrace();
	// // continue RETRYT;
	// } catch (java.net.SocketTimeoutException e) {
	// // socket 超时
	// e.printStackTrace();
	//
	// // continue RETRYT;
	// } catch (InterruptedIOException e) {
	// // socket 超时
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	//
	// if (e instanceof ConnectionPoolTimeoutException) {
	// rewaitCnt++;
	// if (rewaitCnt <= FrameworkHttpConstant.MAX_REWAIT_CNT) {
	// logger.error("ConnectionPoolTimeout rewaitCnt:");
	// continue RETRYT;
	// } else {
	// logger.error("too many retry cnt" + rewaitCnt);
	// }
	//
	// }
	// e.printStackTrace();
	// logger.error(e.getMessage());
	// }
	//
	// finally {
	//
	// httpMethod.releaseConnection();
	// rewaitCnt = 0;// 重试计数器归零
	// }
	// // 正常结束的情况下 可以跳出循环，否则，不断循环，直到计数器满
	// break;
	//
	// }
	//
	// try {
	// return IOUtils.toByteArray(inputStreamReader);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return null;
	//
	// }
/*
	private ByteArrayOutputStream readChunked(byte[] bytes, boolean isChunked)
			throws IOException {
		if (!isChunked) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(bytes, 0, bytes.length);
			return baos;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int mark = 0;
		int pos = 0;
		int orginLength = bytes.length;
		while ((pos + 2) < orginLength) {

			if ((int) bytes[pos + 1] == 13 && (int) bytes[pos + 2] == 10) {

				byte[] length2Byte = new byte[pos - mark + 1];
				System.arraycopy(bytes, mark, length2Byte, 0, pos - mark + 1);
				int length2Int = Integer.parseInt(new String(length2Byte), 16);
				if (length2Int == 0)
					return baos;

				pos += 3;
				mark = pos;
				baos.write(bytes, mark, length2Int);

				pos += (length2Int - 1) + 3;
				mark = pos;
			} else {
				pos++;
			}
		}
		return baos;
	}
*/
	// public void testCookie() {
	// DefaultHttpClient httpclient = new DefaultHttpClient();
	// HttpResponse response = httpclient.execute(httppost);
	// CookieStore cookiestore = httpclient.getCookieStore();
	// // 得到Cookie
	// //
	// 第二次请求，把第一次请求的代码再复制一次。当然，变量名会重复，改一下即可。现在要在发送请求之前加上刚才得到的cookie,还是改上文的37行:
	//
	// DefaultHttpClient httpclient2 = new DefaultHttpClient();
	// httpclient2.setCookieStore(cookiestore);
	// // 把第一次请求的cookie加进去
	// HttpResponse response2 = httpclient2.execute(httppost2);
	//
	// }
/*
	private String getRequestUrlString(String requestURL, String queryString) {

		String fullURL = "";

		try {

			String encodedRequestURL = URIUtil.encodePath(requestURL);

			if (null == queryString) {
				fullURL = encodedRequestURL;
			} else {
				fullURL = encodedRequestURL + "?" + queryString;
			}

		} catch (URIException e1) {
			e1.printStackTrace();
		}

		// url=http%3A%2F%2Fnews.sohu.com%2F20040723%2Fn221153893.shtml&setname=test

		// HttpGet will translate a urlString into a URI, so , we should do the
		// check first,
		// if failed, call the encoder
		try {
			try {
				URI url = new URI(fullURL);
			} catch (URISyntaxException e) {
				fullURL = UriUtils.encodeQuery(fullURL, "UTF-8");

			}

		} catch (UnsupportedEncodingException e) {
			logger.error(e.getLocalizedMessage());
		}

		return fullURL;
	}
*/
	static Map<String, HttpRequestBase> methodMap = new HashMap<String, HttpRequestBase>();

	static {
		methodMap.put(HttpPost.METHOD_NAME, new HttpPost());
		methodMap.put(HttpGet.METHOD_NAME, new HttpGet());
	}

	private static HttpRequestBase getHttpRequest(String methdName) {
		return methodMap.get(methdName);
	}

	public static org.eclipse.jetty.server.Request getRequestByURLHead(
			String urlStr, String head) {
		StringEndPoint endpoint = new StringEndPoint();
		endpoint.setInput(head);

		HttpConnection connection = new HttpConnection(endpoint);

		try {
			connection.handle();

			org.eclipse.jetty.server.Request request = connection.getRequest();

			URI uri = HttpURIUtil.getURI(urlStr);

			request.setQueryString(uri.getQuery());
			request.setRequestURI(uri.toString());
			request.setProtocol(uri.getScheme());
			request.setRemoteHost(uri.getHost());
			request.setContextPath(uri.getPath());
			request.setPathInfo(uri.getPath());

			// use serverPort to represent the remote port
			request.setServerPort(uri.getPort());
			// request.setServletPath(url.get)

			// request.getConnection().getRequestFields();
			// request.getCookies();
			//
			// request.getHeaderNames();

			return request;
		} catch (IOException e) {
			logger.error("processHead:{}", TZUtil.stringifyException(e));
		}
		return null;

	}

	/**
	 * add some necessarily info to the http head
	 *
	 * @param httpRequest
	 * @param irRequest
	 * @param param
	 * @return
	 */
	public static Map<String, Object> preprocessRequest(
			HttpRequestBase httpRequest, IrSession irSession,
			IrRequest irRequest, Map<String, String> enteranceParameter) {

		Map<String, Object> result = new HashMap<String, Object>();

//		List<IrRequest> requests = irSession.requests;

		Configuration cfg = HttpSessionTemplate.getInstance()
				.getFreeMarkerCFG(irRequest);

		Map<String, String> param = Maps.newHashMap();

		param.putAll(enteranceParameter);

		param.put("referer", enteranceParameter.get("url"));

		String head = HttpSessionTemplate.getInstance().getTemplateValue(
				param, cfg, "head");

		HttpServletRequest req = HttpUtil.getRequestByURLHead(irRequest.uri,
				head);

		if (req != null) {

			httpRequest.setHeaders(HttpURIUtil
					.getHeadersByHttpServletRequest(req));

			HttpHost httpHost = new HttpHost(req.getServerName(),
					req.getServerPort(), req.getScheme());

			httpRequest.getParams().setParameter(ClientPNames.DEFAULT_HOST,
					httpHost);
			URI uri;
			try {

				String requestURI = req.getRequestURI();
				String queryString = req.getQueryString();
				if (null != queryString) {
					requestURI = requestURI + "?" + queryString;
				}

				uri = new URI(requestURI);
				httpRequest.setURI(uri);
			} catch (URISyntaxException e) {
				logger.error("URISyntaxException:{}",
						TZUtil.stringifyException(e));
			}

		}

		return result;
	}

	public static ThreadLocal<DocumentBuilderImpl> builder = new ThreadLocal<DocumentBuilderImpl>() {

		class NojsHtmlRendererContext extends UUBotHtmlRendererContext {
			// Override methods here to implement browser functionality
			public NojsHtmlRendererContext(HtmlPanel contextComponent) {
				super(contextComponent);
			}
		}


		public ThreadLocal<HtmlRendererContext> rendererContext = new ThreadLocal<HtmlRendererContext>() {

			ThreadLocal<HtmlPanel> htmlPanel = new ThreadLocal<HtmlPanel>() {
				@Override
				protected synchronized HtmlPanel initialValue() {
					return new HtmlPanel();
				}
			};

			@Override
			protected synchronized HtmlRendererContext initialValue() {
				return new NojsHtmlRendererContext(htmlPanel.get());
			}
		};

		@Override
		protected synchronized DocumentBuilderImpl initialValue() {
//			HtmlRendererContext rc = rendererContext.get();
//			UserAgentContext uc = rc.getUserAgentContext();

			return new DocumentBuilderImpl(rendererContext.get()
					.getUserAgentContext(), rendererContext.get());
		}
	};

	public static String simpleGetHttpContent(AbstractHttpClient client,
			URI uri, Map<String, Object> param) {

		HttpGet httpRequest = new HttpGet(uri);

//		DefaultServiceUnavailableRetryStrategy retryStrategy = new DefaultServiceUnavailableRetryStrategy();

		AutoRetryHttpClient autoRetryHttpClient = new AutoRetryHttpClient(
				client);

		DecompressingHttpClient decompressingHttpClient = new DecompressingHttpClient(
				autoRetryHttpClient);

		// client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(
		// retry_count, true));

		ResponseHandler<HttpSession> httpResponseHandler = new HttpResponseHandler();

		HttpSession httpSession = null;
		try {
			httpSession = decompressingHttpClient.execute(httpRequest,
					httpResponseHandler);
		} catch (HttpResponseException e) {
			logger.error("HttpResponseException:{} , {}", uri,
					e.getLocalizedMessage());

		} catch (SocketTimeoutException e) {
			logger.error("SocketTimeoutException: {} : {}", uri,
					e.getLocalizedMessage());
		} catch (Exception e) {
			logger.error("err: {} : {}", uri, TZUtil.stringifyException(e));
		}

		String contentString = httpSession.getContentString();

		try {

			InputStream in = new ByteArrayInputStream(
					contentString.getBytes("UTF-8"));

			InputStreamReader inputStreamReader = new InputStreamReader(in,
					"UTF-8");

			InputSource ips = new InputSourceImpl(inputStreamReader,
					uri.toString());

			Document document = null;
			try {
				document = builder.get().parse(ips);
			} catch (SAXException e) {
				logger.error("err: {} : {}", uri, TZUtil.stringifyException(e));
			} catch (IOException e) {
				logger.error("err: {} : {}", uri, TZUtil.stringifyException(e));
			}

			return DomUtil.serialize(document);

		} catch (Exception e) {
			logger.error("err : {}:{}", uri.toString(),
					TZUtil.stringifyException(e));
		}

		return "";

	}

	// socket方式获取数据
	/**
	 * maintain the status with the cookie in httpclient
	 *
	 * @param client
	 * @param param
	 * @return null if failed
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static HttpSession getHttpContent(AbstractHttpClient client,
			Map<String, Object> param, IrRequest irRequest) throws Exception {

//		Map ret = new HashMap();

		Map<String, String> entranceParameter = (Map<String, String>) param
				.get("entranceParameter");

		IrSession irSession = (IrSession) param.get("irsession");

		URI uri = (URI) param.get("uri");

//		String queryString = "";
//		String host = "";
		String body = "";

		// process method
		String method = (String) param.get("method");

		if (null != irRequest && irRequest.request != null) {
			method = irRequest.request.getMethod();
//			queryString = irRequest.request.getQueryString();
//			host = irRequest.request.getRemoteHost();
			body = irRequest.body;

		} else {
			method = HttpGet.METHOD_NAME;
		}

		String encoding = (String) param.get("encoding");

		int port = NumberUtils.toInt((String) param.get("port"));
		if (port == 0)
			port = 80;

		if (StringUtils.isEmpty(encoding)) {
			encoding = "GBK";
		}

		int http_socket_timeout = NumberUtils.toInt(
				(String) param.get("http.socket.timeout"), 15000);

		int retry_count = NumberUtils.toInt(
				(String) param.get(HttpConstant.RETRY_COUNT), 3);

		HttpRequestBase httpRequest;

		// BasicRequestLine requestLine = new BasicRequestLine(method,
		// requestURL, new HttpVersion(1, 1));

		httpRequest = getHttpRequest(method);

		// most important parameter
		// httpRequest.setURI(uri);

		// replace the reqeset head
		preprocessRequest(httpRequest, irSession, irRequest, entranceParameter);

		// HttpPost
		if (StringUtils.equalsIgnoreCase(method, HttpPost.METHOD_NAME)) {

			HttpPost hp = (HttpPost) httpRequest;

			StringEntity stringEntity = new StringEntity(body);
			hp.setEntity(stringEntity);

		}

		if (null == httpRequest) {
			logger.error("invalid method : {}", method);
			return null;
		}

		httpRequest.getParams().setParameter("http.socket.timeout",
				http_socket_timeout); // 为HttpMethod设置参数

		DefaultServiceUnavailableRetryStrategy retryStrategy = new DefaultServiceUnavailableRetryStrategy();

		AutoRetryHttpClient autoRetryHttpClient = new AutoRetryHttpClient(
				client,retryStrategy);

		DecompressingHttpClient decompressingHttpClient = new DecompressingHttpClient(
				autoRetryHttpClient);

		client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(
				retry_count, true));

		ResponseHandler<HttpSession> httpResponseHandler = new HttpResponseHandler();

		HttpSession httpSession = null;
		try {
			httpSession = decompressingHttpClient.execute(httpRequest,
					httpResponseHandler);
		} catch (HttpResponseException e) {
			logger.error("HttpResponseException:{} , {}", uri,
					e.getLocalizedMessage());

		} catch (SocketTimeoutException e) {
			logger.error("SocketTimeoutException: {} : {}", uri,
					e.getLocalizedMessage());
		} catch (Exception e) {
			logger.error("err: {} : {}", uri, TZUtil.stringifyException(e));
		}

		return httpSession;
	}

}
