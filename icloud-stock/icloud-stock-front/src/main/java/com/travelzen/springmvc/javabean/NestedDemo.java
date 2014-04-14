package com.travelzen.springmvc.javabean;

import org.hibernate.validator.constraints.NotBlank;



public class NestedDemo {
	@NotBlank
	private String nestedName;
	@NotBlank
	private String nestedCode;
	
	public String getNestedName() {
		return nestedName;
	}
	public void setNestedName(String nestedName) {
		this.nestedName = nestedName;
	}
	public String getNestedCode() {
		return nestedCode;
	}
	public void setNestedCode(String nestedCode) {
		this.nestedCode = nestedCode;
	}


	

}
