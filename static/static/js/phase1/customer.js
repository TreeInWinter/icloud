var customerDetail = function(id){
	if (!$("#customerDetail").data("kendoWindow")) {
		$("#customerDetail").kendoWindow({
			width: "1000px",
			minHeight: "1200px",
			title: "采购商",
			actions: ["Refresh", "Close"],
			content: encodeURI("采购商详情.html") + "?customerId=" + id + "&timestamp=" + (new Date()).toString(),
			visible: false,
			modal:true
		}).data("kendoWindow").open().center();
	}else{
		$("#customerDetail").data("kendoWindow").open().center();
	}
};
	

$(function(){
	
	$("input[type=number]").kendoNumericTextBox({format: "#"})
	
	$("#customerList").kendoGrid({
		dataSource: {
			type: "odata",
			transport: {
				read: "http://demos.kendoui.com/service/Northwind.svc/Orders"
			},
			schema: {
			    model: {
				fields: {
						OrderID: { type: "number" },
						Freight: { type: "number" },
						ShipName: { type: "string" },
						OrderDate: { type: "date" },
						ShipCity: { type: "string" }
					}
			    }
			},
			pageSize: 30,
			serverPaging: true,
			serverFiltering: true,
			serverSorting: true
		},
		height: 1000,
		sortable: true,
		pageable: true,
		resizable: true,
		selectable: "single",
		dataBound: function() {
			//
		},
		columns: [
			{
				field:"OrderID",
				title: "客户编号",
				width:100,
				template:"<a href=\"javascript:customerDetail('#=OrderID#');\">#=OrderID#</a>"
			},
			{
				field: "OrderDate",
				title: "创建日期",
				width: 120,
				format: "{0:yyyy-MM-dd}"
			}, 
			{
				field: "ShipName",
				title: "客户名称",
				width: 260
			}, {
				field: "ShipCity",
				title: "来源城市",
				width: 150
			}
		]
	});
});