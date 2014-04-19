/**
 * 
 * Description: 格式化输出类
 * Copyright: Copyright (c) 2009
 * Company:云壤
 * @author 任水
 * @version 1.0
 * @date Dec 29, 2009
 */
package com.icloud.framework.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PrintUtil {
	/**
	 * 
	 * description: 用[和]包围c中所有的元素（元素之间用delimeter分隔）
	 * @param c
	 * @param delimeter
	 * @return
	 */
	public static String printCollectionWithEnclosingChar(Collection<?> c, String delimeter){
		return printCollectionWithEnclosingChar(c, delimeter, '[', ']');
	}
	/**
	 * 
	 * description:  使用open和close作为c中所有元素的前包围符和后包围符（元素之间用delimeter分隔）
	 * @param c
	 * @param delimeter
	 * @return
	 */
	public static String printCollectionWithEnclosingChar(Collection<?> c, String delimeter, char open, char close){
		return open + printCollection(c,delimeter) + close;
	}
	/**
	 * 
	 * description: 用[和]包围arr中所有的元素（元素之间用delimeter分隔）
	 * @param c
	 * @param delimeter
	 * @return
	 */
	public static String printArrayWithEnclosingChar(Object[] arr, String delimeter){
		return printArrayWithEnclosingChar(arr, delimeter, '[', ']');
	}
	/**
	 * 
	 * description: 使用open和close作为c中所有元素的前包围符和后包围符（元素之间用delimeter分隔）
	 * @param c
	 * @param delimeter
	 * @return
	 */
	public static String printArrayWithEnclosingChar(Object[] arr, String delimeter, char open, char close){
		return open + printArray(arr,delimeter) + close;
	}
	/**
	 * 
	 * description: 用[和]包围arr中所有的元素（元素之间用,分隔）
	 * @param c
	 * @param delimeter
	 * @return
	 */
	public static String printArrayWithEnclosingChar(Object[] arr){
		return printArrayWithEnclosingChar(arr, '[', ']');
	}
	/**
	 * 
	 * description: 使用open和close作为c中所有元素的前包围符和后包围符 
	 * @param arr
	 * @param open
	 * @param close
	 * @return
	 */
	public static String printArrayWithEnclosingChar(Object[] arr, char open, char close){
		return open + printArray(arr,",") + close;
	}
	/**
	 * 
	 * description: 用[和]包围c中所有的元素（元素之间用,分隔）
	 * @param c
	 * @param delimeter
	 * @return
	 */
	public static String printCollectionWithEnclosingChar(Collection<?> c){
		return printCollectionWithEnclosingChar(c, '[', ']');
	}
	/**
	 * 
	 * description: 使用open和close作为c中所有元素的前包围符和后包围符 
	 * @param c
	 * @param open
	 * @param close
	 * @return
	 */
	public static String printCollectionWithEnclosingChar(Collection<?> c, char open, char close){
		return open + printCollection(c,",") + close;
	}
	/**
	 * 
	 * description: 将arr中的元素用delimeter连接
	 * @param arr
	 * @param delimeter
	 * @return
	 */
	public static String printArray(Object[] arr, String delimeter){
		if(arr == null)
			return "";
		StringBuffer sb = new StringBuffer();
		for(Object element : arr)
			sb.append(element).append(delimeter);
		if(sb.length() >= delimeter.length())
			sb.delete(sb.length() - delimeter.length(), sb.length());
		return sb.toString();
	}
	public static String printCollection(Collection<?> c, String delimeter){
		if(c == null)
			return "";
		return printArray(c.toArray(), delimeter);
	}
	/**
	 * 
	 * description:将arr中的元素用,连接
	 * @param arr
	 * @return
	 */
	public static String printArray(Object[] arr){
		return printArray(arr,",");
	}
	/**
	 * 
	 * description:将c中的元素用,连接
	 * @param arr
	 * @return
	 */
	public static String printCollection(Collection<?> c){
		return printCollection(c,",");
	}
	/**
	 * 
	 * description: 将以secret开始的字段的值替换成******
	 * @param map
	 * @return 例子：[key1=value1,key2=value2]
	 */
	public static String printMapEscapingSensitiveValue(Map<?, ?> map){
		List<String> entryList = new ArrayList<String>();
		for(Object key : map.keySet())
			if(key != null && key.toString().startsWith("secret"))
				entryList.add(key + "=******");
			else entryList.add(key + "=" + map.get(key));
		return PrintUtil.printCollectionWithEnclosingChar(entryList);
	}
	/**
	 * 
	 * description: 打印obj所有字段的值
	 * @param obj
	 * @return 例子：[field1=value1,field2=value2]
	 */
	public static String printObjFields(Object obj){
		if(obj == null)
			return "";
		StringBuffer str = new StringBuffer("[");
		@SuppressWarnings("rawtypes")
		Class cls = obj.getClass();
		List<Field> allFields = new ArrayList<Field>();
    	while(cls != null && cls != Object.class){
    		Field[] fields = cls.getDeclaredFields();
    		for(Field field : fields)
    			allFields.add(field);
    		cls = cls.getSuperclass();
    	}
		for (Field field : allFields) {
			field.setAccessible(true);
			try {
				str.append(field.getName()).append("=").append(field.get(obj) != null ?
						field.get(obj).toString() : "null").append(",");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		str.replace(str.length() - 1, str.length(), "]");
		return str.toString();
	}
	/**
	 * 
	 * description: 将异常堆栈保存在字符串中
	 * @param e
	 * @return
	 */
	public static String getStackTrace(Throwable e){
		if(e == null)
			return "";
		
		ByteArrayOutputStream stackTrace = null;
		PrintStream ps = null;
		try{
			stackTrace = new ByteArrayOutputStream();
			ps = new PrintStream(stackTrace);
			e.printStackTrace(ps);
		}finally{
			if(ps != null){
				ps.close();
				ps = null;
			}
			if(stackTrace != null){
				try {
					stackTrace.close();
				} catch (IOException e1) {
				}
			}
		}
		
		return e.getMessage() + "\n" + stackTrace.toString();
	}
	/**
	 * @param bytes
	 * @return
	 */
	public static String toHexStr(byte[] bytes) {
		BigInteger bi = new BigInteger(bytes);
		// Format to hexadecimal
		String s = bi.toString(16);            // 120ff0
		if (s.length() % 2 != 0) {
		    // Pad with 0
		    s = "0"+s;
		}
		return s;
	}
}

