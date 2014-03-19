package com.travelzen.spider.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleXmlSerializer;
import org.htmlcleaner.TagNode;
import org.springframework.web.util.HtmlUtils;

import com.travelzen.search.util.LogFileWriter;

//import com.easou.spider.common.Constants;
//import com.easou.spider.common.entity.HttpSnatchParam;
//import com.easou.spider.common.entity.Resource;
//import com.easou.spider.common.exception.HttpSnatcherException;
//import com.easou.spider.common.util.FileUtils;
//import com.easou.spider.common.util.HttpSnatcher;
import org.apache.http.impl.conn.DefaultClientConnectionOperator;
import org.apache.http.conn.scheme.SchemeRegistry;

public class HtmlExtractUtil {
	private static final Log log = LogFactory.getLog(HtmlExtractUtil.class);
	// <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	private static final Pattern metaCharsetPt = Pattern
			.compile("(?i)<meta (.*?)charset=(.*?)[\"']");
	// <?xml version="1.0" encoding="gb2312"?>
	private static final Pattern xmlEncodingPt = Pattern
			.compile("(?i)<?xml (.*?)encoding=[\"'](.*?)[\"']");
	// text/html; charset=utf-8
	private static final Pattern contentTypePt = Pattern
			.compile("(?i)text/html.*charset=(.*)");

	private static HtmlCharsetDetector detectCharset = new HtmlCharsetDetector();

