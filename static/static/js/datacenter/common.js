var g = {};
$(function() {
   
	
    if(typeof($.datepicker) != "undefined"){
        $.datepicker.regional[ "zh-CN" ];

        var dpSetting = {
            css : {"z-index": 20000},
            showTomorrow:true,
			numberOfMonths:[1,2],
			minDate :new Date(),
			firstDay:0,
			showButtonPanel :true,
            monthNames:['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
		};

        var dpSettingUlt = {
            css : {"z-index": 20000},
            showTomorrow:true,
			numberOfMonths:[1,2],
			firstDay:0,
			showButtonPanel :true
		};

        var dpSettingShort = {
            css : {"z-index": 20000},
            showTomorrow:true,
			numberOfMonths:[1,2],
			firstDay:0,
			showButtonPanel :true
		};


        g.dpSetting = dpSetting;
        g.dpSettingUlt = dpSettingUlt;
        g.dpSettingShort = dpSettingShort;


        var dpEle = $(".datepicker");

        $.each(dpEle,function(){
            var item = $(this);

            if( item.hasClass("dpUlt") ){
                item.datepicker(dpSettingUlt);
            }else if(item.hasClass("dpShort")){
                item.datepicker(dpSettingShort);
            }else{
                item.datepicker(dpSetting);
            }
        });
    }
		
    if(typeof(kendo) != "undefined"){
        kendo.init($("body"));
    }

});

