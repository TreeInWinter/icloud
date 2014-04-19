package com.icloud.framework.core.util;

import org.junit.Test;

import com.icloud.framework.core.common.ReturnCode;
import com.icloud.framework.core.exception.BizException;

public class BizExceptionTest {
	@Test
	public void instance(){
		try{
			throw new NullPointerException();
		}catch(Exception e){
			throw BizException.instance(ReturnCode.ERROR, "error", e);
		}
	}
}
