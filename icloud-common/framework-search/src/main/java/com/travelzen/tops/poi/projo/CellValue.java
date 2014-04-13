package com.travelzen.tops.poi.projo;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;

public class CellValue {
	public static final int StringValue = 1;
	public static final int integerValue = 0;
	private Object object;
	private int type = 0;// 0:int,1:string
	private HSSFCellStyle style;

	public CellValue(String str, HSSFCellStyle style) {
		object = str;
		type = StringValue;
		this.style = style;
	}

	public CellValue(double i, HSSFCellStyle style) {
		object = i;
		type = integerValue;
		this.style = style;
	}

	public void setCell(Cell cell) {
		if (type == integerValue) {
			cell.setCellValue((Double) object);
			if (style != null) {
				cell.setCellStyle(style);
			}
			return;
		}
		if (type == StringValue) {
			cell.setCellValue((String) object);
			if (style != null) {
				cell.setCellStyle(style);
			}
			return;
		}
	}

	public float getHeight() {
		// TODO Auto-generated method stub
		if (type == integerValue) {
			return 0.0f;
		}
		if (type == StringValue) {
			return PoiDataUtil.getExcelCellAutoHeight((String) object, 11.0f);
		}
		return 0.0f;
	}
}
