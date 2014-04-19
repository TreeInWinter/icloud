package com.icloud.framework.core.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import com.icloud.framework.core.util.TZUtil;


public class DomUtil {

	protected static final Logger logger = LoggerFactory
			.getLogger(DomUtil.class);

	private static DocumentBuilderFactory dbf = DocumentBuilderFactory
			.newInstance();

	private static DocumentBuilder db;
	private static DOMImplementation domImpl;

	static {

		try {
			db = dbf.newDocumentBuilder();
			domImpl = db.getDOMImplementation();
		} catch (ParserConfigurationException e) {
			logger.error("err{}",TZUtil.stringifyException(e));
		}

	}

	private static List<Pattern> headList = new ArrayList<Pattern>();

	static {
		headList.add(Pattern.compile("<?xml ?=\"?\" version=\"1.0\">",
				Pattern.LITERAL | Pattern.CASE_INSENSITIVE));
		headList.add(Pattern.compile(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>", Pattern.LITERAL
						| Pattern.CASE_INSENSITIVE));
		headList.add(Pattern.compile(
				"<?xml ?=\"?\" encoding=\"utf-8\" version=\"1.0\">",
				Pattern.LITERAL | Pattern.CASE_INSENSITIVE));

	}

	// final Pattern head = Pattern.compile(
	// "<?xml ?=\"?\" encoding=\"utf-8\" version=\"1.0\">",
	// Pattern.LITERAL);
	//
	// final Pattern head2 = Pattern.compile(
	// "<?xml ?=\"?\" version=\"1.0\">",
	// Pattern.LITERAL);

	private static final Pattern tail = Pattern.compile("</?xml>", Pattern.LITERAL);

	private static Document makeDocumentByNode(Node doc) {
		Document document = null;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			document.appendChild(doc);
		} catch (ParserConfigurationException e) {
			logger.error("err{}",TZUtil.stringifyException(e));
		}
		return document;
	}

	// public void testXmlDocumentWithNamespaces() throws Exception {
	// DocumentBuilder db = dbf.newDocumentBuilder();
	// DOMImplementation domImpl = db.getDOMImplementation();
	// Document document = buildExampleDocumentWithNamespaces(domImpl);
	// serialize(domImpl, document);
	// }

	// private Document buildExampleDocumentWithNamespaces(
	// DOMImplementation domImpl) {
	// Document document = domImpl.createDocument("urn:example.namespace",
	// "my:example", null);
	// Element element = document.createElementNS("http://another.namespace",
	// "element");
	// document.getDocumentElement().appendChild(element);
	// return document;
	// }

	public static String serialize(Document document) {
		DOMImplementationLS ls = (DOMImplementationLS) domImpl;
		LSSerializer lss = ls.createLSSerializer();
		return lss.writeToString(document);
	}

	// 获取dom的全文本内容（wap格式测试过）

	public static String getContent(Document doc) {

		if (null == doc) {
			logger.error("doc is null");
		}

		String xml = "";

		try {

			Element root = doc.getDocumentElement();

			if (null == root)
				return "";

			// 有的页面的根节点不是html节点，这时需要取到html节点
			// (不考虑有多个html节点的情况)
			if (!StringUtils.equals(root.getNodeName(), "html")) {
				if (null != (Element) (doc.getElementsByTagName("html"))
						.item(0)) {
					root = (Element) (doc.getElementsByTagName("html")).item(0);
				}
			}

			// 有的页面会漏掉html标签，这里需要重构页面
			if (root.getNodeName().equalsIgnoreCase("head")) {
				Element htmlnode = doc.createElement("html");
				NodeList nodes = doc.getChildNodes();
				for (int i = 0, n = nodes.getLength(); i < n; i++) {
					htmlnode.appendChild(nodes.item(i));
				}

				root = htmlnode;

			}

			return serialize(makeDocumentByNode(root));

		} catch (Exception e) {
			logger.error("err{}",TZUtil.stringifyException(e));
		}  

		for (Pattern head : headList) {
			xml = head.matcher(xml).replaceFirst("");
		}

		xml = tail.matcher(xml).replaceFirst("");

		return xml;

	}

}
