$.fn.fixedBar = function(args){
    var defaults = {
        fixed:false,  //是否始终固定位置
        css:"position:fixed;top:0;",
        endAt:0,
        createShadow:'fixBarShadow'
    };

    var callback,opts;

    if(this.length==0) return;

    if( Object.prototype.toString.call(args) == "[object Function]" ){
        callback = args; 
    }

    opts = $.extend(defaults, args); 

    if (window.ActiveXObject) {
        window.isIE = window[window.XMLHttpRequest ? 'isIE7' : 'isIE6'] = true;
    }

    if (window.isIE6) try {document.execCommand("BackgroundImageCache", false, true);} catch(e){}

    var ele = $(this).length > 1 ? $(this).eq(0) : $(this);

    function init(){
        var eleOffsetTop = ele.offset().top;
        var elePositionTop = ele.position().top;
        var endPos;
        var shadow;

        if(opts.endAt){
            if(typeof opts.endAt === 'number'){
                endPos = opts.endAt;
            }else{
                endPos = $(opts.endAt).offset().top + $(opts.endAt).height();
            }
        }

        if(opts.createShadow){
            if(typeof opts.createShadow === 'string'){
                shadow = $(opts.createShadow).length ? $(opts.createShadow) : $('<div class="'+opts.createShadow+'" />').css({
                    display:'none',
                    height:ele.outerHeight(true)+'px'
                });
            }
            ele.before(shadow);
        }

        if(opts.fixed){
            eleOffsetTop = -1; 
            if(!ele.hasClass("fixedBar")) ele.addClass("fixedBar").attr("style",opts.css);
            if(window.isIE6) ele.css({"position":"absolute"});
        }

        $(window).bind("scroll.fixedBar",function(e){
            var that = $(this);
            var scrollTop = that.scrollTop();
            
            if(ele.data('disabled') !== true){
                if(scrollTop > eleOffsetTop){
                    if(!ele.hasClass("fixedBar")){
                        shadow.show();
                        ele.addClass("fixedBar").attr("style",opts.css);
                    }
                    if(window.isIE6) ele.css({"top":scrollTop - eleOffsetTop + elePositionTop + "px","position":"absolute"});
                }else{
                    shadow.hide();
                    ele.removeClass("fixedBar").removeAttr("style");
                }
            }

            if(callback) callback.call(ele,scrollTop);

            if(opts.endAt){
                if(scrollTop >= endPos){
                    shadow.hide();
                    ele.removeClass("fixedBar").removeAttr("style").data('disabled',true);
                }else{
                    ele.removeData('disabled'); 
                }
            }
        }); 
    }

    init();

    return this;
};
