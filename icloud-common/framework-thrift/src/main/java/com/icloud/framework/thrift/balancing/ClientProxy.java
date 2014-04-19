package com.icloud.framework.thrift.balancing;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ClientProxy {
    public static <T> T apply(Class<? extends T> cls, final ThriftInvocation thriftInvocation) throws Throwable {
        InvocationHandler handler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Invocation<T> invocation = new Invocation<T>(method, args);
                return thriftInvocation.doBusiness(invocation);
            }
        };
		return (T) Proxy.newProxyInstance(cls.getClassLoader(),cls.getInterfaces(), handler);
	}
}

