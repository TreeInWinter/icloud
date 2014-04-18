package com.travelzen.springmvc.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 如何返回view
 *
 */

@Controller
public class ViewController {
	@RequestMapping("/index")
	// @ResponseBody
	public String helloWorld() {
		System.out.println("say hello,world");
		// return "success";
		return "index";
	}
}
