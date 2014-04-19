package com.icloud.framework.core.config;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author liangwang
 
when you need parse such a config file

NOTE:  you only need the content in the CDATA node
 
<configuration>

	<item name="news_comment_count">
		<refer_url_patterns>
			<refer_url_pattern domain="sports.sina.com.cn">
				<![CDATA[/.*?/\d{4}-\d{2}-\d{2}/\d*.shtml]]>
			</refer_url_pattern>
		</refer_url_patterns>

		<intercetpor_url_patterns>
			<intercetpor_url_pattern>
				<![CDATA[http://comment\d\.news\.sohu\.com/static/cmt_topicp_all]]>
			</intercetpor_url_pattern>
		</intercetpor_url_patterns>

		<ieexpressions>
			<ieexpression>
				<![CDATA[{"allCount":[$number],]]>
			</ieexpression>
		</ieexpressions>
	</item>


you could use such code to achieve the purpose
XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		Document rootDocument = config.getDocument();

		NodeList items = rootDocument.getElementsByTagName("item");
		
for (int i = 0; i < items.getLength(); i++) {
			Element item = (Element) items.item(i);

			String itemName = item.getAttribute("name");
			
			String xpathStr ="//configuration/item[@name='" + itemName
					+ "']/ieexpressions/ieexpression";
			
			List<String> ieexpressionList = CDATAOnlyConfigItemCollector.parseTextList(rootDocument, xpathStr);
			
 
 */
public class CDATAOnlyConfigItemCollector {

	static private Logger logger = LoggerFactory
			.getLogger(CDATAOnlyConfigItemCollector.class);

	static public String parseText(Object rootDocument, String xpathStr) {

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		XPathExpression expr;
		try {
			expr = xpath.compile(xpathStr);

			Object result = expr.evaluate(rootDocument, XPathConstants.NODE);

			Node node = (Node) result;
			
			if(node==null) return null;
 
			NodeList contents = node.getChildNodes();

			for (int k = 0; k < contents.getLength(); k++) {
				// only include the cdata text
				if (contents.item(k).getNodeType() == Node.CDATA_SECTION_NODE) {
					return contents.item(k).getTextContent();
				}
			}

		} catch (XPathExpressionException e) {
			logger.error("XPathExpressionException {}", e.getLocalizedMessage());
		}
		return "";

	}
	
	
	static public NodeList getNodeList(Document rootDocument, String xpathStr){
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		XPathExpression expr;
		try {
			expr = xpath.compile(xpathStr);

			Object xresult = expr.evaluate(rootDocument, XPathConstants.NODESET);

			NodeList nodes = (NodeList) xresult;
			 
			return nodes; 
		} catch (XPathExpressionException e) {
			logger.error("XPathExpressionException {}", e.getLocalizedMessage());
		}
		return null;
	}
	

	static public List<String> parseTextList(Object rootDocument, String xpathStr) {

		List<String> result = new ArrayList<String>();
		
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		XPathExpression expr;
		try {
			expr = xpath.compile(xpathStr);

			Object xresult = expr.evaluate(rootDocument, XPathConstants.NODESET);

			NodeList nodes = (NodeList) xresult;
			for (int j = 0; j < nodes.getLength(); j++) {

				NodeList contents = nodes.item(j).getChildNodes();

				for (int k = 0; k < contents.getLength(); k++) {
					// only include the cdata text
					if (contents.item(k).getNodeType() == Node.CDATA_SECTION_NODE) {
						result.add(contents.item(k).getTextContent());
					}
				}

			}
		} catch (XPathExpressionException e) {
			logger.error("XPathExpressionException {}", e.getLocalizedMessage());
		}
		return result;

	}
}
