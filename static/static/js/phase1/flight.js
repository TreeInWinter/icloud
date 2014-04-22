$(document).ready(function() {
	
	$("#adultNumeric").kendoNumericTextBox({format: "#"});
	$("#childNumeric").kendoNumericTextBox({format: "#"});

	
	$("#TripType").change(function(){
		if($(this).val() == 'RoundTrip'){
			$(".returnDate").show();
		}else{
			$(".returnDate").hide();
		}
	})

	var data=$.map(_cityList,function(n){return {"szm":n[0],"name":n[1],"py":n[2],"sx":n[3]}});

	/*
	var citys = [];
	$(_cityList).each(function(){
		citys.push({"szm":this[0],"name":this[1],"py":this[2],"sx":this[3]})
	});*/

	var cityData = {
		dataTextField:"name",
		filter:function(d,f){
				return {
					filters:
							[
								{ignoreCase: true,value:  d,operator: 'startswith',field: 'szm'},
								{ignoreCase: true,value:  d,operator: 'startswith',field: 'py'},
								{ignoreCase: true,value:  d,operator: 'startswith',field: 'sx'},
								{ignoreCase: true,value:  d,operator: 'startswith',field: 'name'}
							],
				logic:"or"
			}
		},
		template:"#:name#",
		dataSource:data,
		select:function(item){
			item.sender.element.val(item.item.text());
			item.preventDefault();
		}

	}
	$("#departureCity").kendoAutoComplete(cityData);
	$("#arrivedCity").kendoAutoComplete(cityData);

});