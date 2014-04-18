/**
 * @author hongqiang.mao
 *
 * @date 2013-8-8 下午10:18:56
 *
 * @description client for execute get and post request
 */
package com.travelzen.framework.net.http;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class TZHttpClient {

	private String url;
	private Map<String, String> params;

	public TZHttpClient(String url) {
		this(url, null);
	}

	public TZHttpClient(String url, Map<String, String> params) {
		this.setUrl(url);
		this.setParams(params);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	/**
	 * 获取指定链接的响应信息，GET方式
	 *
	 * @param pUrl
	 * @return
	 */
	public String sendGetRequest() {
		StringBuffer lvFinalUrl = new StringBuffer(this.url);
		lvFinalUrl.append("?");
		if (null != this.params) {
			for (Entry<String, String> entry : this.params.entrySet()) {
				lvFinalUrl.append("&");
				lvFinalUrl.append(entry.getKey());
				lvFinalUrl.append("=");
				lvFinalUrl.append(entry.getValue());
			}
		}
		HttpClient lvClient = new DefaultHttpClient();
		HttpGet lvRequest = new HttpGet(lvFinalUrl.toString());
		String lvResponseString = null;
		try {
			HttpResponse response = lvClient.execute(lvRequest);
			lvResponseString = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lvResponseString;
	}

	/**
	 * 获取指定链接的响应信息，POST方式
	 *
	 * @param pUrl
	 * @param pParams
	 *            参数列表
	 * @return
	 */
	public String sendPostRequest() {
		HttpClient lvClient = new DefaultHttpClient();
		HttpPost lvHttpPost = new HttpPost(this.url);
		List<NameValuePair> lvParamsList = new ArrayList<NameValuePair>();
		if (null != this.params) {
			for (Entry<String, String> entry : this.params.entrySet()) {
				NameValuePair tmNameValuePair = new BasicNameValuePair(
						entry.getKey(), entry.getValue());
				lvParamsList.add(tmNameValuePair);
			}
		}
		String lvResponseString = null;
		try {
			lvHttpPost.setEntity(new UrlEncodedFormEntity(lvParamsList,
					HTTP.UTF_8));
			HttpResponse response = lvClient.execute(lvHttpPost);
			lvResponseString = EntityUtils.toString(response.getEntity(),
					"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lvResponseString;
	}

	public InputStream downCVSFile() {
		HttpClient lvClient = new DefaultHttpClient();
		HttpPost lvHttpPost = new HttpPost(this.url);
		List<NameValuePair> lvParamsList = new ArrayList<NameValuePair>();
		if (null != this.params) {
			for (Entry<String, String> entry : this.params.entrySet()) {
				NameValuePair tmNameValuePair = new BasicNameValuePair(
						entry.getKey(), entry.getValue());
				lvParamsList.add(tmNameValuePair);
			}
		}
		try {
			lvHttpPost.setEntity(new UrlEncodedFormEntity(lvParamsList,
					HTTP.UTF_8));
			HttpResponse response = lvClient.execute(lvHttpPost);
			InputStream content = response.getEntity().getContent();
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
