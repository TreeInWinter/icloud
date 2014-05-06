package com.icloud.stock.importer.meta;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.icloud.framework.file.TextFile;
import com.icloud.framework.util.LogFileWriter;
import com.icloud.stock.connector.model.XueqiuMetaInfo;
import com.icloud.stock.connector.model.XueqiuMetaInfo.Stock;

public class XueQiuBaseCateImporter {

	public XueQiuBaseCateImporter() {

	}

	public void importHangye(String readerPath, LogFileWriter writer,
			String cateName) {
		List<String> list = getAllHangye(readerPath);
		String content = list.get(0);
		parse(content, cateName, writer);
	}

	private void parse(String content, String cateName, LogFileWriter writer) {
		char ch = ' ';
		String dataStr = "\"data\":";
		content = content
				.substring(content.indexOf(dataStr) + dataStr.length());
		content = content.replace("[[", "[");
		String[] contents = content.split("\\],\\[");
		for (String con : contents) {
			con = con.replace("[", "").replace("\"", "").replace("}", "");
			String[] params = con.split(",");
			writer.apendln(params[0] + ch + params[1] + ch + cateName);
		}
	}

	public List<String> getAllHangye(String filePath) {
		List<String> list = new ArrayList<String>();
		TextFile textFile = new TextFile(filePath);
		for (String text : textFile) {
			list.add(text.trim());
		}
		return list;
	}

	//
	public static void main(String[] args) throws UnsupportedEncodingException {
		String basePath = "/home/jiangningcui/workspace/mygithub/icloud/icloud-data/xueqiu/fetch/";
		String processPath = "/home/jiangningcui/workspace/mygithub/icloud/icloud-data/xueqiu/processed/jichufenlei.txt";

		String[] name = { "chuangyeban.txt", "hushiA.txt", "hushiB.txt",
				"shenzhenA.txt", "shenzhenB.txt", "zhongxiao.txt" };
		String[] cateName = { "创业板", "沪市A股", "沪市B股", "深市A股", "深市B股", "中小企业" };

		String writerPath = processPath;
		LogFileWriter writer = new LogFileWriter(writerPath);
		XueQiuBaseCateImporter importer = new XueQiuBaseCateImporter();
		for (int i = 0; i < name.length; i++) {
			String readerPath = basePath + name[i];
			importer.importHangye(readerPath, writer, cateName[i]);
		}
		writer.close();
	}
}
