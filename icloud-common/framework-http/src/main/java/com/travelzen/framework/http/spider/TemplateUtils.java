package com.travelzen.framework.http.spider;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TemplateUtils {

	static final Logger LOG = LoggerFactory.getLogger(TemplateUtils.class);

	private static Configuration freemarker_cfg = null;

	public static Map<String, String> templateMap = new HashMap<String, String>();

	static {
		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory
					.newInstance();
			domFactory.setNamespaceAware(true); // never forget this!
			DocumentBuilder builder = domFactory.newDocumentBuilder();

			InputStream resourceAsStream = RegConfUtils.class
					.getResourceAsStream("/customize/reg.xml");

			if (null == resourceAsStream) {
				LOG.error("invalid reg conf file!--"
						+ "/customize/reg.xml");
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取freemarker的配置. freemarker本身支持classpath,目录和从ServletContext获取.
	 */
	protected static Configuration getFreeMarkerCFG() {
		if (null == freemarker_cfg) {
			// Initialize the FreeMarker configuration;
			// - Create a configuration instance
			freemarker_cfg = new Configuration();
			
			freemarker_cfg.setDefaultEncoding("gbk");

			StringTemplateLoader stringLoader = new StringTemplateLoader();

			for (String name : templateMap.keySet()) {
				stringLoader.putTemplate(name, templateMap.get(name));
				;
			}

			freemarker_cfg.setTemplateLoader(stringLoader);
		}

		return freemarker_cfg;
	}

	public static String getTemplateValue(Map<?, ?> propMap, String templateName) {

		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		String res = "";

		try {
			Template t = getFreeMarkerCFG().getTemplate(templateName);
			t.setEncoding("gbk");

			bo = new ByteArrayOutputStream();

			Writer out = new BufferedWriter(new OutputStreamWriter(bo));

			t.process(propMap, out);

			res = bo.toString("gbk");
		} catch (TemplateException e) {
			LOG.error("Error while processing FreeMarker template "
					+ templateName, e);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res;
	}

}
