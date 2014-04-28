package com.icloud.stock.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stock")
public class StockController {
	@RequestMapping("/stockMenu")
	// @ResponseBody
	public String helloWorld() {
		// System.out.println("say hello,world");
		// return "success";
		return "stock/stockMenu";
	}
}
