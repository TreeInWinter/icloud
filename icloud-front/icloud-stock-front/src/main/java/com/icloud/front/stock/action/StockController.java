package com.icloud.front.stock.action;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.icloud.framework.core.wrapper.PageView;
import com.icloud.framework.core.wrapper.Pagination;
import com.icloud.framework.logger.ri.RequestIdentityLogger;
import com.icloud.front.stock.baseaction.BaseStockController;
import com.icloud.front.stock.pojo.BaseStockMenu;
import com.icloud.front.stock.pojo.StockMenuBean;
import com.icloud.stock.model.Stock;
import com.icloud.stock.model.constant.StockConstants.BaseCategory;

@Controller
@RequestMapping("/stock")
public class StockController extends BaseStockController {
	private static final Logger logger = RequestIdentityLogger
			.getLogger(StockController.class);

	@RequestMapping("/stockMenu")
	// @ResponseBody
	// @RequestParam(required=true) String hotelId
	public ModelAndView stockMenu() {
		ModelAndView model = new ModelAndView("stock/mainPage");
		model.addObject("mainMenus", stockCommonBussiness.getBaseMenu());
		return model;
	}

	@RequestMapping("/listStockView")
	public ModelAndView stockListMenu(
			@RequestParam(required = true) String cateId, String pageNo) {
		ModelAndView model = new ModelAndView("stock/stock-list-view");
		BaseStockMenu baseStockMenu = this.stockCommonBussiness
				.getBaseStockMenu(cateId);
		Pagination<Stock> pagination = this.stockListBussiness.getStockList(
				cateId, pageNo, 30);
		PageView pageView = PageView.convertPage(pagination);
		model.addObject("pagination", pagination);
		model.addObject("pageView", pageView);
		model.addObject("baseStockMenu", baseStockMenu);
		model.addObject("cateId", cateId);
		return model;
	}

	@RequestMapping("/getStockMenu")
	public ModelAndView getStockMenu(String id) {
		ModelAndView model = new ModelAndView("stock/menus");
		if (id == null)
			id = BaseCategory.BASE.getType();
		List<StockMenuBean> menuList = stockCommonBussiness
				.getStockMenuBean(id);
		model.addObject("menuList", menuList);
		return model;
	}
}
