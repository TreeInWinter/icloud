$(document).ready(function() {
	$("#menu").kendoMenu({
		direction:'default' //该参数可控制 子菜单弹出的方向,  left, right, top ,top right,top left 
	});
	
	$("select").kendoDropDownList();
	$(".Datepicker").kendoDatePicker({format: "yyyy-MM-dd",culture:"zh-CN"});
	
	$(".Timepicker").kendoTimePicker({format: "HH",culture:"zh-CN"});
	
	$(".tabstrip").kendoTabStrip({ animation: { open: { effects: 'toggle' } } });
	
});