package com.icloud.stock.connector.parser.impl;

import com.icloud.stock.connector.model.XueqiuMetaInfo;
import com.icloud.stock.connector.parser.Parser;

public class XueqiuMetaInfoPaser implements Parser<XueqiuMetaInfo> {

	@Override
	public XueqiuMetaInfo parse(String content, Object... params) {
		System.out.println(content);
		return null;
	}

	public static void main(String[] args) {
		XueqiuMetaInfoPaser paser = new XueqiuMetaInfoPaser();

	}
}
