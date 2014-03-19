/**
* author: Simon Lee
* Date  : Aug 30, 2013
*/
package com.travelzen.framework.web.util;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

/**
 * 辅助设置下载请求的response
 * @author simon
 *
 */
public class WebDownloadUtil {

	public static void setDownloadResponse(HttpServletResponse response, InputStream inputStream, String fileName) throws IOException {
		byte[] bytes = IOUtils.toByteArray(inputStream);
		setDownloadResponse(response, bytes, fileName);
	}

	public static void setDownloadResponse(HttpServletResponse response, byte[] bytes, String fileName) throws IOException {
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);// 设置文件名
		response.addIntHeader("Content-Length", bytes.length);// 设置下载文件大小
		response.setContentType("application/octet-stream");// 设置文件类型
		response.getOutputStream().write(bytes);
	}

}
