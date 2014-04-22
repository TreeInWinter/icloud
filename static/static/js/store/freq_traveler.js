$(function() {
	var data = [{
		categories: "北京-上海",
		customers: [{
			name: "白展堂（*1234）",
			id: "1"
		},
		{
			name: "白展堂（*1234）",
			id: "2"
		},
		{
			name: "白展堂（*1234）",
			id: "3"
		}]
	},
	{
		categories: "上海-成都",
		customers: [{
			name: "黑展堂（*1234）",
			id: "4"
		},
		{
			name: "绿展堂（*1234）",
			id: "5"
		},
		{
			name: "白展堂（*1234）",
			id: "6"
		}]
	},
	{
		categories: "上海电信销售部",
		customers: [{
			name: "白展堂（*1234）",
			id: "7"
		},
		{
			name: "蓝展堂（*1234）",
			id: "8"
		},
		{
			name: "白展堂（*1234）",
			id: "9"
		}]
	}];
	var keyup_timeout = null;
	layer = new frequentFlyer({
		template: "#ffcTemplate",
		trigger: "#ffc_input",
		type: "zAutocomplete",
		data: data,
		offsetX: - 1,
		offsetY: 31,
		open: function() {
			$("#ffc_title").addClass("expand")
		},
		close: function() {
			$("#ffc_title").removeClass("expand")
		}
	});

	$("body").delegate(".ul_ffc :checkbox", "change", function() {
		if ($(this).prop("checked")) {
			console.log($(this).val());
		}
		else {

		}
		//return false;
	})

	$("#ffc_input").keyup(function() {
		var that = $(this);
		if (keyup_timeout) {
			clearTimeout(keyup_timeout);
		}
		keyup_timeout = setTimeout(function() {
			ffcSearchData(that)
		},
		200);
	})

	function ffcSearchData(el) {
		var that = el;
		if (that.val().length > 0) {
			if (that.data(that.val())) {
				layer.content(kendo.template($("#ffcsTemplate").html())(that.data(that.val())));
			}
			else {
				var tmpArr = [];
				for (var i = 0; i < data.length; i++) {
					for (var j = 0; j < data[i].customers.length; j++) {
						if (data[i].customers[j].name.indexOf(that.val()) >= 0) {
							tmpArr.push(data[i].customers[j]);
						}
					}
				}
				that.data(that.val(), tmpArr);
				layer.content(kendo.template($("#ffcsTemplate").html())(that.data(that.val())));
			}
		}
		else {
			layer.data(data);
		}
	}
});