	public static String extractHtml(String pageContent, String template,
			String outputEncoding) {
		TransformerFactory tFactory = SAXTransformerFactory.newInstance();
		StreamSource xslts = new StreamSource(IOUtils.toInputStream(template));
		try {
			pageContent = pageContent.replaceAll("\\<\\!\\-\\-", "").replaceAll("\\-\\-\\>",
					"");
			pageContent = pageContent.replaceAll("(?i)<br\\s*/?>", "\n");
			pageContent = HtmlUtils.htmlUnescape(pageContent);
			pageContent = pageContent.replaceFirst("(?i)<html.*?>", "<html>");
			pageContent = cleanHtml(pageContent);
			
			Transformer transformer = tFactory.newTransformer(xslts);
			StreamSource xmls = new StreamSource(IOUtils.toInputStream(pageContent));
			ByteArrayOutputStream outs = new ByteArrayOutputStream();
			transformer.transform(xmls, new StreamResult(outs));

			String xml = outs.toString(outputEncoding);
			outs.close();
			return xml;
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e, e);
		}
		return null;

	}

	public static String getContent(String url) throws IOException {
		return getContent(url, null);
	}

	public static String getContent(String url, Map<String, String> headers)
			throws IOException {

		URLConnection conn = null;
		InputStream in = null;
		String page = null;

		try {
			conn = (URLConnection) new URL(url).openConnection();
			conn.setConnectTimeout(180000);
			conn.setReadTimeout(180000);
			
			if (null != headers) {
				for (String hd : headers.keySet()) {
					if (!hd.equalsIgnoreCase("Accept-Encoding")) {
						conn.setRequestProperty(hd, headers.get(hd));
					}
				}
			} else {
				conn.setRequestProperty("User-Agent",
						"Mozilla/4.0 (compatible;  Windows NT 5.1)");
				conn.setRequestProperty("Accept", "*/*");
			}

			conn.setUseCaches(false);
			conn.connect();
			String contentEncoding = conn.getContentEncoding();
			String contentType = conn.getContentType();
			in = conn.getInputStream();

			if (contentEncoding != null
					&& -1 != contentEncoding.toLowerCase().indexOf("gzip")) { // 解压gzip流
				in = new GZIPInputStream(in);
			}
			byte[] buf = IOUtils.toByteArray(in);

			String encoding = getEncoding(contentType, buf);
			// log.debug("encoding : " + encoding);
			page = new String(buf, encoding);
			// log.debug("page : " + page);
			page = HtmlUtils.htmlUnescape(HtmlUtils.htmlUnescape(HtmlUtils
					.htmlEscapeDecimal(page)));
			in.close();
		} catch (MalformedURLException e) {
			return null;
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (IOException e) {
			throw e;
		} finally {
			if (conn != null && conn instanceof HttpURLConnection)
				((HttpURLConnection) conn).disconnect();
		}
		return page;
	}

	public static String getEncoding(String contentType, byte[] content) {
		String encode = null;
		try {
			if (contentType != null) {
				log.debug("contentType : " + contentType);
				Matcher mc = contentTypePt.matcher(contentType);
				if (mc.find()) {
					encode = mc.group(1).trim();
					log.debug("encode0 : " + encode);
				}
			}
			if (encode == null) {
				String htmlContent = new String(content);
				Matcher mc = metaCharsetPt.matcher(htmlContent);
				if (mc.find()) {
					encode = mc.group(2).trim();
					log.debug("encode1 : " + encode);
				} else { // 否则是<?xml version="1.0" encoding="gb2312"?>格式的
					mc = xmlEncodingPt.matcher(htmlContent);
					if (mc.find()) {
						encode = mc.group(2).trim();
					}
					log.debug("encode2 : " + encode);
				}
			}
			if (encode == null) {
				encode = detectCharset.detectCharset(content);
				log.debug("encode3 : " + encode);
			}
			if (encode.toLowerCase().indexOf("utf") == -1) {
				encode = "GB18030";
			} else {
				encode = "UTF-8";
			}

			log.debug("encode4 : " + encode);
		} catch (Exception e) {
			log.info(e, e);
			encode = "GB18030";
		}

		return encode;
	}

	public static boolean tryConnect(String url) {
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();

			conn.setConnectTimeout(60000);
			conn.setReadTimeout(60000);
			conn.setUseCaches(false);
			// conn.setFollowRedirects(false);//不重定向
			conn.setInstanceFollowRedirects(false);// 不重定向
			conn.connect();
			int status = conn.getResponseCode();
			// System.out.println(status);
			if (status >= 200 && status < 300) {
				return true;
			} else {
				log.info("TryConnect URL: " + url + " Status: " + status);
				return false;
			}
		} catch (Exception e) {
			log.info("TryConnect Exception! URL: " + url + "\n", e);

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return false;
	}

	public Map<String, String> parseHeaders(String headerStr) {
		if (null == headerStr || headerStr.equals("")) {
			return null;
		}
		int beginIndex = headerStr.indexOf("\r\t");
		int endIndex = headerStr.indexOf("\r\t\r\t");

		if (beginIndex > 0) {
			// 提取文件类型
			String hdstr;
			if (endIndex > beginIndex) {
				hdstr = headerStr.substring(beginIndex + 2, endIndex);
			} else {
				hdstr = headerStr.substring(beginIndex + 2);
			}
			// System.out.println("头： "+hdstr);
			// 解释头信息
			hdstr = hdstr.replace("\r\t", "\r");
			String[] hds = hdstr.split("\r");
			Map<String, String> headers = new HashMap<String, String>();
			for (int i = 0; i < hds.length; i++) {
				String[] nv = hds[i].split(":", 2);
				headers.put(nv[0].trim(), nv[1].trim());
			}
			return headers;
		} else
			return null;

	}

	/**
	 * 清理网页内容，返回规范的html内容
	 * 
	 * @param htmlPage
	 * @return
	 */
	public static String cleanHtml(String htmlPage) throws IOException {

		// take default cleaner properties
		CleanerProperties props = new CleanerProperties();

		// customize cleaner's behaviour with property setters
		props.setAllowHtmlInsideAttributes(true);
		props.setOmitComments(true);
		props.setOmitDoctypeDeclaration(true);
		// props.setOmitXmlDeclaration(true);
		// props.setTreatUnknownTagsAsContent(true);
		// props.setOmitDeprecatedTags(true);
		props.setPruneTags("script,link,base,style,meta");
		props.setAdvancedXmlEscape(true);

		// create an instance of HtmlCleaner
		HtmlCleaner cleaner = new HtmlCleaner(props);
		SimpleXmlSerializer simpleXmlSerializer = new SimpleXmlSerializer(props);
		TagNode tagNode = cleaner.clean(htmlPage);

		return simpleXmlSerializer.getXmlAsString(tagNode);
	}
//	private static String getPicture(String photoFile) {
//		String storePath = null;
//		HttpSnatchParam param = new HttpSnatchParam();
//		param.setUrl(photoFile);
//		// 构建存储路径
//		storePath = FileUtils.makeDir("f://")
//				+ FileUtils.makeFileName();
//		param.setPath(storePath);
//		param.setResourceExts(new HashSet<String>(Arrays
//				.asList(Constants.PICTURE_RESOURCE_TYPE_ALLOW)));
//		param.setMinLength(Constants.PICTURE_MIN_LENGTH_ALLOW);
//		param.setMaxLength(Constants.PICTURE_MAX_LENGTH_ALLOW);
//		// 下载
//		Resource resource = null;
//		try {
//			resource = HttpSnatcher.getResource(param);
//			if (resource == null
//					|| resource.getHttpStatus() != 200)
//				storePath = null;
//		} catch (HttpSnatcherException e) {
////			errorLog.error("catch picture fail url:"+photoFile,e);
//			storePath = null;
//		}
//		if (storePath != null)
//			storePath = resource.getFile().getPath();
//		return storePath;
//	}
	/**
	 * @param args
	 * @author Snail
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
//		String templateFile = "temp.txt";
//		InputStream in = ClassLoader.getSystemResourceAsStream(templateFile);
//		// new StringReader(tmp)
//		String template = IOUtils.toString(in, "utf8").trim();
//		byte[] buf = template.getBytes();
//		String temp = new String(buf, "utf8");
//		// in.close();
//		// System.out.println("\n\ntemplate: "+template);
//		String page = getContent("http://www.5ilrc.com/souge1.asp");
//		// System.out.println("\n\npage: "+page);
//		String xml = extractHtml(page, template, "utf8");
//		System.out.println(xml);
//		getPicture("http://www.114yin.com/up_pic/2011/12/31/201112311935048302.jpg");

	}

}
