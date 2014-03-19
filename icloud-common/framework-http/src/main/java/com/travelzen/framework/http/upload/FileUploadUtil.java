package com.travelzen.framework.http.upload;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelzen.framework.core.util.TZUtil;


public class FileUploadUtil {

	static private Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);;
	
	static AbstractHttpClient httpClient;

	static {

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
				.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory
				.getSocketFactory()));

		PoolingClientConnectionManager cm = new PoolingClientConnectionManager(
				schemeRegistry, new SystemDefaultDnsResolver());

		// Increase max total connection to 200
		cm.setMaxTotal(200);

		cm.setDefaultMaxPerRoute(20);

		httpClient = new DefaultHttpClient(cm);
		
	}

	public static int  upload(URI targetUri, File filecontent,
			Map<String, StringBody> param) throws ClientProtocolException,
			IOException {
		
		logger.debug("upload:{}",param.toString());

		try {
			HttpPost httpPost = new HttpPost(targetUri);

			FileBody content = new FileBody(filecontent);

			MultipartEntity reqEntity = new MultipartEntity();

			reqEntity.addPart("content", content);// upload为请求后台的File upload;属性

			for (Map.Entry<String, StringBody> entry : param.entrySet()) {
				reqEntity.addPart(entry.getKey(), entry.getValue());
			}

			httpPost.setEntity(reqEntity);

			HttpResponse response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {

				HttpEntity resEntity = response.getEntity();

				// System.out.println(EntityUtils.toString(resEntity));//
				// httpclient自带的工具类读取返回数据

				EntityUtils.consume(resEntity);
				
				return statusCode;
				

			} else {
				
				return statusCode;
				// System.out.println(EntityUtils.toString(response.getEntity()));//
				// httpclient自带的工具类读取返回数据
			}

		} catch(Exception e){
			logger.error("err{}",TZUtil.stringifyException(e));
			
		}finally {
			try {
//				httpClient.getConnectionManager().shutdown();
			} catch (Exception ignore) {

			}
		}
		
		return HttpStatus.SC_OK;

	}

	static public void upload() throws ClientProtocolException, IOException {

		try {
			HttpPost httpPost = new HttpPost(
					"http://localhost:8380/jproxy-bridge/upload/jproxy");

			FileBody content = new FileBody(new File(
					"/tmp/pac_screenshot_21549538.5394646.jpg"));

			StringBody description = new StringBody("just test");

			StringBody domain = new StringBody("sina.com.cn");

			MultipartEntity reqEntity = new MultipartEntity();

			reqEntity.addPart("content", content);// upload为请求后台的File upload;属性

			reqEntity.addPart("domain", domain);// upload为请求后台的File upload;属性

			reqEntity.addPart("description", description);// str 为请求后台的String
															// str;属性

			httpPost.setEntity(reqEntity);

			HttpResponse response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {

				HttpEntity resEntity = response.getEntity();

				System.out.println(EntityUtils.toString(resEntity));// httpclient自带的工具类读取返回数据

				EntityUtils.consume(resEntity);

			} else {
				System.out.println(EntityUtils.toString(response.getEntity()));// httpclient自带的工具类读取返回数据
			}

		} finally {
			try {
				httpClient.getConnectionManager().shutdown();
			} catch (Exception ignore) {

			}
		}
	}

	public static void main(String argv[]) {
		try {
			upload();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
