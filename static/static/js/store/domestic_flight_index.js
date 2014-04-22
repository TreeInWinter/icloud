/* 国际机票首页 */
$(function() {

	$("input[name='passenger_type']").click(function() {
		if ($(this).val() == "child") {
			$("#child_extra").show();
		} else {
			$("#child_extra").hide();
		}

	});
	$("input[name='with_type']").click(function() {

        switch($(this).val()){
            case 'without_adult':
                $("#adult_pnr").hide();
                $("#adult_order_id").hide();
                break;
            case 'adult_pnr':
                $("#adult_pnr").show();
                $("#adult_order").hide();
                break;
            case 'adult_order':
                $("#adult_pnr").hide();
                $("#adult_order").show();
                break;
            default:
                break;
        }
    });

	$("input[name='flight_type']").click(function() {
        switch($(this).val()){
            case 'one_way':
                $("#return_label label").addClass("muted");
                $("#return_date").prop("disabled",true);
                break;
            case 'round_trip':
                $("#return_label label").removeClass("muted");
                $("#return_date").prop("disabled",false);

                break;
            default:
                break;
        }
    });
});

