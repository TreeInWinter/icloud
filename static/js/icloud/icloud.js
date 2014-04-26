/** 公共 js 文件 */
function backToTopWind() {
    /*
    var offset = $(".main-body").eq(0).offset();
    var w = $(".main-body").eq(0).width();
    */
    var $backToTopTxt = "返回顶部",
        $backToTopEle = $('<a class="backToTop"></a>').appendTo($(".main-body"))
            .text($backToTopTxt).attr("title", $backToTopTxt).click(function () {
                $("html, body").animate({
                    scrollTop: 0
                }, 120);
            }),
        $backToTopFun = function () {
            var st = $(document).scrollTop(),
                winh = $(window).height();
            (st > 250) ? $backToTopEle.show() : $backToTopEle.hide();
            //IE6下的定位
            if (!window.XMLHttpRequest) {
                $backToTopEle.css("top", st + winh - 166);
                //            $advice.css("top", st + winh - 166);
            }
        }; //$advice=$('<a class="advice"></a>').appendTo($("body"));

    $(window).bind("scroll", $backToTopFun);
    /*
    $(window).bind("resize", function(){
        var offset = $(".main-body").eq(0).offset();
        var w = $(".main-body").eq(0).width();
        $backToTopEle.css({left:offset.left + w + 20});
        $advice.css({left:offset.left + w + 20});
    });
    */
    $(function () {
        $backToTopFun();
    });

};

$(function () {
    $("#sidebar-menu > ul > li").hover(function () {
        $(this).addClass("selected");
        $(".menu-panel", this).addClass("selected");
        var bgImg = $(".menu-item", this).css("background-image");
        bgImg = bgImg.replace(".", "-hover.");
        $(".menu-item", this).css("background-image", bgImg);
    }, function () {
        $(this).removeClass("selected");
        $(".menu-panel", this).removeClass("selected");
        var bgImg = $(".menu-item", this).css("background-image");
        bgImg = bgImg.replace("-hover.", ".");
        $(".menu-item", this).css("background-image", bgImg);
    });
});

$(document).ready(function () {
    backToTopWind();
});