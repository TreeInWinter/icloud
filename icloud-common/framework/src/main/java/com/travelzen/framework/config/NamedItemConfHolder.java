package com.travelzen.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liangwang
 * 
 * read name-item pair from xml configure file
 *
 */
public class NamedItemConfHolder {

	static final Logger LOG = LoggerFactory.getLogger(NamedItemConfHolder.class);

	public Map<String, String> namedItemMap = new HashMap<String, String>();

	public static Map<String, NamedItemConfHolder> cachedNamedItemConfUtil = new HashMap<String, NamedItemConfHolder>();

	public static NamedItemConfHolder getInstance(String path) {

		if (cachedNamedItemConfUtil.containsKey(path)) {
			return cachedNamedItemConfUtil.get(path);
		}

		NamedItemConfHolder regConfUtil = new NamedItemConfHolder();

		SAXBuilder builder = new SAXBuilder(XMLReaders.NONVALIDATING);
		Document doc;

		InputStream resourceAsStream = NamedItemConfHolder.class
				.getResourceAsStream(path);

		InputStreamReader reader;
		try {
			reader = new InputStreamReader(resourceAsStream, "UTF-8");
			doc = builder.build(reader);

			Element root = doc.getRootElement();

			List<Element> configurationlist = (List<Element>) root
					.getChildren("item");

			for (Element item : configurationlist) {

				String name = item.getAttribute("name").getValue();

				regConfUtil.namedItemMap.put(name, item.getTextTrim());
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		cachedNamedItemConfUtil.put(path, regConfUtil);

		return regConfUtil;
	}

	public String getItem(String name) {
		return namedItemMap.get(name);
	}

	public static void main(String[] args) {

	}

}
