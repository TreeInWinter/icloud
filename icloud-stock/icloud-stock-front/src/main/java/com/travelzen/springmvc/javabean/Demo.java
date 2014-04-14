package com.travelzen.springmvc.javabean;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;




public class Demo {
	/*@NotBlank
	@Size(max=2)*/
	private String name;
	
/*	@NotBlank(message = "code.empty")
	@Size(max=23)*/
	private String code;
	/*@Valid*/
	private NestedDemo nested;
	/*@Email*/
	private String email;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public NestedDemo getNested() {
		return nested;
	}
	public void setNested(NestedDemo nested) {
		this.nested = nested;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	

	

}
