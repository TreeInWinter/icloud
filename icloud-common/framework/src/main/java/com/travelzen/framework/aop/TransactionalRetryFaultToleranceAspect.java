package com.travelzen.framework.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
@Order(50)
@Aspect
public class TransactionalRetryFaultToleranceAspect extends RetryFaultToleranceAspect{
	@Pointcut("execution(@TransactionalRetryAnnotation * *.*(..))")
	private void transactionalRetryOp() {}
	@Override
	@Pointcut("transactionalRetryOp()")
	public void retryOperation(){}
}
