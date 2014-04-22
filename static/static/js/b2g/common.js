var b2g = {};
$(function() {
    /*
    var offset = $(".main-body").eq(0).offset();
    var w = $(".main-body").eq(0).width();
    */

    var $backToTopTxt = "返回顶部", $backToTopEle = $('<a class="backToTop"></a>').appendTo($("body"))
    .text($backToTopTxt).attr("title", $backToTopTxt).click(function() {
            $("html, body").animate({ scrollTop: 0 }, 120);
    }), $backToTopFun = function() {
        var st = $(document).scrollTop(), winh = $(window).height();
        (st > 0)? $backToTopEle.show(): $backToTopEle.hide();    
        //IE6下的定位
        if (!window.XMLHttpRequest) {
            $backToTopEle.css("top", st + winh - 166);
//            $advice.css("top", st + winh - 166);
        }
    };//$advice=$('<a class="advice"></a>').appendTo($("body"));

    var backTimer = null;
    var i = 0;

    $backToTopEle.bind("mouseenter.myAnimate",function(){
        var that = $(this);
        if(backTimer){
            clearInterval(backTimer);
        }

        backTimer = setInterval(function(){
            that.css("background-position","0px -" + (i % 3 + 1) * 62 + "px");
            i++;
        },200);
    });

    $backToTopEle.bind("mouseleave.myAnimate",function(){
        i = 0;
        $(this).css("background-position","0px 0px");

        if(backTimer){
            clearInterval(backTimer);
        }
    });
    
    $(window).bind("scroll", $backToTopFun);
    /*
    $(window).bind("resize", function(){
        var offset = $(".main-body").eq(0).offset();
        var w = $(".main-body").eq(0).width();
        $backToTopEle.css({left:offset.left + w + 20});
        $advice.css({left:offset.left + w + 20});
    });
    */
    $(function() { $backToTopFun(); });


    var dpEle = $(".datepicker");
    initDatePicker(dpEle);

		
    if(typeof(kendo) != "undefined"){
        kendo.init($("body"));
    }


    $("body").delegate(".toggle_trigger","click",function(){
        var that = $(this),
            t = that,
            textFilter = t, 
            temp,
            i,j,
            noslide,
            tags = ['th','tr','td','table','tbody','thead','tfooter'];

        if(that.attr("data-toggle")){
            o = $.parseJSON(that.attr("data-toggle"));
        }else{
            o = $.parseJSON(that.attr("toggle"));
        }

        if(o.filer){
            t = $(o.filter,that);
        }

        if(o.textFilter) {
            textFilter = $(o.textFilter,that); 
        }

        temp = $(o.target).get(0);
        noslide = false;

        for(i = 0, j = tags.length; i < j; i++){
            if(temp && (temp.tagName.toLowerCase() == tags[i])){
                noslide = true;
                break;
            }
        }

        if(noslide){
            if(o.wrapper){
                $(o.target,that.closest(o.wrapper)).toggle();
            }else{
                $(o.target).toggle();
            }

        }else{
            if(o.wrapper){
                $(o.target,that.closest(o.wrapper)).slideToggle("normal");
            }else{
                $(o.target).slideToggle("normal");
            }
        }

        /* 折叠 toggle_trigger */
        if(o.toggleSelfClass){
            if(typeof(o.toggleSelfClass) == "string"){
                that.toggleClass(o.toggleSelfClass);
            }else{
                if(that.hasClass(o.toggleSelfClass[0])){
                    that.removeClass(o.toggleSelfClass[0]).addClass(o.toggleSelfClass[1]);
                }else{
                    that.removeClass(o.toggleSelfClass[1]).addClass(o.toggleSelfClass[0]);
                }
            }
        }

        if(o.toggleParentClass){

        }

        /* 折叠触发者 */
        if(o.toggleClass){
            if(typeof(o.toggleClass) == "string"){
                t.toggleClass(o.toggleClass);
            }else{
                if(t.hasClass(o.toggleClass[0])){
                    t.removeClass(o.toggleClass[0]).addClass(o.toggleClass[1]);
                }else{
                    t.removeClass(o.toggleClass[1]).addClass(o.toggleClass[0]);
                }
            }
        }

        /* 切换触发者文本 */
        if(o.toggleText){
            if(textFilter.html() == o.toggleText[0]){
                textFilter.html(o.toggleText[1]);
            }else{
                textFilter.html(o.toggleText[0]);
            }
        }
    });


    $(".header-bar-content label").click(function(){
        $(".header-bar-content .for_reason").trigger("click");
    });
    $(".header-bar-content .for_reason").click(function(e){
        if($(".reason_checkbox",this).hasClass("checked")){
            $("#settour").removeClass("setuser-invalid").addClass("setuser");
            $(".reason_checkbox",this).removeClass("checked");
            $("input[name=reason]",this).prop("checked",false);
        }else{
            $("#settour").removeClass("setuser").addClass("setuser-invalid");
            $(".reason_checkbox",this).addClass("checked");
            $("input[name=reason]",this).prop("checked",true);
        }
    });

});

