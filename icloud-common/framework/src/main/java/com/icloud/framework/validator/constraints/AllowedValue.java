package com.icloud.framework.validator.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.icloud.framework.validator.constraints.impl.AllowedValueValidator;

@Constraint(validatedBy = AllowedValueValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface AllowedValue {
	
	String[] values(); 
	
	String message() default "取值错误";
	
	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	
	@Target({ FIELD })
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		AllowedValue[] value();
	}
}
