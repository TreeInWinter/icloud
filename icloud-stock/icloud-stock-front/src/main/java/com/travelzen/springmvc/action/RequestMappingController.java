package com.travelzen.springmvc.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @RequestMapping标签
 *
 */

@Controller
@RequestMapping("/requestmapping")
public class RequestMappingController {

	/**
	 * 请求路径为 contextpath+/demo/demo,默认get请求
	 * @RequestParam 绑定请求参数,默认是必须有这个参数
	 */
	@RequestMapping(value = "/demodefault")
	public ModelAndView getDemo(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("page/requestMapping");
		return view;

	}


	/**
	 * 请求路径为 contextpath+/demo/demo,默认get请求
	 * @RequestParam 绑定请求参数,默认是必须有这个参数
	 */
	@RequestMapping(value = "/get/demo")
	public ModelAndView getDemo(HttpServletRequest request, HttpServletResponse response, @RequestParam String demo) {
		ModelAndView view = new ModelAndView("page/requestMapping");
		view.addObject("arg", "传进来的参数是"+demo);
		return view;

	}
	/**
	 *   请求路径为contextpath+/demo/test,默认为get请求
	 *   @RequstParam 绑定请求参数，默认必须有这个参数
	 */
	@RequestMapping(value="/test")
	public ModelAndView getTest(HttpServletRequest request,	HttpServletResponse response){
		ModelAndView view=new ModelAndView("page/test");
		return view;
	}
	/**
	 * 请求路径为 contextpath+/demo/demo1,显示指定get请求
	 * @RequestParam(required=false) 这个参数可以不加
	 */
	@RequestMapping(value = "/get/demo1", method = RequestMethod.GET)
	public ModelAndView getDemo1(HttpServletRequest request, HttpServletResponse response, @RequestParam(required=false) String demo) {
		ModelAndView view = new ModelAndView("page/requestMapping");
		view.addObject("arg1", "传进来的参数是"+demo);
		return view;
	}

	/***
	 * 请求路径为 contextpath+/demo/demo2,post请求
	 */
	@RequestMapping(value = "/demo2", method = RequestMethod.POST)
	public ModelAndView getDemo2(HttpServletRequest request, HttpServletResponse response, @RequestParam String demo) {
		ModelAndView view = new ModelAndView("page/requestMapping");
		view.addObject("arg3", "传进来的参数是"+demo);
		return view;
	}

	/**
	 * rest风格的url
	 */
	@RequestMapping(value = "/rest/{demo3}/{demo4}", method = RequestMethod.GET)
	public ModelAndView getRest(@PathVariable String demo3, @PathVariable String demo4) {
		ModelAndView view = new ModelAndView("page/requestMapping");
		view.addObject("arg4", "传进来的参数demo3="+demo3 +" demo4="+demo4);
		return view;
	}

	/**
	 * 正则表达式
	 */
	@RequestMapping(value = "/regex/{version:\\d*}", method = RequestMethod.GET)
	public ModelAndView getRegex(@PathVariable String version) {
		ModelAndView view = new ModelAndView("page/requestMapping");
		view.addObject("arg5", "传进来的参数="+version);
		return view;
	}

	/**
	 * 只接受request 的content-type为指定类型
	 */
	@RequestMapping(value = "/consume", method = RequestMethod.POST,consumes={"text/xml", "application/json"})
	@ResponseBody
	public String getConsume(@RequestBody String name) {
		return name;
	}

	/**
	 * 只接受request 的accept为指定类型,客户端请求返回json，常和@ResponseBody一起用,@ResponseBody
	 * 指把返回值写到response的数据区域,而不是返回页面
	 */
	@RequestMapping(value = "/produce", method = RequestMethod.GET,produces="application/json")
	@ResponseBody
	public String getProduce(@RequestParam(required=false) String name) {
		return "{'name':'name'}";
	}

	/**
	 * 请求参数中有参数myParam并且myParam=myValue
	 * @param demo
	 */
	@RequestMapping(value = "/demo12", method = RequestMethod.GET, params="myParam=myValue")
	  public void getDemo12(@RequestParam String demo) {
	    // implementation omitted
	  }
	/**
	 * 请求参数中有参数myParam
	 * @param demo
	 */
	@RequestMapping(value = "/demo13", method = RequestMethod.GET, params="myParam")
	  public void getDemo13(@RequestParam String demo) {
	    // implementation omitted
	  }

	/**
	 * 请求参数没有myParam
	 * @param demo
	 */
	@RequestMapping(value = "/demo14", method = RequestMethod.GET, params="!myParam")
	  public void getDemo14(@RequestParam String demo) {
	    // implementation omitted
	  }

	/**
	 * 请求的header中有myHeader=myValue
	 * @param demo
	 */
	@RequestMapping(value = "/demo15", method = RequestMethod.GET, headers="myHeader=myValue")
	  public void getDemo15(@RequestParam String demo) {
	    // implementation omitted
	  }
}
