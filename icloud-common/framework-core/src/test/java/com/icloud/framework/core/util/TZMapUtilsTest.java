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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.icloud.framework.core.util.TZMapUtils;

public class TZMapUtilsTest {

    @Test
    public void test() {
	Map<String,String> map = new HashMap<String,String>();
	map.put("k1", "v1");
	map.put("k2", null);
	map.put("k3", null);
	TZMapUtils.removeAllNullValues(map);
	System.out.println(map);
    }

}
