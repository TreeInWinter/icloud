/** 
 * window.onresize 事件 专用事件绑定器 v0.1 Alucelx 
 * http://www.cnblogs.com/Alucelx/archive/2011/10/20/2219263.html 
 * <description> 
 * 用于解决 lte ie8 & chrome 及其他可能会出现的 原生 window.resize 事件多次执行的 BUG. 
 * </description> 
 * <methods> 
 * add: 添加事件句柄 
 * remove: 删除事件句柄 
 * </methods> 
 */ 
var onWindowResize = function(){ 
    //事件队列 
    var queue = [], 
        indexOf = Array.prototype.indexOf || function(){ 
            var i = 0, length = this.length; 
            for( ; i < length; i++ ){ 
                if(this[i] === arguments[0]){ 
                    return i; 
                } 
            } 
            return -1; 
        }; 
    var isResizing = {}, //标记可视区域尺寸状态， 用于消除 lte ie8 / chrome 中 window.onresize 事件多次执行的 bug 
        lazy = true, //懒执行标记 
        listener = function(e){ //事件监听器 
            var h = window.innerHeight || (document.documentElement && document.documentElement.clientHeight) || document.body.clientHeight, 
            w = window.innerWidth || (document.documentElement && document.documentElement.clientWidth) || document.body.clientWidth; 
            if( h === isResizing.h && w === isResizing.w){ 
                return; 
            }else{ 
                e = e || window.event; 
                var i = 0, len = queue.length; 
                for( ; i < len; i++){ 
                    queue[i].call(this, e); 
                } 
                isResizing.h = h, 
                    isResizing.w = w; 
            } 
        } 
    return { 
        add: function(fn){ 
                 if(typeof fn === 'function'){ 
                     if(lazy){ //懒执行 
                         if(window.addEventListener){ 
                             window.addEventListener('resize', listener, false); 
                         }else{ 
                             window.attachEvent('onresize', listener); 
                         } 
                         lazy = false; 
                     } 
                     queue.push(fn); 
                 }else{ } 
                 return this; 
             }, 
        remove: function(fn){ 
                    if(typeof fn === 'undefined'){ 
                        queue = []; 
                    }else if(typeof fn === 'function'){ 
                        var i = indexOf.call(queue, fn); 
                        if(i > -1){ 
                            queue.splice(i, 1); 
                        } 
                    } 
                    return this; 
                } 
    }; 
}.call(this); 



