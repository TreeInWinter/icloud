$(document).ready(function() {
	$("#orderList").kendoGrid({
		dataSource: {
			type: "odata",
			transport: {
				read: "http://demos.kendoui.com/service/Northwind.svc/Orders"
			},
			schema: {
				model: {
					fields: {
						OrderID: {
							type: "number"
						},
						Freight: {
							type: "number"
						},
						ShipName: {
							type: "string"
						},
						OrderDate: {
							type: "date"
						},
						ShipCity: {
							type: "string"
						}
					}
				}
			},
			pageSize: 10,
			serverPaging: true,
			serverFiltering: true,
			serverSorting: true
		},
		dataBinding: function() {
			//@todo add some code here
		},
		sortable: true,
		pageable: {
			messages: {
				display: " 共 {2} 个订单，{0} - {1} 条",
				empty: "找个0个记录",
				page: "页",
				of: "共 {0}",
				itemsPerPage: "每页",
				first: "第一页",
				previous: "前一页",
				next: "后一页",
				last: "最后页",
				refresh: "刷新"
			}
		},
		resizable: true,
		selectable: "single",
		dataBound: function() {
			//
		},
		columns: [{
			field: "OrderID",
			title: "订单编号",
			width: 135,
			template: "<a href=\"javascript:orderDetail('#=OrderID#');\">#=OrderID#</a>"
		},

		/*
		{
			field: "OrderDate",
			title: "订单日期",
			width: 120,
			format: "{0:yyyy-MM-dd}"
		},
        */
		{
			field: "ShipName",
			title: "PNR",
			width: 78,
			template: kendo.template("Jy4w")
		},
		{
			field: "ShipName",
			title: "票号",
			width: 118,
			template: kendo.template("871-16127777121")
		},
		{
			field: "ShipName",
			title: "订单状态",
			width: 78,
			template: kendo.template("已出票")
		},
		{
			field: "ShipName",
			title: "航班",
			width: 78,
			template: kendo.template("SHAPEK")
		},
		{
			field: "ShipName",
			title: "结算方式",
			width: 78,
			template: kendo.template("账户余额")
		},
		{
			field: "ShipName",
			title: "应收款",
			width: 78,
			template: kendo.template("1290.00")
		},
		{
			field: "ShipName",
			title: "客户名称",
			width: 78,
			template: kendo.template("航班管家")
		},
		{
			field: "ShipName",
			title: "乘机人",
			width: 78,
			template: kendo.template("毛小病")
		},
		{
			field: "ShipCity",
			title: "订单来源",
			width: 150,
			template: kendo.template("后台建单")
		},
		{
			field: "ShipName",
			title: "供应商名称",
			width: 78,
			template: kendo.template("不夜城BSP")
		}]
	});
});

