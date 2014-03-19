package com.travelzen.framework.core.common;

import com.travelzen.framework.core.util.StringUtil;




/**
 * 
 * @author
 * @version 1.0
 */
public abstract class RootClass
{
	public String getCacheId(Class<?> cls, Object object)
	{
		if (object == null)
		{
			return StringUtil.getMD5(cls.getName() + "!");
		}

		String sSeparator = "@";

		if (object instanceof Long)
		{
			sSeparator = "#";
		}
		else if (object instanceof Integer)
		{
			sSeparator = "$";
		}

		return StringUtil.getMD5(cls.getName() + sSeparator + object.toString());
	}

 
}