function FixCol(target,userOpts) {
	if (typeof(target) == "undefined") {
		return;
	}

	var defaults = {
        fixClassName: "fixcol",
        fixLineName : "fixline",
        offsetName: "offset",
		kendoGrid: false,
        xchange : true,
        autoWrap: false,
        lastChangeIndex: 0,
        lineAddUp: -60
	},
	opts,
	t = target,
	wrapper,
	thead,
	tbody,
	table;

    var e = document.createElement("DIV"),
        c = {
            haspointerlock: "pointerLockElement" in document || "mozPointerLockElement" in document || "webkitPointerLockElement" in document
        };
        c.isie = "all" in document && "attachEvent" in e && !c.isopera;
        c.isieold = c.isie && !("msInterpolationMode" in e.style);
        c.isie7 = c.isie && !c.isieold && (!("documentMode" in document) || 7 == document.documentMode);
        c.isie8 = c.isie && "documentMode" in document && 8 == document.documentMode;
        c.isie9 = c.isie && "performance" in window && 9 <= document.documentMode;
        c.isie10 = c.isie && "performance" in window && 10 <= document.documentMode;
        c.ischrome = "chrome" in window;

	if (typeof(t.sender.wrapper) != "undefined") {
		opts = {
			kendoGrid: true
		};

		/* kendo grid */
		wrapper = $(t.sender.wrapper);
		thead = $(t.sender.thead);
		tbody = $(t.sender.tbody);
		table = $(t.sender.table);
        grid_content = $(table).closest(".k-grid-content");
	} else {
		/* default table */
		wrapper = $(t);
		thead = $("table > thead", wrapper);
		tbody = $("table > tbody", wrapper);
		table = $("table", wrapper);
	}

	opts = $.extend(defaults, opts, userOpts);

	if (opts.kendoGrid && ! t.sender.options.scrollable) {
        //grid table scrollable 为 false

        if(c.isie8){
            opts.lineAddUp += 30; 
        }else{
            opts.lineAddUp += 18; 
        }
	}



    if(c.isieold || c.isie7){
        if(opts.xchange){
            opts.lastChangeIndex = opts.lastChangeIndex < 0 ? 0 : opts.lastChangeIndex;
            var fields =  $("tr:eq(0) th",thead).size();
            opts.lastChangeIndex = opts.lastChangeIndex > fields ? fields : opts.lastChangeIndex; 


            var colgroup = $("colgroup",table);

            if(colgroup){
                $("colgroup",thead).each(function(){
                    var col = $("col:last",this);
                    col.insertBefore($("col:eq(" + opts.lastChangeIndex  + ")", this));
                });
                $("colgroup",tbody).each(function(){
                    var col = $("col:last",this);
                    col.insertBefore($("col:eq(" + opts.lastChangeIndex  + ")", this));
                });
            }

            $("tr",thead).each(function(){
                var o = $("th:last",this);
                var ins = $("th:eq(" + opts.lastChangeIndex + ")", this).css({"border-left-width":1});
                o.insertBefore(ins);
            });
            $("tr",tbody).each(function(){
                var o = $("td:last",this);
                o.insertBefore($("td:eq(" + opts.lastChangeIndex + ")", this));
            });
        }

        this.startFixed = function(){
            //empty;
        };

        this.stopFixed = function(){
            //empty;
        };

        this.resizeFixed = function(){
            //empty;
        };

        return this;
    }else{
        $("colgroup",wrapper).each(function(){
            $("col:last",this).css({width:"auto"});
        });
    }

	/* 容器宽度 */
	var w = wrapper.width();

    var getDocScroll = function(){
        return {top:$(document).scrollTop(),left: $(document).scrollLeft()};
    };



    /*
    var autoWrap = function(obj){
        if(c.isieold || c.isie7){
            opts.autoWrap = true;
        }

        if(!this.hasWrap && opts.autoWrap){
            var wrapDiv = $('<div class="fixwrap"></div>');
            var child = obj.children();
            if(child.length){
                obj.append(wrapDiv.append(child));
            }else{
                obj.html(wrapDiv.html(obj.html()));
            }
        }else{
            obj.addClass(opts.fixClassName);
        }
    };
    */

	var setFixed = function() {
        var th = $("tr:eq(0) th:last", thead),
            th_offset = th.offset(), 
            d = getDocScroll(),
            p = wrapper.offset(),
            last_w ,
            l = 0,
            st = 0;


		$("tr", tbody).each(function(index) {
			var td = $("td:last", this),
                td_offset = td.offset();

            //autoWrap(td);

            td.addClass(opts.fixClassName);

            if(!last_w){
                last_w = td.outerWidth();
            }

            l = p.left + w - last_w;
            td.css({
                left: l,
                top:td_offset.top - d.top,
                zIndex: index + 1
            }).data(opts.offsetName,{left: l,top:td_offset.top});
		});

        //autoWrap(th);
        //this.hasWrap = true;

        th.css({
            left:l,
            top:th_offset.top - d.top,
            zIndex:0
        }).data(opts.offsetName,{left: l,top:th_offset.top});

        th.addClass(opts.fixClassName);

        if(!this.vline){
            this.vline = $('<div class="' + opts.fixLineName + '"></div>');
            wrapper.append(vline);
        }

        this.vline.css({
            left:l,
            zIndex:100,
            top: th_offset.top - d.top,
            height: wrapper.height() + opts.lineAddUp
        });

	}

    var lightReFixed = function(){
        var d = getDocScroll();
        var firstTop ;
        $("." + opts.fixClassName).each(function(){
            var a = $(this).data(opts.offsetName);

            if(!firstTop){
                firstTop = a.top - d.top;
            }

            $(this).stop().animate({
                left : a.left - d.left,
                top : a.top - d.top
            },20);

            /*
            $(this).css({
                left : a.left - d.left,
                top : a.top - d.top
            });
            */
        });
        this.vline.css({top: firstTop}); 
    };

	var unFixed = function() {
        $("." + opts.fixClassName).removeClass(opts.fixClassName).css({left:0});
	}

    /**
     * 外部函数
     */
    this.resizeFixed = function(){
        unFixed();
        if(grid_content){
            grid_content.scrollLeft(0);
        }else{
            wrapper.scrollLeft(0);
        }

        setFixed();
    };

    this.startFixed = setFixed; 
    this.stopFixed = unFixed;
    this.resizeFixed = resizeFixed;

    $(window).bind("scroll", lightReFixed);
    onWindowResize.add(resizeFixed);

    return this;
}

