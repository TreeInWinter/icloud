package com.icloud.framework.web.method.annotation;

import java.lang.annotation.Annotation;
import java.util.Iterator;

import javax.servlet.ServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public abstract class BaseMethodArgumentResolver implements HandlerMethodArgumentResolver {
	private Class<? extends Annotation> cls;

	public BaseMethodArgumentResolver(Class<? extends Annotation> cls) {
		this.cls = cls;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(cls);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Annotation annotation = parameter.getParameterAnnotation(cls);
		Object target = BeanUtils.instantiateClass((Class<?>) annotation.getClass().getMethod("type").invoke(annotation));
		WebDataBinder binder = binderFactory.createBinder(webRequest, target, null);
		if (target != null)
			bindRequestParameters(binderFactory, binder, webRequest, parameter);
		target = binder.convertIfNecessary(binder.getTarget(), parameter.getParameterType());
		return target;
	}

	protected abstract void bindRequestParameters(WebDataBinderFactory binderFactory, WebDataBinder binder, NativeWebRequest webRequest, MethodParameter parameter) throws Exception;

	protected ServletRequest prepareServletRequest(NativeWebRequest webRequest, MethodParameter parameter) {
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		Iterator<String> iterator = webRequest.getParameterNames();
		while (iterator.hasNext()) {
			String name = iterator.next();
			if (name.contains("."))
				mockRequest.addParameter(name, webRequest.getParameterValues(name));
		}
		return mockRequest;
	}

	protected String getPrefixName(String name) {
		return name.substring(0, name.lastIndexOf('.'));
	}
}
