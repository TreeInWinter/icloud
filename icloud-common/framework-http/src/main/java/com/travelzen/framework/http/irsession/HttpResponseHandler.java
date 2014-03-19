package com.travelzen.framework.http.irsession;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.message.BasicHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelzen.framework.core.util.TZUtil;
import com.travelzen.framework.http.encode.ChardetUtil;
import com.travelzen.framework.http.encode.HtmlEncodingSniffer;

public class HttpResponseHandler implements ResponseHandler<HttpSession> {

	private Logger logger = LoggerFactory
			.getLogger(HttpResponseHandler.class);;
	protected ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	public HttpResponseHandler() {
		super();
	}

	private void processContent(HttpSession httpSession) {

		byte[] contentBytes = httpSession.getContentByte();

		String contentString = "";
		String encode = "UTF-8";

		try {

			encode = HtmlEncodingSniffer.sniffCharacterEncoding(contentBytes);

			if (null != encode) {
				try {
					contentString = new String(contentBytes, encode);
				} catch (UnsupportedEncodingException e) {

					logger.debug("UpportedEncodingException:{}", encode);

//					encode = CharsetDetectorUtils.detectSimplifiedChineseCharset(contentBytes);
					encode = ChardetUtil.getEncoding(contentBytes);

					contentString = new String(contentBytes, encode);

				}
			} else {
//				encode = CharsetDetectorUtils.detectSimplifiedChineseCharset(contentBytes);
				encode = ChardetUtil.getEncoding(contentBytes);
				contentString = new String(contentBytes, encode);
			}

		} catch (Exception e) {

			logger.error("error{}, stack:{}", e.getLocalizedMessage(),
					TZUtil.stringifyException(e));
		}

		httpSession.setContentString(contentString);
		httpSession.setEncode(encode);
	}

	public HttpSession handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {

		int statusCode = response.getStatusLine().getStatusCode();
		InputStream in = null;

		HttpSession httpSession = new HttpSession();

		if (statusCode != HttpStatus.SC_OK
				&& statusCode != HttpStatus.SC_NOT_MODIFIED) {

			logger.error("HttpStatus err:{},{}", statusCode);

			return null;
		} else {

			if (response instanceof BasicHttpResponse) {
				BasicHttpResponse basicHttpResponse = (BasicHttpResponse) response;
				httpSession.setBasicHttpResponse(basicHttpResponse);
			}

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				in = entity.getContent();

				try {
					IOUtils.copy(in, buffer);

					IOUtils.closeQuietly(in);
					IOUtils.closeQuietly(buffer);

				} catch (IOException e) {
					logger.debug("err:{}", TZUtil.stringifyException(e));
					return null;
				}

				// System.out.println( new String (buffer.toByteArray(),"GBK"));

				httpSession.setContentByte(buffer.toByteArray());
				httpSession.setStatusCode(HttpStatus.SC_OK);
				processContent(httpSession);

				return httpSession;
				// This works
				// for (int i;(i = in.read()) >= 0;) System.out.print((char)i);
			}

		}
		return httpSession;
	}
}