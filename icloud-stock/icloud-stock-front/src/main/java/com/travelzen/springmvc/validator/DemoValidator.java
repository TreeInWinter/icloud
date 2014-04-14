package com.travelzen.springmvc.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.travelzen.springmvc.javabean.Demo;

public class DemoValidator implements Validator{
	    
	@Override
	public boolean supports(Class<?> clazz) {
		return Demo.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		 ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty");
		 ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "code.empty");
		 ValidationUtils.rejectIfEmpty(errors, "nested.nestedName", "nestedName.empty");
	}

	
}
