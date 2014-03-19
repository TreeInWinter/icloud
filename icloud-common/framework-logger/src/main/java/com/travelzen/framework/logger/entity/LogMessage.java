/**
 * 
 */
package com.travelzen.framework.logger.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.joda.time.DateTime;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.travelzen.framework.dao.mongo.entity.CommonEntity;


/**
 * @author peilv
 * 信息实体。记录信息级别、信息时间、信息内容、处理方式。
 */
@Entity
public class LogMessage  extends CommonEntity{

    //做为日志使用 yegensheng 2013/03/24 如客户操作赋值客户编号 订单操作赋值为订单编号 财务操作赋值为财务账号 以此类推。。。
    public String systemLogFlagID ;
    //
    //     做为日志使用 当前登录的用户名 yegensheng 2013/03/24
    public String systemLoginID;
    //
    // 摘要:
    //     做为日志使用 如KH代表客户 CW代表财务 POS代表POS yegensheng 2013/03/24
    public String systemModule;
    public String content;
	//
    //     做为日志使用 当前操作类型 yegensheng 2013/03/24
    public String systemOptionType;
	//信息级别
	private int level;//  INFO = 0; WARN = 1;ERROR = 2;

	


	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);   
	}
	
	
 


	public String getSystemLogFlagID() {
		return systemLogFlagID;
	}


	public void setSystemLogFlagID(String systemLogFlagID) {
		this.systemLogFlagID = systemLogFlagID;
	}


	public String getSystemLoginID() {
		return systemLoginID;
	}


	public void setSystemLoginID(String systemLoginID) {
		this.systemLoginID = systemLoginID;
	}


	public String getSystemModule() {
		return systemModule;
	}


	public void setSystemModule(String systemModule) {
		this.systemModule = systemModule;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


 


	public String getSystemOptionType() {
		return systemOptionType;
	}


	public void setSystemOptionType(String systemOptionType) {
		this.systemOptionType = systemOptionType;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}

 
	
}
