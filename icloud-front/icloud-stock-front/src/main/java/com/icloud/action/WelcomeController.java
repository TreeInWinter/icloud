package com.icloud.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WelcomeController {
	@RequestMapping("/index")
	// @ResponseBody
	public String helloWorld() {
		// System.out.println("say hello,world");
		// return "success";
		return "index";
	}
}
