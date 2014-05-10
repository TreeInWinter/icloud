package com.icloud.stock.importer.meta;

import com.icloud.framework.file.TextFile;

public class importSecondXueqiuMenu {
	public void importCategory() {
		String filePath = "/home/jiangningcui/workspace/mygithub/icloud/icloud-data/xueqiu/fetch/h.html";
		TextFile textFile = new TextFile(filePath);
		StringBuffer sb = new StringBuffer();
		for (String str : textFile) {
			sb.append(str);
		}
		String text = sb.toString();
		String[] split = text.split("href=");
		for (String s : split) {
			getTitle(s);
		}
		System.out.println("-----------");
	}

	public String getTitle(String s) {
		if (s.indexOf("title") != -1) {
			String titleStr = s.substring(s.indexOf("title=") + 7);
			String title = titleStr.substring(0, titleStr.indexOf("\""));
			System.out.print(title + " ");

			String codeStr = s.substring(s.indexOf("data-level2code=") + 17);
			String code = codeStr.substring(0, codeStr.indexOf("\""));
			System.out.println(code);
		}
		return null;
	}

	public static void main(String[] args) {
		importSecondXueqiuMenu deleteData = new importSecondXueqiuMenu();
		deleteData.importCategory();
	}
}
