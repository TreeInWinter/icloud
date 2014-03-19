package com.travelzen.framework.http.irsession;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author liangwang
 * 
 *         use freemarker to generate file
 * 
 */
public class HttpSessionTemplate {

	private static class SIngletonHolder {
		static final HttpSessionTemplate INSTANCE = new HttpSessionTemplate();
	}

	public static HttpSessionTemplate getInstance() {
		return SIngletonHolder.INSTANCE;
	}

	static final Logger logger = LoggerFactory
			.getLogger(HttpSessionTemplate.class);

	final static String DEFAULT_ENCODING = "UTF-8";

//	private Map<String, Configuration> freemarkerCfgMap = new HashMap<String, Configuration>();

	HttpIRSessionConf sessionConf;

	Map<HttpIRSessionConf.IrRequest , Configuration> freeMarkerCfgCache = new HashMap<HttpIRSessionConf.IrRequest , Configuration>();

	public Configuration getFreeMarkerCFG(HttpIRSessionConf.IrRequest request) {

		if (freeMarkerCfgCache.containsKey(request))
			return freeMarkerCfgCache.get(request);

		Configuration freemarkerCfg = new Configuration();

		freemarkerCfg.setDefaultEncoding(DEFAULT_ENCODING);

		StringTemplateLoader stringLoader = new StringTemplateLoader();

		stringLoader.putTemplate("url", request.uri);
		stringLoader.putTemplate("body", request.body);
		stringLoader.putTemplate("head", request.head);

		freemarkerCfg.setTemplateLoader(stringLoader);

		freeMarkerCfgCache.put(request, freemarkerCfg);

		return freemarkerCfg;
	}
	
	
	

	public String getTemplateValue(Map<?, ?> propMap, Configuration conf,
			String templateName) {

		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		String res = "";

		try {
			Template t = conf.getTemplate(templateName);
			t.setEncoding(DEFAULT_ENCODING);

			bo = new ByteArrayOutputStream();

			Writer out = new BufferedWriter(new OutputStreamWriter(bo));

			t.process(propMap, out);

			res = bo.toString(DEFAULT_ENCODING);
		} catch (TemplateException e) {
			logger.error("Error while processing FreeMarker template "
					+ templateName, e);
		} catch (IOException e) {
			logger.error("IOException:{}", e.getLocalizedMessage());
		} catch (Throwable e) {
			logger.error("Throwable:{}", e.getLocalizedMessage());
		}

		return res;
	}

}
