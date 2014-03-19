/**
 *
 */
package com.travelzen.framework.http.spider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;
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

import freemarker.cache.TemplateLoader;

/**
 * @author Administrator
 *
 */
public class XMLTemplateLoader implements TemplateLoader {

	static final Logger LOG = LoggerFactory.getLogger(RegConfUtils.class);

	public static Map<String, String> templateMap = new HashMap<String, String>();

	static {
		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory
					.newInstance();
			domFactory.setNamespaceAware(true); // never forget this!
			DocumentBuilder builder = domFactory.newDocumentBuilder();

			InputStream resourceAsStream = RegConfUtils.class
					.getResourceAsStream("/com/baidu/bidder/conf/querystr_template.xml");

			if (null == resourceAsStream) {
				LOG.error("invalid reg conf file!--"
						+ "/com/baidu/bidder/conf/querystr_template.xml");
			}

			Document doc = builder.parse(resourceAsStream);

			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile("//item");

			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				String regexp = nodes.item(i).getChildNodes().item(0)
						.getNodeValue();

				String name = nodes.item(i).getAttributes()
						.getNamedItem("name").getNodeValue();

				templateMap.put(name, regexp);

			}

			// resourceAsStream = RegConfUtils.class
			// .getResourceAsStream("patch.xml");

			File dir = new File("patchs");
			String[] aryFile = dir.list();

			Arrays.sort(aryFile);

			for (String file : aryFile) {

				String[] names = file.split("-");
				if (names.length != 3)
					continue;

				if (!StringUtils.equals("tplpatch", names[0]))
					continue;


				if (!StringUtils
						.equals(names[1], FrameworkSpiderConstant.VERSION_NUMBER))
					continue;

				doc = builder.parse(new File("patchs/" + file));

				factory = XPathFactory.newInstance();
				xpath = factory.newXPath();
				expr = xpath.compile("//item");

				result = expr.evaluate(doc, XPathConstants.NODESET);
				nodes = (NodeList) result;
				for (int i = 0; i < nodes.getLength(); i++) {
					String regexp = nodes.item(i).getChildNodes().item(0)
							.getNodeValue();

					String name = nodes.item(i).getAttributes().getNamedItem(
							"name").getNodeValue();

					templateMap.put(name, regexp);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see freemarker.cache.TemplateLoader#closeTemplateSource(java.lang.Object)
	 */
	@Override
	public void closeTemplateSource(Object arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see freemarker.cache.TemplateLoader#findTemplateSource(java.lang.String)
	 */
	@Override
	public Object findTemplateSource(String arg0) throws IOException {

		templateMap.get(arg0);

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see freemarker.cache.TemplateLoader#getLastModified(java.lang.Object)
	 */
	@Override
	public long getLastModified(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see freemarker.cache.TemplateLoader#getReader(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public Reader getReader(Object arg0, String arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
