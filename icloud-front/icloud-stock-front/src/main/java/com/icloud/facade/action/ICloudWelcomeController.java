package com.icloud.facade.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ICloudWelcomeController {
	@RequestMapping("/index")
	public String helloWorld() {
		/**
		 * 入口
		 */
		return "redirect:/stock/stockMenu";
	}
}
