/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.icloud.framework.http.domain;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.icloud.framework.core.util.TZUtil;

/**
 * Storage class for <code>DomainSuffix</code> objects Note: this class is
 * singleton
 * 
 * @author Enis Soztutar &lt;enis.soz.nutch@gmail.com&gt;
 */
public class DomainSuffixes {
	private static final Log LOG = LogFactory.getLog(DomainSuffixes.class);

	private HashMap<String, DomainSuffix> domains = new HashMap<String, DomainSuffix>();

	private static DomainSuffixes instance;

	/** private ctor */
	private DomainSuffixes() {
 		String file = "domain-suffixes.xml";
		
		
 		InputStream input = this.getClass().getClassLoader()
 				.getResourceAsStream(file);
//
//		if (null == input) {
//			try {
//				input = new FileInputStream("conf/domain-suffixes.xml");
//			} catch (FileNotFoundException e) {
//				LOG.error(e);
//			}
//		}

		if (null == input) {
			 URL url = getClass().getClassLoader().getResource("http/domain-suffixes.xml");//<--①   
			 try {
				input =  url.openStream() ;
			} catch (IOException e) {
				e.printStackTrace();
			}
//			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//			// 只加载一个绝对匹配Resource，且通过ResourceLoader.getResource进行加载
//			try {
//				Resource[] resources = resolver.getResources("classpath*:com/travelzen/framework/http/domain/*.xml");
//				
//				
//				if(resources!=null &&  resources.length>0){
//					input = resources[0].getInputStream();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

		}

		try {
			new DomainSuffixesReader().read(this, input);
		} catch (Exception ex) {
			LOG.warn(TZUtil.stringifyException(ex));
		}
	}

	/**
	 * Singleton instance, lazy instantination
	 * 
	 * @return
	 */
	public static DomainSuffixes getInstance() {
		if (instance == null) {
			instance = new DomainSuffixes();
		}
		return instance;
	}

	void addDomainSuffix(DomainSuffix tld) {
		domains.put(tld.getDomain(), tld);
	}

	/** return whether the extension is a registered domain entry */
	public boolean isDomainSuffix(String extension) {
		return domains.containsKey(extension);
	}

	/**
	 * Return the {@link DomainSuffix} object for the extension, if extension is
	 * a top level domain returned object will be an instance of
	 * {@link TopLevelDomain}
	 * 
	 * @param extension
	 *            of the domain
	 */
	public DomainSuffix get(String extension) {
		return domains.get(extension);
	}

}
