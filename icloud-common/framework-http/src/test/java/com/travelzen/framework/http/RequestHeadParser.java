package com.travelzen.framework.http;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationListener;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.InMemoryDnsResolver;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.springframework.core.io.ClassPathResource;

import com.travelzen.framework.http.irsession.HttpIRSessionConf;

public class RequestHeadParser {

	HttpIRSessionConf httpSessionConf = new HttpIRSessionConf();
	XMLConfiguration config;
	AbstractHttpClient httpClient;

	public void initHttpSession(String irsessionConfPath) throws Exception {

		ClassPathResource resource = new ClassPathResource(irsessionConfPath);

		config = new XMLConfiguration();
		config.setFileName(resource.getFile().getAbsolutePath());
		config.setDelimiterParsingDisabled(true);
		config.setExpressionEngine(new XPathExpressionEngine());

		config.load();

		config.setReloadingStrategy(new FileChangedReloadingStrategy());

		httpSessionConf.parse(config);

		ConfigurationListener listener = new ConfigurationListener() {
			@Override
			public void configurationChanged(ConfigurationEvent event) {
				httpSessionConf.parse(config);
			}

		};
		config.addConfigurationListener(listener);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
				.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory
				.getSocketFactory()));

		PoolingClientConnectionManager cm = new PoolingClientConnectionManager(
				schemeRegistry, new InMemoryDnsResolver());
		// Increase max total connection to 200
		cm.setMaxTotal(200);

		cm.setDefaultMaxPerRoute(20);

		httpClient = new DefaultHttpClient(cm);
	}

	public static void main(String argv[]) {
		RequestHeadParser parser = new RequestHeadParser();
		try {
			parser.initHttpSession("irsession/163yuedu-session.xml");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
