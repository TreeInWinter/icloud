package com.icloud.stock.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MyInterceptor {

	@Pointcut("execution (* com.icloud.stock.business.impl.PersonServiceImpl.*(..))")
	private void anyMethod() {
		System.out.println("--------------------");
	}// 声明一个切入点

	@Before("anyMethod() && args(name)")
	// 定义前置通知,拦截的方法不但要满足声明的切入点的条件,而且要有一个String类型的输入参数,否则不会拦截
	public void doAccessCheck(String name) {
		System.out.println("前置通知:" + name);
	}

	@AfterReturning(pointcut = "anyMethod()", returning = "result")
	// 定义后置通知,拦截的方法的返回值必须是int类型的才能拦截
	public void doAfterReturning(int result) {
		System.out.println("后置通知:" + result);
	}

	@AfterThrowing(pointcut = "anyMethod()", throwing = "e")
	// 定义例外通知
	public void doAfterThrowing(Exception e) {
		System.out.println("例外通知:" + e);
	}

	@After("anyMethod()")
	// 定义最终通知
	public void doAfter() {
		System.out.println("最终通知");
	}

	@Around("anyMethod()")
	// 定义环绕通知
	public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
		// if(){//判断用户是否在权限
		System.out.println("进入方法");
		Object result = pjp.proceed();// 当使用环绕通知时，这个方法必须调用，否则拦截到的方法就不会再执行了
		System.out.println("退出方法");
		// }
		return result;
	}

}