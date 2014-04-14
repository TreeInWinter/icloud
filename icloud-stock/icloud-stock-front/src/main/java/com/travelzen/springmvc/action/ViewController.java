package com.travelzen.springmvc.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * 如何返回view
 *
 */

@Controller
@RequestMapping("/view")
public class ViewController {
	/**
	 * 请求路径为 contextpath+/demo/demo,默认get请求
	 * @d
	 */
	
	@RequestMapping(value = "modelandview")
	public ModelAndView getDemo2(HttpServletRequest request, HttpServletResponse response) {
		 
		return new ModelAndView("page/view");
	}
	
	@RequestMapping(value = "redirect")
	public String getDemo3(HttpServletRequest request, HttpServletResponse response) {
		return "redirect:model";
	}
	
	@RequestMapping(value = "forward")
	public String getDemo4(HttpServletRequest request, HttpServletResponse response) {
		return "forward:model";
	}
}
