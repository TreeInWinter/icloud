package com.travelzen.framework.web.method.annotation;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;

import com.travelzen.framework.web.bind.annotation.RequestArray;

public class RequestArrayResolver extends BaseMethodArgumentResolver {
	public RequestArrayResolver() {
		super(RequestArray.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void bindRequestParameters(WebDataBinderFactory binderFactory, WebDataBinder binder, NativeWebRequest webRequest, MethodParameter parameter) throws Exception {
		ServletRequest servletRequest = prepareServletRequest(webRequest, parameter);
		if (Collection.class.isAssignableFrom(binder.getTarget().getClass())) { // bind collection
			Type type = parameter.getGenericParameterType();
			Class<?> componentType = Object.class;
			if (type instanceof ParameterizedType)
				componentType = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
			else if (parameter.getParameterType().isArray())
				componentType = parameter.getParameterType().getComponentType();
			Collection<Object> coll = (Collection<Object>) binder.getTarget();
			Enumeration<String> enums = servletRequest.getParameterNames();
			Set<String> prefixNames = new HashSet<>();
			while (enums.hasMoreElements()) {
				String prefixName = getPrefixName(enums.nextElement());
				if (prefixNames.contains(prefixName))
					continue;
				prefixNames.add(prefixName);
				Object component = BeanUtils.instantiate(componentType);
				if (component != null) {
					binderFactory.createBinder(webRequest, component, null).bind(new ServletRequestParameterPropertyValues(servletRequest, prefixName + '.', ""));
					coll.add(component);
				}
			}
		} else { // bind model
			ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
			servletBinder.bind(servletRequest);
		}
	}
}
