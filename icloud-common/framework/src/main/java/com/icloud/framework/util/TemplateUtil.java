package com.icloud.framework.util;

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author liangwang
 * 
 * use freemarker to generate file
 *
 */
public class TemplateUtil {

	static final Logger LOG = LoggerFactory.getLogger(TemplateUtil.class);

	final static String DEFAULT_ENCODING = "UTF-8";

	private static Configuration freemarker_cfg = null;

	private static Map<String, String> templateMap = new HashMap<String, String>();

	static public void loadTemplate(String path) {

		ApplicationContext ctx = new ClassPathXmlApplicationContext();
		Resource[] resources;
		
		try {
			resources = ctx.getResources("classpath*:" + path);
			for (Resource res : resources) {

				try {
					DocumentBuilderFactory domFactory = DocumentBuilderFactory
							.newInstance();
					domFactory.setNamespaceAware(true); // never forget this!
					DocumentBuilder builder = domFactory.newDocumentBuilder();

					InputStream resourceAsStream = res.getInputStream();

					if (null == resourceAsStream) {
						LOG.error("invalid reg conf file:{}", res.toString());
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

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * 获取freemarker的配置. freemarker本身支持classpath,目录和从ServletContext获取.
	 */
	static protected  Configuration getFreeMarkerCFG() {
		if (null == freemarker_cfg) {
			// Initialize the FreeMarker configuration;
			// - Create a configuration instance
			freemarker_cfg = new Configuration();

			freemarker_cfg.setDefaultEncoding(DEFAULT_ENCODING);

			StringTemplateLoader stringLoader = new StringTemplateLoader();

			for (String name : templateMap.keySet()) {
				stringLoader.putTemplate(name, templateMap.get(name));
				;
			}

			freemarker_cfg.setTemplateLoader(stringLoader);
		}

		return freemarker_cfg;
	}

	static public  String getTemplateValue(Map<?, ?> propMap, String templateName) {

		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		String res = "";

		try {
			Template t = getFreeMarkerCFG().getTemplate(templateName);
			t.setEncoding(DEFAULT_ENCODING);

			bo = new ByteArrayOutputStream();

			Writer out = new BufferedWriter(new OutputStreamWriter(bo));

			t.process(propMap, out);

			res = bo.toString(DEFAULT_ENCODING);
		} catch (TemplateException e) {
			LOG.error("Error while processing FreeMarker template "
					+ templateName, e);
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Throwable e) {
			e.printStackTrace();
		}

		return res;
	}

}
