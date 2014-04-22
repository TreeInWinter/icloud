$(function() {
	//航空公司
	var selc = $(".sel_airlines").kendoComboBox({
		filter: "contains",
		suggest: true,
		index: 3
	});

	$(".sel_airlines").on('focus', function() {
		var combobox = $("#sel_airlines").data("kendoComboBox");
		combobox.open();
	});

    $('body').delegate('#trip_type','change',function(){
        console.log($(this).val());
        switch($(this).val()){
            case 'one_way':
                $(".xchange").show();
                $("#return_label").addClass("muted");
                $("#return_date").prop("disabled", true);
                $("#one_and_round").show();
                $("#muti_trip").hide();
                break;
            case 'round_trip':
                $(".xchange").show();
                $("#return_label").removeClass("muted");
                $("#return_date").prop("disabled", false);
                $("#one_and_round").show();
                $("#muti_trip").hide();
                break;
            case 'muti_trip':
                $(".xchange").hide();
                $("#return_label").removeClass("muted");
                $("#return_date").prop("disabled", false);

                $("#one_and_round").hide();
                $("#muti_trip").show();
                break;
            default:
                break;

        }

    });

	function createCityAc(ele) {
		CityAutocomplete({
            css : {"z-index":100},
			template: "#city_popup",
			input: ele,
			width: 200,
			// autocomplete:{
			//     dataSource:{
			//         transport:{
			//             read:{
			//             } 
			//         } 
			//     },
			//     placeholder:'三字吗/城市'
			// },
			// 以@分割热门非热门，以;分割条目，以|分割三字码拼音和中文
			url: '/static/js/data.txt',
			group: ["热门", "GHJ", "ABCDEF", "KLMN", "PQSTW", "XYZ"]
		});
	}

    function init_datepicker(){
        $(this).datepicker({
            css : {"z-index": 20000},
			showDay:true,
			numberOfMonths:[1,2],
			minDate :new Date(),
			firstDay:0,
			showButtonPanel :true
		});
    }

	createCityAc(".city_ac");


    $('.ac-add-line').bind('click',function(){
        var container = $('.muti_trip tbody');
        var html = kendo.template($('#ac-add-line-template').html())({});
        container.append(html);
        reorder();
        createCityAc(container.find('tr').last().find('.city_ac'));
        init_datepicker.call(container.find("tr").last().find(".datepicker"));
    });

    $('body').delegate('.ac-remove-line','click',function(){
        var t = $(this);
        if (t.closest('table').find('tr').length <= 2){
            return ;
        }
        t.closest('tr').remove();
        reorder();
    });

    function reorder(){
        $('.index_num').each(function(i){
            var t = $(this);
            t.text(i+1);
        }); 
    }
});

