var userDetail = function(orderId){
	if (!$("#userDetail").data("kendoWindow")) {
		$("#userDetail").kendoWindow({
			width: "600px",
			minHeight: "600px",
			title: "所属角色列表",
			actions: ["Refresh", "Close"],
			content: encodeURI("角色列表.html")  + "?orderID=" + orderId + "&timestamp=" + (new Date()).toString(),
			visible: false,
			modal:true
		}).data("kendoWindow").open().center();
	}else{
		$("#userDetail").data("kendoWindow").open().center();
	}
};

$(function(){
	$("#userList").kendoGrid({
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
				title: "用户编号",
				width:100,
				template:"<a href=\"javascript:orderDetail('#=OrderID#');\">#=OrderID#</a>"
			},
			{
				field: "ShipName",
				title: "用户名称",
				width: 260
			}, 
			{
				field: "ShipCity",
				title: "用户备注",
				width: 150
			},
			{
				field: "OrderDate",
				title: "创建日期",
				width: 120,
				format: "{0:yyyy-MM-dd}"
			},
			{
				field:"OrderID",
				title: "操作",
				width:100,
				template: kendo.template($("#userButton").html())
			}
		]
	});
});