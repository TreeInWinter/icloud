/**
 * 
 * Description: 提供随机数实现
 * Copyright: Copyright (c) 2009
 * Company:云壤
 * @author 任水
 * @version 1.0
 * @date Dec 28, 2009
 */
package com.icloud.framework.core.util;

import java.util.Random;

public class RandomUtil {

	/**
	 * 
	 * description: 生成长度为length的随机串
	 * @param length
	 * @return
	 */
	public static String getRandomStr(int length) {
		Random rand = new Random();
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < length; i++)
			sb.append(rand.nextInt(10));
		return sb.toString();
	}

}

