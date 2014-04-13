/**
* author: Simon Lee
* Date  : Aug 28, 2013
*/
package com.travelzen.framework.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelIEUtil {

	private static Logger logger = LoggerFactory.getLogger(ExcelIEUtil.class);

	private static int size = 1000;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void export(Map<String, String> fields, List<?> data, String fullName) {
		exportExcel(fields, data, fullName, size);
	}

	/**
	 *	导出数据到excel中，每个文件1000条数据，数据过多时，自动拆分文件
	 *
	 * @param fields 	导出的表的表头
	 * @param data 		需要导出的数据
	 * @param fullName  导出的文件全路径名
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static File exportExcel(Map<String, String> fields, List<?> data, String fullName, int fileSize) {
		try {
			Object fObj = data.get(0);
			Map<String, Field> fieldMap = new HashMap<String, Field>();
			getFieldMap(fObj.getClass(), fieldMap);

			// 拆分后的数据构成一个列表
			List list = new ArrayList();
			for (int i = 0; i * fileSize < data.size(); i++) {
				int end = (i + 1) * fileSize > data.size() ? data.size() - 1 : (i + 1) * fileSize;
				list.add(data.subList(i * fileSize, end));
			}

			// 拆分后的文件名列表
			List<String> fileName = new ArrayList<String>();
			if (list.size() == 1) {
				fileName.add(fullName);
			} else {
				for (int i = 1; i <= list.size(); i++) {
					int dotIdx = fullName.lastIndexOf('.');
					if (dotIdx == -1) {
						System.out.println("文件名应该包含后缀！");
					} else if (dotIdx == 0) {
						fileName.add("_" + i + fullName.substring(dotIdx));
					} else {
						fileName.add(fullName.substring(0, dotIdx) + "_" + i + fullName.substring(dotIdx));
					}
				}
			}

			// 设置每个文件内容
			for (int i = 0; i < list.size(); i++) {
				List dataI = (List)list.get(i);
				Workbook workbook = new HSSFWorkbook();
				Sheet sheet = workbook.createSheet();
				Row row0 = sheet.createRow(0);
				Row row1 = sheet.createRow(1);
				int coll0Num = 0;
				int coll1Num = 0;
				for (Map.Entry<String, String> entry : fields.entrySet()) {
					Cell cell0 = row0.createCell(coll0Num++);
					Cell cell1 = row1.createCell(coll1Num++);
					cell0.setCellValue(entry.getKey());
					cell1.setCellValue(entry.getValue());
				}
				int rowNum = 2;
				for (Object obj : dataI) {
					Row row = sheet.createRow(rowNum++);
					int collNum = 0;
					for (String field : fields.keySet()) {
						Cell cell = row.createCell(collNum++);
						Field f = fieldMap.get(field);
						f.setAccessible(true);
						Object value = f.get(obj);
						if (f.getType() == Date.class) {
							cell.setCellValue(sdf.format(value));
						} else if (f.getType() == DateTime.class) {
							cell.setCellValue(((DateTime)value).toString("yyyy-MM-dd HH:mm:ss"));
						} else {
							cell.setCellValue(value.toString());
						}
					}
				}
				FileOutputStream outputStream = new FileOutputStream(fileName.get(i));
				workbook.write(outputStream);
				IOUtils.closeQuietly(outputStream);
			}
			if (fileName.size() == 1) {
				return new File(fileName.get(0));
			} else {
				List<File> files = new ArrayList<File> ();
				for (String fn : fileName) {
					files.add(new File(fn));
				}
				File result = GZIPUtil.compress(GZIPUtil.pack(files.toArray(new File[] {}), new File("/tmp/stu.tar")));
				for (File f : files) {
					f.delete();
				}
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *	导出数据到excel中，并返回对应的二进制数据
	 *
	 * @param fields 	导出的表的表头
	 * @param data 		需要导出的数据
	 */
	public static byte[] exportBytes(Map<String, String> fields, List<?> data) {
		try {
			Object fObj = data.get(0);
			Map<String, Field> fieldMap = new HashMap<String, Field>();
			getFieldMap(fObj.getClass(), fieldMap);

			// 设置每个文件内容
			Workbook workbook = new HSSFWorkbook();
			Sheet sheet = workbook.createSheet();
			Row headRow = sheet.createRow(0);
			int coll1Num = 0;
			for (Map.Entry<String, String> entry : fields.entrySet()) {
				Cell cell1 = headRow.createCell(coll1Num++);
				cell1.setCellValue(entry.getValue());
			}
			int rowNum = 1;
			for (Object obj : data) {
				Row row = sheet.createRow(rowNum++);
				int collNum = 0;
				for (String field : fields.keySet()) {
					Cell cell = row.createCell(collNum++);
					Field f = fieldMap.get(field);
					f.setAccessible(true);
					Object value = f.get(obj);
					if (value == null) {
						cell.setCellValue("");
					} else if (f.getType() == Date.class) {
						cell.setCellValue(sdf.format(value));
					} else if (f.getType() == DateTime.class) {
						cell.setCellValue(((DateTime)value).toString("yyyy-MM-dd HH:mm:ss"));
					} else {
						cell.setCellValue(value.toString());
					}
				}
			}
			ByteArrayOutputStream opStrm = new ByteArrayOutputStream();
			workbook.write(opStrm);
			return opStrm.toByteArray();
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}
	}

	public static <T extends Object> List<T> importFromInputStream(Class<T> clz, InputStream input) throws Exception {
		Map<String, Field> fields = new HashMap<String, Field>();
		getFieldMap(clz, fields);
		Workbook workbook = new HSSFWorkbook(input);
		Sheet sheet = workbook.getSheetAt(0);
		if (sheet == null) {
			logger.error("Nothing in the excel file at sheet[0]");
			return null;
		}
		Row row0 = sheet.getRow(0);
		List<Field> fieldList = new ArrayList<Field>();
		int i = 0;
		Cell cell0 = null;
		while ((cell0 = row0.getCell(i ++)) != null && StringUtils.isNotBlank(cell0.getStringCellValue())) {
			String name = cell0.getStringCellValue();
			fieldList.add(fields.get(name));
		}
		int j = 2;
		Row rowI = sheet.getRow(j++);
		if (rowI == null) {
			logger.error("data should start at the 3rd row");
		}
		List<T> list = new LinkedList<T>();
		while (rowI != null) {
			T obj = clz.newInstance();
			int k = 0;
			Cell cellK = null;
			while (k < fieldList.size()) {
				cellK = rowI.getCell(k);
				if (cellK == null) {
					k++;
					continue;
				}
				Field field = fieldList.get(k);
				field.setAccessible(true);
				if (StringUtils.isNotBlank(cellK.getStringCellValue())) {
					String strValue = cellK.getStringCellValue();
					Object value = null;
					if (field.getType() == Integer.class) {
						value = Integer.parseInt(strValue);
					} else if (field.getType() == Double.class) {
						value = Double.parseDouble(strValue);
					} else if (field.getType() == Float.class) {
						value = Float.parseFloat(strValue);
					} else if (field.getType() == String.class) {
						value = strValue;
					} else if (field.getType() == Date.class) {
						value = sdf.parse(strValue);
					} else if (field.getType() == DateTime.class) {
						value = DateTime.parse(strValue, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
					} else if (field.getType() == Boolean.class) {
						value = Boolean.parseBoolean(strValue);
					}
					field.set(obj, value);
				}
				k++;
			}
			list.add(obj);
			rowI = sheet.getRow(j++);
		}
		return list;
	}

	public static <T extends Object> List<T> importFromExcel(Class<T> clz, String fileName) throws Exception {
		FileInputStream fis = new FileInputStream(fileName);
		return importFromInputStream(clz, fis);
	}

	private static <T extends Object> void getFieldMap(Class<T> clz, Map<String, Field> result) {
		for (Field field : clz.getDeclaredFields()) {
			result.put(field.getName(), field);
		}
		if (clz.getSuperclass() != null) {
			getFieldMap(clz.getSuperclass(), result);
		}
	}

}
