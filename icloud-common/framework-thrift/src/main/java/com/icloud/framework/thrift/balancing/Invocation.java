package com.icloud.framework.thrift.balancing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

public class Invocation<T> {

    private Method method;
    private Object[] args;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Invocation(Method method, Object[] args) {
        name = method.getName();
        this.method = method;
        this.args = args;
    }

    public Object doInvoke(T obj) throws Throwable {
        try {
            return method.invoke(obj, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (UndeclaredThrowableException e) {
            throw e.getUndeclaredThrowable();
        } catch (Throwable th) {
            throw th;
        }
    }

}