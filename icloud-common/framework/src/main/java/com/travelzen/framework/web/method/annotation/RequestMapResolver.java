package com.travelzen.framework.web.method.annotation;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;

import com.travelzen.framework.web.bind.annotation.RequestMap;

public class RequestMapResolver extends BaseMethodArgumentResolver {
	public RequestMapResolver() {
		super(RequestMap.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void bindRequestParameters(WebDataBinderFactory binderFactory, WebDataBinder binder, NativeWebRequest webRequest, MethodParameter parameter) throws Exception {
		ServletRequest servletRequest = prepareServletRequest(webRequest, parameter);
		if (Map.class.isAssignableFrom(binder.getTarget().getClass())) { // bind map
			Map<Object, Object> map = (Map<Object, Object>) binder.getTarget();
			Enumeration<String> enums = servletRequest.getParameterNames();
			Set<String> prefixNames = new HashSet<>();
			while (enums.hasMoreElements()) {
				String prefixName = getPrefixName(enums.nextElement());
				if (prefixNames.contains(prefixName))
					continue;
				prefixNames.add(prefixName);
				RequestMap requestMap = parameter.getParameterAnnotation(RequestMap.class);
				Object value = BeanUtils.instantiate(requestMap.value());
				if (value != null) {
					binderFactory.createBinder(webRequest, value, null).bind(new ServletRequestParameterPropertyValues(servletRequest, prefixName + '.', ""));
					map.put(parseType(prefixName, requestMap.key()), value);
				}
			}
		} else { // bind model
			ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
			servletBinder.bind(servletRequest);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object parseType(String name, Class<?> type) {
		if (String.class.isAssignableFrom(type))
			return name;
		if (Integer.class.isAssignableFrom(type))
			return Integer.parseInt(name);
		if (Long.class.isAssignableFrom(type))
			return Long.parseLong(name);
		if (Float.class.isAssignableFrom(type))
			return Float.parseFloat(name);
		if (Double.class.isAssignableFrom(type))
			return Double.parseDouble(name);
		if (Byte.class.isAssignableFrom(type))
			return Byte.parseByte(name);
		if (Short.class.isAssignableFrom(type))
			return Short.parseShort(name);
		if (Character.class.isAssignableFrom(type))
			return name.charAt(0);
		if (type.isEnum())
			return Enum.valueOf((Class<Enum>) type, name);
		return name;
	}
}
