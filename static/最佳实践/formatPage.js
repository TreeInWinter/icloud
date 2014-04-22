;$(function(){
   var $ul = $("<ul></ul>");
   $("body > h1").each(function(index){
       var title = "示例" + (index + 1) + " " + $(this).html();
       $(this).html(title);
       $ul.append("<li><a href=\"#example" + (index + 1) + "\">" + $(this).html() + "</a></li>");
       $("<a name=\"example" + (index + 1) + "\"></a>").insertBefore(this);
   });
   
   $("body").prepend($ul);
   
   
   var $backToTopTxt = "返回顶部", $backToTopEle = $('<div class="backToTop"></div>').appendTo($("body"))
	   .text($backToTopTxt).attr("title", $backToTopTxt).click(function() {
	       $("html, body").animate({ scrollTop: 0 }, 120);
	}), $backToTopFun = function() {
	   var st = $(document).scrollTop(), winh = $(window).height();
	   (st > 0)? $backToTopEle.show(): $backToTopEle.hide();    
	   //IE6下的定位
	   if (!window.XMLHttpRequest) {
	       $backToTopEle.css("top", st + winh - 166);    
	   }
	};
	$(window).bind("scroll", $backToTopFun);
	$(function() { $backToTopFun(); });


});