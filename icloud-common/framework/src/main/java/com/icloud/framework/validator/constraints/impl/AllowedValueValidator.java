package com.icloud.framework.validator.constraints.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.icloud.framework.validator.constraints.AllowedValue;

public class AllowedValueValidator implements ConstraintValidator<AllowedValue, String> {
	private String[] allowedValues;
	@Override
	public void initialize(AllowedValue constraintAnnotation) {
		// TODO Auto-generated method stub
		allowedValues = constraintAnnotation.values();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null)
			return true;
		if("".endsWith(value.trim()))
			return true;
		// TODO Auto-generated method stub
		for(String allowedValue: allowedValues)
			if(allowedValue.equals(value))
				return true;
		return false;
	}


}
