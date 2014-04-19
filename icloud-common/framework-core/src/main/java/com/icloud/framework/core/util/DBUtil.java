/**  
*   
* 项目名称：framework  
* 类名称：DBUtil  
* 类描述：  
* 创建人：liangwang  
* 创建时间：2011-11-1 下午01:45:11  
* 修改人：liangwang  
* 修改时间：2011-11-1 下午01:45:11  
* 修改备注：  
* @version   
*   
*/ 
package com.icloud.framework.core.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author liangwang
 *
 */
public class DBUtil {

	/**
	 * 数据库表记录主键值
	 */
	public static String getTablePK() {
		return RandomStringUtils.randomAlphanumeric(10);
	}
	
	
	/**
	 * 数据库表记录主键值
	 */
	public static String getTableSimplePK() {
		return RandomStringUtils.randomAlphanumeric(6).toLowerCase();
	}
}
