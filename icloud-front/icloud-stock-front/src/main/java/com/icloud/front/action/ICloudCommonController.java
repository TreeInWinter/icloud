package com.icloud.front.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/icloud")
public class ICloudCommonController {
	@RequestMapping("/about-us")
	public ModelAndView aboutUs() {
		ModelAndView model = new ModelAndView("icloud-base/icloud-aboutus");
		return model;
	}

	@RequestMapping("/contract-us")
	public ModelAndView contractUs() {
		ModelAndView model = new ModelAndView("icloud-base/icloud-contractus");
		return model;
	}
}
