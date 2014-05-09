package com.icloud.front.stock.bussiness.view;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.icloud.framework.core.wrapper.Pagination;
import com.icloud.front.stock.bussiness.BaseAction;
import com.icloud.stock.model.CategoryStock;
import com.icloud.stock.model.Stock;

@Service("stockListBussiness")
public class StockListBussiness extends BaseAction {
	public static final String CATEGORY_ID = "category.id";

	public Pagination<Stock> getStockListByType(int cateId, int pageNo,
			int limit) {
		Pagination<Stock> pagination = new Pagination<Stock>();
		pagination.setPageNo(pageNo);
		pagination.setPageSize(limit);

		if (pageNo < 0)
			pageNo = 0;
		if (limit <= 0)
			limit = 40;
		int start = limit * pageNo;
		List<Stock> resultList = null;
		if (cateId != -1) {
			resultList = new ArrayList<Stock>();
			long count = this.categoryStockService.countByProperties(
					CATEGORY_ID, cateId);
			pagination.setTotalItemCount(count);
			List<CategoryStock> findAll = this.categoryStockService
					.findByProperties(CATEGORY_ID, cateId, start, limit);
			for (CategoryStock cs : findAll) {
				resultList.add(cs.getStock());
			}
			pagination.setData(resultList);
		} else {
			pagination.setTotalItemCount(this.stockService.count());
			resultList = this.stockService.findAll(start, limit);
			pagination.setData(resultList);
		}
		pagination.build();
		return pagination;
	}

	public Pagination<Stock> getStockList(int pageNo, int limit) {
		return getStockListByType(-1, pageNo, limit);
	}

}
