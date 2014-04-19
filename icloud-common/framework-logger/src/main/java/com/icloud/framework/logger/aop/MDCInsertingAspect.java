package com.icloud.framework.logger.aop;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;

import com.icloud.framework.logger.ri.RequestIdentityHolder;

/**
 * 在MDC中增加参数
 * 
 * @author renshui
 * 
 */
@Aspect
public abstract class MDCInsertingAspect {

	@Pointcut
	public abstract void mdcInsertingOperation();

	@Around("mdcInsertingOperation()")
	public Object insert(final ProceedingJoinPoint pjp) throws Throwable {

		try {
			String rpid = RequestIdentityHolder.get();
			if(StringUtils.isBlank(rpid))
				rpid = RandomStringUtils.randomNumeric(10);
			MDC.put("rpid", String.format("[rpid=%s]", rpid));
			return pjp.proceed();
		} finally {
			MDC.clear();
		}
	}
}
