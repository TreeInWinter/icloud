/**
 * 
 * Description: 
 * Copyright (c) 2013
 * Company:真旅网
 * @author renshui
 * @version 1.0
 * @date 2013-6-4
 */
package com.icloud.framework.core.util;

import java.util.LinkedList;
import java.util.Map;

public class TZMapUtils {
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map removeAllNullValues(Map map){
	java.util.List list = new LinkedList();
	list.add(null);
	map.values().removeAll(list);
	return map;
    }
}
