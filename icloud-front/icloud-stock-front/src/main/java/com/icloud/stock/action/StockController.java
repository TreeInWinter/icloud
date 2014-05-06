package com.icloud.stock.action;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.icloud.framework.logger.ri.RequestIdentityLogger;
import com.icloud.front.business.ICloudCommonBussiness;
import com.icloud.stock.bussiness.BaseAction;
import com.icloud.stock.model.constant.StockConstants.BaseCategory;

@Controller
@RequestMapping("/stock")
public class StockController extends BaseAction {
	private static final Logger logger = RequestIdentityLogger
			.getLogger(StockController.class);

	@RequestMapping("/stockMenu")
	// @ResponseBody
	// @RequestParam(required=true) String hotelId
	public ModelAndView stockMenu() {
		ModelAndView model = new ModelAndView("stock/mainPage");
		model.addObject("mainMenus", ICloudCommonBussiness.getBaseMenu());
		return model;
	}

	@RequestMapping("/stockListMenu")
	public ModelAndView stockListMenu() {
		ModelAndView model = new ModelAndView("stock/stockList");
		return model;
	}

	@RequestMapping("/getStockMenu")
	// @ResponseBody
	public ModelAndView getStockMenu(String id) {
		ModelAndView model = new ModelAndView("stock/menus");
		if (id == null)
			id = BaseCategory.BASE.getType();
		id = id.trim();
		if (BaseCategory.XUEQIU.getType().equals(id)) {

		} else if (BaseCategory.ZHENGJIANHUI.getType().equals(id)) {

		} else {

		}
		return model;
	}
}
