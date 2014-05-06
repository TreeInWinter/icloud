package com.icloud.stock.connector.handler.impl;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.util.StopWatch;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.icloud.framework.net.http.HttpUtils;
import com.icloud.stock.connector.handler.StockHandler;
import com.icloud.stock.connector.model.XueqiuMetaInfo;
import com.icloud.stock.connector.parser.Parser;
import com.icloud.stock.http.HtmlUnit;

public class XueqiuMetaInfoHandler extends StockHandler<XueqiuMetaInfo> {

	private String typeName;

	public XueqiuMetaInfoHandler(String typeName) throws NoSuchFieldException,
			SecurityException, NoSuchMethodException {
		super(null, null, true);
		this.typeName = typeName;
	}

	@Override
	public void apply() {
		// TODO Auto-generated method stub

	}

	public  String getHangyeUrl(String hangye, int page, int size) {
		String url = "http://xueqiu.com/industry/quote_order.json?page="
				+ page
				+ "&size="
				+ size
				+ "&order=desc&orderBy=percent&level2code=G59&exchange=CN&plate="
				+ HttpUtils.encodingGB2312(hangye)
				+ "&access_token=CSr4RehgUt3wohdtqUTp9E&_=1399368774171";
		return url;
	}

	// public static void main(String[] args) {
	// String name = "仓储业";
	// System.out.println(getHangyeUrl(name, 1, 30));
	// }

	public XueqiuMetaInfo getHttpData() {
		HtmlUnit unit = new HtmlUnit();
		String url = getHangyeUrl(typeName, 1, 60);
		Parser<XueqiuMetaInfo> parser = (Parser<XueqiuMetaInfo>) factory
				.getParser(domainClass);

		StopWatch stopwatch = new StopWatch(domainClass.getName());
		stopwatch.start("调用http接口");
		String content = null;

		XueqiuMetaInfo result = null;
		try {
			result = parser.parse(unit.getContentAsString(url), null); // 获得第一个结果
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

		if (result != null) {
			int count = result.getCount();
			if (count > 60) { // 如果count大于60的时候,计算总共页码数目
				int ceil = (int) Math.ceil(count / 60.0);
				for (int i = 2; i <= ceil; i++) {
					url = getHangyeUrl(typeName, i, 60);
					try {
						XueqiuMetaInfo tmpResult = parser.parse(
								unit.asText(url), null); // 获得结果
						result.getData().addAll(tmpResult.getData());
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
			}
		}
		stopwatch.stop();
		LOGGER.info("{}:{}", stopwatch.getLastTaskName(), stopwatch.toString());
		return result;
	}

}
