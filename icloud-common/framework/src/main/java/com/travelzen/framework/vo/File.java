package com.travelzen.framework.vo;

import java.io.Serializable;

public class File implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fileName;

	private String id;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
