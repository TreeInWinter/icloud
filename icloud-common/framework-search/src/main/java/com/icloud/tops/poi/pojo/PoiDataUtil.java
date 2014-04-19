package com.icloud.tops.poi.pojo;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class PoiDataUtil {
	public static void writeSheetWithRow(Sheet sheet, int rowNum, Map<Integer, CellValue> map) {
		Row row = sheet.createRow(rowNum);
		float maxHeight = 0.0f;

		for (Entry<Integer, CellValue> entry : map.entrySet()) {
			entry.getValue().setCell(row.createCell(entry.getKey()));
			float height = entry.getValue().getHeight();
			maxHeight = maxHeight < height ? height : maxHeight;
		}
		if (maxHeight > 0.0f) {
			row.setHeight((short) maxHeight);
		}
	}

	public static void addCellValue(Map<Integer, CellValue> map, int col, String value, HSSFCellStyle style) {
		if (map != null) {
			CellValue cell = new CellValue(value, style);
			map.put(col, cell);
		}
	}

	public static float getExcelCellAutoHeight(String str, float fontCountInline) {
		float defaultRowHeight = 200.00f;// 每一行的高度指定
		if (str == null)
			return defaultRowHeight;
		float factor = str.length() / 9.00f;
		return defaultRowHeight + defaultRowHeight * factor;
	}

	public static float getregex(String charStr) {

		if (charStr == " ") {
			return 0.5f;
		}
		// 判断是否为字母或字符
		if (Pattern.compile("^[A-Za-z0-9]+$").matcher(charStr).matches()) {
			return 0.5f;
		}
		// 判断是否为全角

		if (Pattern.compile("[\u4e00-\u9fa5]+$").matcher(charStr).matches()) {
			return 1.00f;
		}
		// 全角符号 及中文
		if (Pattern.compile("[^x00-xff]").matcher(charStr).matches()) {
			return 1.00f;
		}
		return 0.5f;

	}

	public static void addCellValue(Map<Integer, CellValue> map, int col, Double value, HSSFCellStyle style) {
		// TODO Auto-generated method stub
		if (map != null) {
			CellValue cell = new CellValue(value, style);
			map.put(col, cell);
		}
	}

	public static HSSFCellStyle getHSSFCellStyle(Workbook wb) {
		if (wb != null) {
			HSSFCellStyle setBorder = (HSSFCellStyle) wb.createCellStyle();
			setBorder.setWrapText(true);
			setBorder.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			setBorder.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			return setBorder;
		}
		return null;
	}

}
