package com.travelzen.springmvc.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.travelzen.springmvc.javabean.NestedDemo;

public class NestedDemoValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return NestedDemo.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		 ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nestedName", "nestedName.empty");
		 NestedDemo p = (NestedDemo) target;
	        if (p.getNestedCode().equals("1")) {
	        	errors.rejectValue("nestedCode", "nestedCode.invalid");
	        }
	}

	
}
