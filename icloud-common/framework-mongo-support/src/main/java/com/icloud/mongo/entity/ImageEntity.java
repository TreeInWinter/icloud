package com.icloud.mongo.entity;

import java.io.InputStream;

public class ImageEntity {

	private InputStream inputStream;
	private Long version;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

}
