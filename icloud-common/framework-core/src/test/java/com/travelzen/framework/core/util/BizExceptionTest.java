package com.travelzen.framework.core.util;

import org.junit.Test;

import com.travelzen.framework.core.common.ReturnCode;
import com.travelzen.framework.core.exception.BizException;

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
