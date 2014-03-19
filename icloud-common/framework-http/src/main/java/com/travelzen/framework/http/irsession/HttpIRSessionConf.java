package com.travelzen.framework.http.irsession;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.XMLConfiguration;
import org.eclipse.jetty.io.bio.StringEndPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.travelzen.framework.core.config.CDATAOnlyConfigItemCollector;
import com.travelzen.framework.core.util.TZUtil;
import com.travelzen.framework.http.HttpURIUtil;

public class HttpIRSessionConf {

	private static Logger logger = LoggerFactory
			.getLogger(HttpIRSessionConf.class);

	public static class IrSession {
		public List<IrRequest> requests;
	}

	public static class IrRequest {
		public String uri;
		public String head;
		public String body;
		public List<String> iees;

		public HttpServletRequest request;
	}

	Map<String, IrSession> namesessionMap = new HashMap<String, IrSession>();

	//
	// <session name="yuedu.163.com">
	//
	// <request>
	//
	// <url>
	// <![CDATA[http://yuedu.163.com/source.do?operation=querySourceComments&id=e4884d70-b43d-4403-9aa7-fdd883ec2dbf_1&page=1]]>
	// </url>
	//
	// <head>
	// <![CDATA[GET
	// /source.do?operation=querySourceComments&id=e4884d70-b43d-4403-9aa7-fdd883ec2dbf_1&page=1
	// HTTP/1.1
	// Host: yuedu.163.com
	// User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:12.0)
	// Gecko/20100101 Firefox/12.0
	// Accept: text/html, */*; q=0.01
	// Accept-Language: zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3
	// Accept-Encoding: gzip, deflate
	// Connection: keep-alive
	// X-Requested-With: XMLHttpRequest
	// Referer:
	// http://yuedu.163.com/source/e4884d70-b43d-4403-9aa7-fdd883ec2dbf_1
	// Cookie: NTESYUEDUSI=17E146428453924A086C6F1436D6DDC7.yuedu4-8011;
	// __utma=63080999.1050185229.1344927303.1344927303.1344927303.1;
	// __utmb=63080999.2.10.1344927303; __utmc=63080999;
	// __utmz=63080999.1344927303.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)
	// Cache-Control: max-age=0
	//
	//
	// ]]>
	// </head>
	//
	// <body>
	// <![CDATA[]]>
	// </body>
	//
	// <iees>
	// <iee name="pageNum"><![CDATA[共 [$pageNum] 页显示]]></iee>
	// </iees>
	//
	// </request>
	//
	//
	// </session>
/*
	private String getSessionConfText(Object rootDocument, String itemName,
			String attr) {

		// String xpathStr = "//configuration/session[@name='" + itemName
		// + "']/request/"+attr;
		String xpathStr = "attr";

		return CDATAOnlyConfigItemCollector.parseText(rootDocument, xpathStr);

	}

	private List<String> getSessionIeesConfTextList(Document rootDocument,
			String itemName, String attr) {

		String xpathStr = "//configuration/session[@name='" + itemName
				+ "']/request/" + attr;

		return CDATAOnlyConfigItemCollector.parseTextList(rootDocument,
				xpathStr);

	}
*/
	public static org.eclipse.jetty.server.Request getRequestByURLHead(
			String urlStr, String head) {
		StringEndPoint endpoint = new StringEndPoint();
		endpoint.setInput(head);

		HttpConnection connection = new HttpConnection(endpoint);

		try {
			connection.handle();

			org.eclipse.jetty.server.Request request = connection.getRequest();

			URI uri = null;
			try {
				uri = HttpURIUtil.getURI(urlStr);
				
				request.setQueryString(uri.getQuery());
				request.setRequestURI(uri.toString());
				request.setProtocol(uri.getScheme());
				request.setRemoteHost(uri.getHost());
				request.setContextPath(uri.getPath());
				request.setPathInfo(uri.getPath());
				request.setServerPort(uri.getPort());
				
			} catch (Exception e) {
				logger.debug("invalid urlStr{}", urlStr);
			}

			// request.setServletPath(url.get)

			// request.getConnection().getRequestFields();
			// request.getCookies();
			//
			// request.getHeaderNames();

			return request;
		} catch (IOException e) {
			logger.error("processHead:{}", TZUtil.stringifyException(e));
		}
		return null;

	}

	public void parse(XMLConfiguration config) {

//		Map<String, List<String>> itemName2IEEListMap = new HashMap<String, List<String>>();

//		XPathFactory factory = XPathFactory.newInstance();
//		XPath xpath = factory.newXPath();

		Document rootDocument = config.getDocument();

		NodeList items = rootDocument.getElementsByTagName("session");

		for (int i = 0; i < items.getLength(); i++) {
			Element item = (Element) items.item(i);

			String itemName = item.getAttribute("name");

			NodeList requests = CDATAOnlyConfigItemCollector.getNodeList(
					rootDocument, "//configuration/session[@name='" + itemName
							+ "']/request");

			List<IrRequest> requestList = new ArrayList<IrRequest>();

			for (int j = 0; j < requests.getLength(); j++) {

				Object requestDocument = requests.item(j);

				String url = CDATAOnlyConfigItemCollector.parseText(
						requestDocument, "url");

				String head = CDATAOnlyConfigItemCollector.parseText(
						requestDocument, "head");

				String body = CDATAOnlyConfigItemCollector.parseText(
						requestDocument, "body");

				List<String> iees = CDATAOnlyConfigItemCollector.parseTextList(
						requestDocument, "iees/iee");

				IrRequest irrequest = new IrRequest();
				irrequest.body = body;
				irrequest.uri = url;
				irrequest.head = head;
				irrequest.iees = iees;

				requestList.add(irrequest);

				org.eclipse.jetty.server.Request req = getRequestByURLHead(url,
						head);
				irrequest.request = req;

			}

			IrSession session = new IrSession();
			session.requests = requestList;

			namesessionMap.put(itemName, session);

		}

	}

	public Map<String, IrSession> getNamesessionMap() {
		return namesessionMap;
	}

}
