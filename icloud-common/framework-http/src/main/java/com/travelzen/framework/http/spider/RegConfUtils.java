package com.travelzen.framework.http.spider;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class RegConfUtils {

	static final Logger LOG = LoggerFactory.getLogger(RegConfUtils.class);

	public static Map<String, String> regexpMap = new HashMap<String, String>();

	static {
		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory
					.newInstance();
			domFactory.setNamespaceAware(true); // never forget this!
			DocumentBuilder builder = domFactory.newDocumentBuilder();

			InputStream resourceAsStream = RegConfUtils.class
					.getResourceAsStream("/customize/reg.xml");

			if (null == resourceAsStream) {
				LOG.error("invalid reg conf file!--" + "/customize/reg.xml");
			}

			Document doc = builder.parse(resourceAsStream);

			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile("//item");

			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {

				int size = nodes.item(i).getChildNodes().getLength();

				FIND_NOT_EMPTY: for (int j = 0; j < size; j++) {

					String regexp = nodes.item(i).getChildNodes().item(j)
							.getNodeValue();

					if (StringUtils.isNotBlank(regexp)) {
						String name = nodes.item(i).getAttributes()
								.getNamedItem("name").getNodeValue();

						regexpMap.put(name, regexp);
						break FIND_NOT_EMPTY;
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

	}

}
