function load_header() {
	$.get(encodeURI("/运营商/同步头.html"), {},
	function(data) {
		$(".header").html(data);
		init_header();
	});
}

var cookiepre = '';

function setcookie(cookieName, cookieValue, seconds, path, domain, secure) {
	var expires = new Date();
	if(cookieValue == '' || seconds < 0) {
		cookieValue = '';
		seconds = -2592000;
	}
	expires.setTime(expires.getTime() + seconds * 1000);
	domain = !domain ? cookiedomain : domain;
	path = !path ? cookiepath : path;
	document.cookie = escape((cookiepre ? cookiepre : '') + cookieName) + '=' + escape(cookieValue)
		+ (expires ? '; expires=' + expires.toGMTString() : '')
		+ (path ? '; path=' + path : '/')
		+ (domain ? '; domain=' + domain : '')
		+ (secure ? '; secure' : '');
}

function getcookie(name, nounescape) {
	name = (cookiepre ? cookiepre : '') + name;
	var cookie_start = document.cookie.indexOf(name);
	var cookie_end = document.cookie.indexOf(";", cookie_start);
	if(cookie_start == -1) {
		return '';
	} else {
		var v = document.cookie.substring(cookie_start + name.length + 1, (cookie_end > cookie_start ? cookie_end : document.cookie.length));
		return !nounescape ? unescape(v) : v;
	}
}

$(function() {
    /** 返回顶部 */
    /*
    var offset = $("#container").offset();
    var w = $("#container").width();
    */
	var $backToTopTxt = "返回顶部",
	$backToTopEle = $('<a class="backToTop"></a>').appendTo($("#container")).text($backToTopTxt).attr("title", $backToTopTxt).click(function() {
		$("html, body").animate({
			scrollTop: 0
		},
		120);
	}),
	$backToTopFun = function() {
		var st = $(document).scrollTop(),
		winh = $(window).height();
		(st > 800) ? $backToTopEle.show() : $backToTopEle.hide();
		//IE6下的定位
		if (!window.XMLHttpRequest) {
			$backToTopEle.css("top", st + winh - 166);
		}
	};
	$(window).bind("scroll", $backToTopFun);
    /*
    $(window).bind("resize", function(){
        var offset = $("#container").offset();
        var w = $("#container").width();
        $backToTopEle.css({left:offset.left + w + 20}); 
    });
    */
	$(function() {
		$backToTopFun();
	});

    /** 返回顶部结束 */

	if (typeof(init_header) == "undefined") {
		$.getScript("/static/js/header.js", function() {
			load_header();
		});
	} else {
		load_header();
	}



    var screen_change = function(p){
        if(p == "fullscreen"){
            $("#container").css("width","100%");
            setcookie("fs","fullscreen",86400,"/",location.host);
        }else{
            $("#container").css("width","980");
            setcookie("fs","",-1,"/",location.host);
        }
    }
    if(getcookie("fs") == "fullscreen"){
        screen_change("fullscreen");
    }else{
        screen_change("");
    }


    /* 注册一个切换全屏的快捷建 */
    $("body").keydown(function(e){
        if(e.altKey && e.keyCode == 13){
            if(/980/.test($("#container").css("width"))){
                screen_change("fullscreen");
            }else{
                screen_change("");
            }
        }
    });
});

