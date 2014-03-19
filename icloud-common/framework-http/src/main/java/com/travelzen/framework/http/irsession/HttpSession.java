package com.travelzen.framework.http.irsession;

import org.apache.http.message.BasicHttpResponse;

public class HttpSession {

	String url;
	String contentString;
	String encode;
	String refer;
	String referDomain;
	String pageDomain;
	BasicHttpResponse basicHttpResponse;

	int statusCode;

	byte[] contentByte;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public String getRefer() {
		return refer;
	}

	public void setRefer(String refer) {
		this.refer = refer;
	}

	public String getReferDomain() {
		return referDomain;
	}

	public void setReferDomain(String referDomain) {
		this.referDomain = referDomain;
	}

	public String getPageDomain() {
		return pageDomain;
	}

	public void setPageDomain(String pageDomain) {
		this.pageDomain = pageDomain;
	}

	public String getContentString() {
		return contentString;
	}

	public void setContentString(String contentString) {
		this.contentString = contentString;
	}

	public BasicHttpResponse getBasicHttpResponse() {
		return basicHttpResponse;
	}

	public void setBasicHttpResponse(BasicHttpResponse basicHttpResponse) {
		this.basicHttpResponse = basicHttpResponse;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public byte[] getContentByte() {
		return contentByte;
	}

	public void setContentByte(byte[] contentByte) {
		this.contentByte = contentByte;
	}

}