function initDatePicker(dpEle)
{
    if(typeof($.datepicker) != "undefined"){
        $.datepicker.regional[ "zh-CN" ];

        var dpSetting = {
            css : {"z-index": 20000},
            showDay:true,
            numberOfMonths:[1,2],
            minDate :new Date(),
            firstDay:0,
            showButtonPanel :true,
            monthNames:['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
        };

        var dpSettingUlt = {
            css : {"z-index": 20000},
            showDay:true,
            numberOfMonths:[1,2],
            firstDay:0,
            showButtonPanel :true
        };

        var dpSettingShort = {
            css : {"z-index": 20000},
            numberOfMonths:[1,2],
            firstDay:0,
            showButtonPanel :true
        };

        var dpSettingSingle = {
            css : {"z-index": 20000},
            numberOfMonths:[1,1],
            firstDay:0,
            showButtonPanel :true
        };

        var dpSettingChangeMonthYear={
            css : {"z-index": 20000},
            numberOfMonths:[1,1],
            firstDay:0,
            changeMonth:true,
            changeYear:true,
            showButtonPanel :true,
            minDate:new Date("1900-01-01"),
            maxDate:new Date("2050-01-01"),
            beforeShow:function(el,inst){
                var minDate=$(el).attr("minDate")?new Date($(el).attr("minDate")):inst.settings.minDate;
                var maxDate=$(el).attr("maxDate")?new Date($(el).attr("maxDate")):inst.settings.maxDate;
                changeMinMaxYear(minDate.getFullYear(),maxDate.getFullYear());
            },
            onSelect:function(){
                fl_datepicker_monthSelect.close();
                fl_datepicker_yearSelect.close();
            }
        }

        b2g.dpSetting = dpSetting;
        b2g.dpSettingUlt = dpSettingUlt;
        b2g.dpSettingShort = dpSettingShort;
        b2g.dpSettingSingle = dpSettingSingle;
        b2g.dpSettingChangeMonthYear=dpSettingChangeMonthYear;




        $.each(dpEle,function(){
            var item = $(this);

            if( item.hasClass("dpUlt") ){
                item.datepicker(dpSettingUlt);
            }else if(item.hasClass("dpShort")){
                item.datepicker(dpSettingShort);
            }else if(item.hasClass("dpSingle")){
                item.datepicker(dpSettingSingle);
            }else if(item.hasClass("dpChangeMonthYear")){
                item.datepicker(dpSettingChangeMonthYear);
                if(window["fl_datepicker_yearSelect"]==null)
                {
                    var datepicker_monthSelectTemplate='<div>\
                                                            <div class="datepicker-monthSelectPanel">\
                                                                <ul>\
                                                                    <li data-value="0">一月</li>\
                                                                    <li data-value="1">二月</li>\
                                                                    <li data-value="2">三月</li>\
                                                                    <li data-value="3">四月</li>\
                                                                    <li data-value="4">五月</li>\
                                                                    <li data-value="5">六月</li>\
                                                                </ul>\
                                                                <ul>\
                                                                    <li data-value="6">七月</li>\
                                                                    <li data-value="7">八月</li>\
                                                                    <li data-value="8">九月</li>\
                                                                    <li data-value="9">十月</li>\
                                                                    <li data-value="10">十一月</li>\
                                                                    <li data-value="11">十二月</li>\
                                                                </ul>\
                                                                <div class="clearfix"></div>\
                                                            </div>\
                                                        </div>';
                    var datepicker_yearSelectTemplate='<div>\
                                                            <div class="datepicker-yearSelectPanel">\
                                                                <ul>\
                                                                # for (var i=minY; i<=maxY;i++) { # \
                                                                    <li data-value="#= i #">#= i #</li>  \
                                                                # } #\
                                                                </ul>\
                                                        </div></div>';

                    //datepicker修改
                    $("body").delegate(".dpChangeMonthYear","click",function(){
                        $("body").data("currentDP",this);
                    })

                    var currentY=new Date().getFullYear();

                    fl_datepicker_monthSelect=FloatLayer({
                        css:{padding:0,zIndex:99999},
                        trigger:".ui-datepicker-month-ipt",
                        template:datepicker_monthSelectTemplate
                    })
                    fl_datepicker_yearSelect=FloatLayer({
                        css:{padding:0,zIndex:99999},
                        trigger:".ui-datepicker-year-ipt",
                        template:datepicker_yearSelectTemplate,
                        data:{minY:currentY-100,maxY:currentY+50}
                    })

                    $("body").delegate(".ui-datepicker-month-ipt","change",function(){
                        if(/^(\d|1[0-1])$/.test($(this).val()))
                        {
                            $.data($("body").data("currentDP"),"datepicker").drawMonth=parseInt($(this).val())-1;
                            $($("body").data("currentDP")).datepicker("refresh");
                            fl_datepicker_monthSelect.close();
                            return false;
                        }
                        else
                        {
                            alert("error!");
                        }
                    })
                    $("body").delegate(".ui-datepicker-year-ipt","change",function(){
                        if(/^(19|20)\d{2}$/.test($(this).val()))
                        {
                            $.data($("body").data("currentDP"),"datepicker").drawYear=parseInt($(this).val());
                            $($("body").data("currentDP")).datepicker("refresh");
                            fl_datepicker_monthSelect.close();
                            return false;
                        }
                        else
                        {
                            alert("error!");
                        }
                    })

                    $("body").delegate(".datepicker-monthSelectPanel,.datepicker-yearSelectPanel","mousedown",function(){
                        return false;
                    })

                    $("body").delegate(".datepicker-monthSelectPanel li","click",function(){
                        $.data($("body").data("currentDP"),"datepicker").drawMonth=$(this).attr("data-value");
                        $($("body").data("currentDP")).datepicker("refresh");
                        fl_datepicker_monthSelect.close();
                        return false;
                    })

                    $("body").delegate(".datepicker-yearSelectPanel li","click",function(){
                        $.data($("body").data("currentDP"),"datepicker").drawYear=$(this).attr("data-value");
                        $($("body").data("currentDP")).datepicker("refresh");
                        fl_datepicker_yearSelect.close();
                        return false;
                    })

                    fl_datepicker_yearSelect.show();

                    $(".datepicker-yearSelectPanel").jScrollPane({
                        showArrows:true
                    });

                    fl_datepicker_yearSelect.hide();
                }
            }else{
                item.datepicker(dpSetting);
            }
        });
    }
}

function changeMinMaxYear(minY,maxY){
    fl_datepicker_yearSelect.data({minY:minY,maxY:maxY})
    fl_datepicker_yearSelect.show();

    $(".datepicker-yearSelectPanel").jScrollPane({
        showArrows:true
    });

    fl_datepicker_yearSelect.hide();
}

