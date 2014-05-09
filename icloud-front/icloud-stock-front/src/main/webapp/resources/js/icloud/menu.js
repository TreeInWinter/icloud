function menuloading() {
	$("#sidebar-menu > ul > li").hover(
			function() {
				$(this).addClass("selected");
				$(".menu-panel", this).addClass("selected");
				if ($("input[name=menu-id]", this).length > 0) { // 如果存在,
					// 则进行删除哦
					var val = $("input[name=menu-id]", this).val();
					$("input[name=menu-id]", this).remove();
					/**
					 * 进行删除,并进行贴上加载按钮
					 */
					$(".menu-panel", this).append(
							"<div class=\"loading\"><p>数据加载中,请稍候…</p></div>");
					var show = $(".menu-panel", this);
					/**
					 * 进行访问
					 */
					$.ajax({
						url : basepath + '/stock/getStockMenu',
						type : 'post',
						dataType : 'html',
						data : {
							"id" : val
						},
						complete : function() {
						},
						success : function(result) {
							show.html("");
							show.append(result);
						}
					});
				} else {

				}

			}, function() {
				$(this).removeClass("selected");
				$(".menu-panel", this).removeClass("selected");
			});
}

$(document).ready(function() {
	menuloading();
});
