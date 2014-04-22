var funcDetail = function(orderId){
	if (!$("#roleDetail").data("kendoWindow")) {
		$("#roleDetail").kendoWindow({
			width: "600px",
			minHeight: "800px",
			title: "设置权限",
			actions: ["Refresh", "Close"],
			content: encodeURI("功能菜单树.html")  + "?orderID=" + orderId + "&timestamp=" + (new Date()).toString(),
			visible: false,
			modal:true
		}).data("kendoWindow").open().center();
	}else{
		$("#roleDetail").data("kendoWindow").open().center();
	}
};


$(function(){
	
	$("input[type=number]").kendoNumericTextBox({format: "#"})
	
	$("#roleList").kendoGrid({
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
				title: "角色编号",
				width:100
			},
			{
				field: "ShipCity",
				title: "角色名称",
				width: 150
			},
			{
				field: "ShipName",
				title: "角色描述",
				width: 260
			}, 
			{
				field: "OrderDate",
				title: "创建日期",
				width: 120,
				format: "{0:yyyy-MM-dd}"
			},
			{
				field: "OrderID",
				title: "操作",
				width: 120,
				template: kendo.template($("#roleButton").html())
			}
		]
	});
});