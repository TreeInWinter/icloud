package com.icloud.front.common.freemarker;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class CurrentUrlGetter implements TemplateMethodModel {

	@Override
	public Object exec(List arguments) throws TemplateModelException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getRequestURL().toString();
	}

}
