package com.icloud.stock.http;

import java.io.IOException;
import java.net.MalformedURLException;

import org.slf4j.Logger;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.icloud.framework.logger.ri.RequestIdentityLogger;

public class HtmlUnit {
	protected static final Logger LOGGER = RequestIdentityLogger
			.getLogger(HtmlUnit.class);
	private WebClient webClient = null;

	public HtmlUnit() {
		webClient = new WebClient(BrowserVersion.FIREFOX_3);
		webClient.setJavaScriptEnabled(true);
		webClient.setCssEnabled(false);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.setTimeout(35000);
		webClient.setThrowExceptionOnScriptError(false);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "http://hotel.elong.com/search/list_cn_0201.html?IsNotAcceptRecommend=false&Keywords=7%E5%A4%A9%E8%BF%9E%E9%94%81%E9%85%92%E5%BA%97%20%EF%BC%88%E4%B8%8A%E6%B5%B7%E8%99%B9%E6%A1%A5%E5%BA%97%EF%BC%89&KeywordsType=999&aioIndex=-1&aioVal=7%E5%A4%A9%E8%BF%9E%E9%94%81%E9%85%92%E5%BA%97%20%EF%BC%88%E4%B8%8A%E6%B5%B7%E8%99%B9%E6%A1%A5%E5%BA%97%EF%BC%89";
		HtmlUnit htmlUnit = new HtmlUnit();
		// try {
		// htmlUnit.homePage_Firefox(url);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		try {
			System.out.println(htmlUnit.asText(url));
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Page getHtmlPage(String url) throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		LOGGER.info("fetching {}", url);
		Page page = webClient.getPage(url);
		webClient.closeAllWindows();
		LOGGER.info("fetched {}", url);
		return page;
	}

	public String getContentAsString(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		Page page = getHtmlPage(url);
		return page.getWebResponse().getContentAsString();
	}

	public String asText(String url) throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		HtmlPage page = (HtmlPage) getHtmlPage(url);
		return page.asText();
	}
}
