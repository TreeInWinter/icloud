package com.travelzen.framework.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelzen.framework.core.util.TZUtil;

public class HttpURIUtil {

	static final Logger logger = LoggerFactory.getLogger(HttpURIUtil.class);

 
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
/*
	private static HttpRequestBase getHttpRequest(String methdName) {
		return methodMap.get(methdName);
	}
*/
	public static URI getURI(String urlStr) {

		URI uri = null;

		try {
			uri = new URI(urlStr);
		} catch (URISyntaxException e) {

			logger.error("URISyntaxException:{},{}", urlStr,
					TZUtil.stringifyException(e));

			try {
				urlStr = URIUtil.encodeAll(urlStr);
			} catch (URIException e1) {
				logger.error("err getURL:{}", TZUtil.stringifyException(e));
				e1.printStackTrace();
			}

		}

		return uri;

	}

	// 传过来的queryString应该是合法的， 如果不合法， 再做encode，确保合法性
	public static String getRequestUrlString(HttpServletRequest request) {

		String fullURL = "";

		try {

			String urlString = request.getRequestURL().toString();

			String encodedRequestURL = URIUtil.encodePath(urlString);

			String queryString = request.getQueryString();

			if (null == queryString) {
				fullURL = encodedRequestURL;
			} else {
				fullURL = encodedRequestURL + "?" + queryString;
			}

		} catch (URIException e1) {
			e1.printStackTrace();
		}
		// http://robinkin:8280/jproxy-feeder/urlset/add

		// request.getPathTranslated();
		// //
		// /home/liangwang/repo/mobile/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp2/wtpwebapps/jproxy/jproxy-feeder/urlset/add
		//
		// request.getPathInfo();
		// // /jproxy-feeder/urlset/add
		//
		// request.getRequestURI();
		// // /jproxy-feeder/urlset/add
		//
		// request.getContextPath();
		// /

		// url=http%3A%2F%2Fnews.sohu.com%2F20040723%2Fn221153893.shtml&setname=test

		// HttpGet will translate a urlString into a URI, so , we should do the
		// check first,
		// if failed, call the encoder
//		try {
//			try {
//				URI url = new URI(fullURL);
//			} catch (URISyntaxException e) {
//				fullURL = UriUtils.encodeQuery(fullURL, "UTF-8");
//
//			}
//
//		} catch (UnsupportedEncodingException e) {
//			logger.error(e.getLocalizedMessage());
//		}

		return fullURL;
	}
 

	public static Header[] getHeadersByHttpServletRequest(HttpServletRequest req) {

		List<Header> headers = new ArrayList<Header>();

		for (Enumeration<String> e = req.getHeaderNames(); e.hasMoreElements();) {

			String headerName = e.nextElement().toString();

			headers.add(new BasicHeader(headerName, req.getHeader(headerName)));

		}

		return headers.toArray(new Header[] {});

	}
  

}
