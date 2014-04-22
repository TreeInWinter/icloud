$(function(){
	
	$("input[name=office],input[name=orderOffice]").kendoAutoComplete({
		dataTextField: "ProductName",
		filter: "contains",
		minLength: 3,
		dataSource: {
			type: "odata",
			serverFiltering: true,
			serverPaging: true,
			pageSize: 20,
			transport: {
				read: "http://demos.kendoui.com/service/Northwind.svc/Products"
			}
		}
	});
	
})