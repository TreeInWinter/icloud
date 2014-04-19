package com.icloud.framework.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.retry.RetryCallback;
import org.springframework.batch.retry.RetryContext;
import org.springframework.batch.retry.support.RetryTemplate;

@Aspect
public abstract class RetryFaultToleranceAspect {
	private static Logger svLogger = LoggerFactory.getLogger(RetryFaultToleranceAspect.class);
	@Pointcut
	public abstract void retryOperation();
	private RetryTemplate retryTemplate;
	@Around("retryOperation()")
	public Object retry(final ProceedingJoinPoint pjp) throws Throwable {
		svLogger.debug("is arounding " + pjp.getSignature().toShortString());
		RetryCallback<Object> worker = new RetryCallback<Object>() {
									public Object doWithRetry(RetryContext retryContext) throws Exception {
											try {
												return pjp.proceed();
											} catch (Exception ex) {
											    	if(retryContext.getRetryCount() == 1)
											    	    svLogger.error("", ex);
												throw ex;
											} catch (Error error) {
											    	if(retryContext.getRetryCount() == 1)
											    	    svLogger.error("", error);
												throw error;
											} catch (Throwable t) {
											    	if(retryContext.getRetryCount() == 1)
											    	    svLogger.error("", t);
												throw new IllegalStateException("Caught throwable that is neither " + "Exception nor Error");
											}
									}};
       return retryTemplate.execute(worker);
}
	public void setRetryTemplate(RetryTemplate retryTemplate) {
		this.retryTemplate = retryTemplate;
	}
}
