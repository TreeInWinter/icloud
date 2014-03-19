package com.travelzen.framework.aop;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.travelzen.framework.util.TZBeanUtils;

@Aspect
public abstract class TrimReturnValueAspect {

    @Pointcut
    public abstract void trimOperation();

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @AfterReturning(
	    pointcut="trimOperation()",
	    returning="retVal"
     )
    public void trim(Object retVal) throws Throwable {
	if(retVal == null)
	    return;
	if(retVal instanceof Collection){
	    Iterator iter = ((Collection) retVal).iterator();
	    while(iter.hasNext()){
		TZBeanUtils.trimObjectFields(iter.next());
	    }
	}else if(retVal instanceof Map){
	    Iterator iter = ((Map) retVal).entrySet().iterator();
	    while(iter.hasNext()){
		Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>)iter.next();
		Object value = entry.getValue();
		if(null != value){
		    entry.setValue(TZBeanUtils.trimObjectFields(value));
		}
	    }
	}else{
	    TZBeanUtils.trimObjectFields(retVal);
	}
    }
}
