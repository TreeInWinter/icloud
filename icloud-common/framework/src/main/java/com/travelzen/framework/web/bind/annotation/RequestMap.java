package com.travelzen.framework.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMap {
	Class<?> type() default HashMap.class; // 实例化Map的类型，默认为HashMap
	Class<?> key() default String.class; // 实例化Map的Key类型，默认为String
	Class<?> value(); // 实例化Map的Value类型，必填项，必须为对象类型
}
