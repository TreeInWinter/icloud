package com.travelzen.framework.util;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import com.travelzen.framework.core.time.DateTimeUtil;
import com.travelzen.framework.util.TZBeanUtils;

/**
 * 生成log内容,所有log调用此类生成log内容,统一log格式
 * @author wangmeng,Adam
 *
 */
public class LogFormatter {
	
	private static final String ENTER = "enter";
	private static final String EXIT = "exit";
	private static final String ERROR = "error";
	private static final String NULL = "null";
	private static final String SEPARTOR = "|";
	
	public static String format(Project project,String method,String id,String... msg){
		return generateLogContent(project,method,id,msg).toString();
	}

	public static String enterFormatWithBeans(Project project,String method, String id, Object... objs){
		StringBuilder log = commonLog(project,method);
		log.append(ENTER);
		log.append("|id:").append(id);
		log.append(generateObjectArgsLog(objs));
		return log.toString();
	}
	
	public static String enterFormat(Project project,String method, String id, String... objs){
		StringBuilder log = commonLog(project,method);
		log.append(ENTER).append(SEPARTOR);
		log.append("id:").append(id).append(SEPARTOR);
		log.append(generateStringArgsLog(objs));
		return log.toString();
	}
	
	public static String exitFormat(Project project,String method, String id, String... objs){
		StringBuilder log = commonLog(project,method);
		log.append("id:").append(id).append(SEPARTOR);
		log.append(EXIT).append(SEPARTOR);
		log.append(generateStringArgsLog(objs));
		return log.toString();
	}

	public static String errorFormat(Project project,String method, String id, String errorCode, String errorDescription, Throwable e){
		StringBuilder log = commonLog(project,method);
		log.append(ERROR).append(SEPARTOR+id);
		log.append("|error_code:"+errorCode+"|error_description:"+errorDescription).append(SEPARTOR);
		if(e != null){
			log.append("|\nexception:"+e.toString());
		}
		return log.toString();
	}
	
	public static String descrbeBean(Object obj){
		StringBuilder log = new StringBuilder();
		if(obj == null){
			log.append(NULL).append(SEPARTOR);
		}else{
			try{
				log.append(TZBeanUtils.describe(obj));
			}catch(Exception e){
				log.append(SEPARTOR);
			}
		}
		return log.toString();
	}
	
	@SuppressWarnings("unused")
	private static StringBuilder descrbeObjs(Object[] objs){
		StringBuilder log = new StringBuilder();
		if(objs == null) return log;
		for(int i=0;i<objs.length;i++){
			if(objs[i]==null){
				log.append("|object["+i+"]").append(NULL);
			}else{
				try{
					log.append("|object["+i+"]").append(TZBeanUtils.describe(objs[i]));
				}catch(Exception e){
					log.append(SEPARTOR);
				}
			}
		}
		return log;
	}
	
	/**
	 * print date+project+method string
	 * 
	 * @param project
	 * @param method
	 * @return
	 */
	private static StringBuilder commonLog(Project project,String method){
		StringBuilder content = new StringBuilder();
		content.append(DateTimeUtil.format(new DateTime(), "yyyy-MM-dd HH:mm:ss.SSS")).append(SEPARTOR);
		content.append("project:");
		if(project != null){
			content.append(project.name()).append(SEPARTOR);
		}else{
			content.append(NULL).append(SEPARTOR);
		}
		content.append("method:");
		if(StringUtils.isNotBlank(method)){
			content.append(method).append(SEPARTOR);
		}else{
			content.append(NULL).append(SEPARTOR);
		}
		return content;
	}
	private static StringBuilder generateLogContent(Project project,String method,String idValue,String... msgs){
		StringBuilder content = commonLog(project,method);
		content.append(idValue).append(SEPARTOR);
		content.append(generateStringArgsLog(msgs));
		return content;
	}
	
	private static StringBuilder generateStringArgsLog(String... msgs){
		StringBuilder content = new StringBuilder();
		if(msgs == null) return content;
		for(String msg : msgs){
			if(msg == null){
				content.append(NULL).append(SEPARTOR);
			}else{
				content.append(msg).append(SEPARTOR);
			}
		}
		return content;
	}
	
	private static StringBuilder generateObjectArgsLog(Object... objs){
		StringBuilder arrayContent = new StringBuilder();
		if(objs == null) return arrayContent;
		for(Object obj:objs){
			if(obj == null) {
				arrayContent.append(NULL).append(SEPARTOR);
				continue;
			}
			if(obj instanceof String || obj instanceof Boolean|| obj instanceof Integer||obj instanceof Long||obj instanceof Double||obj instanceof Float){
				arrayContent.append(obj.toString()).append(SEPARTOR);
			}else if(obj instanceof Date){
				arrayContent.append(DateTimeUtil.format((Date)obj, DateTimeUtil.DATE_TIME_PATTERN)).append(SEPARTOR);
			}else if(obj instanceof DateTime){
				arrayContent.append(DateTimeUtil.format((DateTime)obj, DateTimeUtil.DATE_TIME_PATTERN)).append(SEPARTOR);
			}else{
				try{
					arrayContent.append(TZBeanUtils.describe(obj)).append(SEPARTOR);
				}catch(Exception e){
					arrayContent.append(SEPARTOR);
				}
			}
		}
		return arrayContent;
	}

	public static enum Project{
		tops_front_operator_hotel,tops_hotel_creme_service,tops_hotel_creme,tops_data_static,
		tops_order_core,tops_front_operator_biz,tops_front_purchaser, tops_order_dao,tops_report_timing,tops_front_report,
		tops_hotel_qunar_service;
	}
}
