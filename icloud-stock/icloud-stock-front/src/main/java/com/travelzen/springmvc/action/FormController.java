package com.travelzen.springmvc.action;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

//import com.travelzen.hotel.gta.entity.Hotel;
import com.travelzen.springmvc.javabean.Demo;
//import com.travelzen.springmvc.service.IDemoService;
import com.travelzen.springmvc.validator.DemoValidator;

/**
 * for test
 * 
 * @author wangmeng
 * 
 */

/**
 * 一、如何注册controller 1:在类前加上Controller标签 2.在配置文件里 <mvc:annotation-driven/>
 * <context:component-scan base-package="com.travelzen.springmvc.action"/>
 */
@Controller
@RequestMapping("/form")
public class FormController extends BaseAction {

	// @Resource private IDemoService demoService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new DemoValidator());
	}

	// @InitBinder
	// public void initBinder(WebDataBinder binder) {
	// //binder.registerCustomEditor(Date.class, new CustomDateEditor(new
	// SimpleDateFormat("yyyy-MM-dd"), false));
	// binder.setValidator(new DemoValidator());
	// }

	/**
	 * 
	 * @param request
	 * @param locale
	 * @param demo
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/saveHotel", method = RequestMethod.POST)
	public ModelAndView saveHotel(@Valid Demo demo, BindingResult result) {

		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			mav.addObject("demo", demo);
			mav.addObject("abc", new ModelAndView());
			mav.setViewName("page/form");
		} else {
			mav.setViewName("redirect:getForm");
		}
		return mav;
	}

	@RequestMapping(value = "/getForm", method = RequestMethod.GET)
	public ModelAndView getHotel(HttpServletRequest request) {

		ModelAndView mav = new ModelAndView();
		mav.addObject("demo", new Demo());
		mav.setViewName("page/form");
		// List<Hotel> hotels = demoService.getHotel();
		// mav.addObject("hotels", hotels);
		return mav;
	}

	@RequestMapping(value = "/ajaxValidate", method = RequestMethod.POST)
	@ResponseBody
	public String validate(@RequestParam String validate) throws UnsupportedEncodingException {
		ErrorResult result = new ErrorResult("#nameerror", "不能为空");
		return JSONArray.fromObject(result).toString();
	}

	public static class ErrorResult {
		private String errorInfo;
		private String errorInfoSelector;

		public ErrorResult(String errorInfoSelector, String errorInfo) {
			this.errorInfo = errorInfo;
			this.errorInfoSelector = errorInfoSelector;
		}

		public String getErrorInfo() {
			return errorInfo;
		}

		public void setErrorInfo(String errorInfo) {
			this.errorInfo = errorInfo;
		}

		public String getErrorInfoSelector() {
			return errorInfoSelector;
		}

		public void setErrorInfoSelector(String errorInfoSelector) {
			this.errorInfoSelector = errorInfoSelector;
		}

	}

}
