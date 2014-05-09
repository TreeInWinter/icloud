function stockListloading(cateId, pageNo) {
	setLoader();
	/**
	 * 进行访问
	 */
	$.ajax({
		url : basepath + '/stock/listStockView',
		type : 'post',
		dataType : 'html',
		data : {
			"cateId" : cateId,
			"pageNo" : pageNo
		},
		complete : function() {
		},
		success : function(result) {
			 $("#stock-list-view").html(result);
			 hideLoader();
		}
	});

}

function setLoader() {
	$("#loader").css("display", "block");
}

function hideLoader() {
	$("#loader").css("display", "none");
}

$(document).ready(function() {
	stockListloading(885, 0);
});
