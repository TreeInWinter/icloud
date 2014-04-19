package com.icloud.framework.http;

public class HttpContent
{
  private String contentType;
  private String url;
  private byte[] content;

  public String getContentType()
  {
    return this.contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public byte[] getContent() {
    return this.content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }
}