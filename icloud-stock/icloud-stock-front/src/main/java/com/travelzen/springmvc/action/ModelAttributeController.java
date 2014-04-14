package com.travelzen.springmvc.action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.travelzen.springmvc.javabean.Demo;


/**
 * ModelAttribute用法
 * 1.放在方法上，主要用于页面上有些下拉菜单等数据，每次请求都需要这些数据，或者form表单中model的绑定
 *  在调用所有标记有@RequestMapping的方法之前执行,有两种形式，如下
 *
 * 2.用在参数前：用于改变页面和bean绑定的名字
 */
@Controller
@RequestMapping("/modelattribute")
public class ModelAttributeController {

	@ModelAttribute
	public Demo getDemo1(){
		return new Demo();
	}
	
	@ModelAttribute
	public void getDemo2(Model model) {
		Demo demo1 = new Demo();
		Demo demo2 = new Demo();
	    model.addAttribute("demo1", demo1);
	    model.addAttribute("demo2", demo2);
	}
	
	@RequestMapping("modelattribute")
	public String getDemo3(@ModelAttribute Demo demo) {
		return "";
	}
	

}
