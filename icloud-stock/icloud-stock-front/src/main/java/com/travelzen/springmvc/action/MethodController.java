package com.travelzen.springmvc.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.travelzen.springmvc.javabean.Demo;

/**
 * 如何定义controller中的方法
 */

@Controller
/**
 *定义在类上,表示访问该类的基路径,容器启动的时候会注册改路径
 */
@RequestMapping("/method")
public class MethodController {

	/**
	 * 
	 * Locale 语言
	 * 
	 * @RequestParam 请求参数
	 * @PathVariable restful请求参数
	 * @RequestBody http协议的数据区域,用于前端向后端发送json或者xml ModelMap
	 *              方法中可以将数据放入ModelMap中返回给页面
	 * @CookieValue 获取cookie
	 * @RequestHeader 获取header
	 */
	@RequestMapping(value = "/demo")
	public ModelAndView getDemo(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView view = new ModelAndView("page/method");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}

	/**
	 * 
	 * Demo springmvc 会自动绑定,把request参数中和demo字段名字相同的值设进去，
	 * 
	 * @ModelAttribute 可选，放在参数前可以修改页面绑定Demo的名称，不加默认是javabean的名字第一个字母小写
	 *                 BindingResult 用于springmvc 的验证，可以从BindingResult获取错误信息
	 * @Valid 加上这个标签springmvc会验证该javabean
	 */
	@RequestMapping(value = "demo2", method = RequestMethod.POST)
	public ModelAndView getDemo(@ModelAttribute("demo2") Demo demo) {
		ModelAndView view = new ModelAndView("page/method");
		view.addObject("demo", demo);
		return view;
	}

}
