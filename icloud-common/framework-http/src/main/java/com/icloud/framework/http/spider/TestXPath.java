package com.icloud.framework.http.spider;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class TestXPath {

 

	public static void main(String[] args) {

		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory
					.newInstance();
			domFactory.setNamespaceAware(true); // never forget this!
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			
			
			InputStream resourceAsStream = TestXPath.class.getResourceAsStream("com/baidu/bidder/conf/reg.xml");
			Document doc = builder.parse(resourceAsStream);

			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath
					.compile("//item");

			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				System.out.println(nodes.item(i).getChildNodes().item(0).getNodeValue());
				
				System.out.println(nodes.item(i).getAttributes().getNamedItem("name").getNodeValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
