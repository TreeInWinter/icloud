package com.icloud.stock.action;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.icloud.framework.logger.ri.RequestIdentityLogger;

@Controller
@RequestMapping("/stock")
public class StockController {
	private static final Logger logger = RequestIdentityLogger
			.getLogger(StockController.class);

	@RequestMapping("/stockMenu")
	// @ResponseBody
	// @RequestParam(required=true) String hotelId
	public ModelAndView stockMenu() {
		ModelAndView model = new ModelAndView("stock/stockMenu");
		// model.addObject("roomCatList", lvRoomCats);
		// model.addObject("bookingClassMap", lvBookingClassMap);
		// model.addObject("breakfastComp",BreakfastEnum.values());
		// model.addObject("dateListMap", lvDateWeekMap);
		// model.setViewName();
		return model;
	}

	@RequestMapping("/stockListMenu")
	public ModelAndView stockListMenu() {
		ModelAndView model = new ModelAndView("stock/stockList");
		return model;
	}

	@RequestMapping("/getStockMenu")
	@ResponseBody
	public String getStockMenu(String id) {
		logger.info(id);
		return "你好1111 " + id;
	}

}
