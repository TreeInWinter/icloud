/*
* Author: shaotian.hu
* Email:  shaotian.hu@travelzen.com
*/

//弹窗封装
function PopWindow(trigger, customSettings, window_id){
    var kendoWinDefaults = {
        visible:false,
        animation:false,
        width:570,
        modal:true,
        autoHide:false,
        closeClassName:'close',
        reload:false,
        isPropagationStopped:false
    }
    this.triggerText = trigger;
    this.trigger = $(trigger);
    this.window_id = window_id;
    if(typeof arguments[0]!='string'){
        // 没有选择器
        customSettings = trigger; 
        this.triggerText = "#POPWINDOW_NULL";
    }
    this.settings = $.extend(kendoWinDefaults,customSettings);
    this.win = null;
};
PopWindow.prototype = {
    init:function(){
        this.render();
        this.bindClick();

        return this.win;
    },
    render:function(){
        var that = this,
            windowEle,
            opts,
            tmpl;

        opts = that.settings;

        if(typeof(opts.template) == "undefined" && typeof(that.window_id) != "undefined"){
            //直接元素弹窗
            that.win = $(that.window_id).kendoWindow( opts ).data('kendoWindow');
            return ;
        }else{
            windowEle = $('<div class="popup-window">');
            windowEle.appendTo($('body'));
        }

        tmpl = $(opts.template).html();

        if ( typeof opts.content === 'undefined'
            && opts.template && tmpl) {
                opts.content = {
                    template : kendo.template(tmpl)(opts.data||{})
                }
        }

        if( typeof opts.content === 'undefined' ){
            throw new Error('PopWindow Error.\n'+that.triggerText + '缺少弹窗内容'+ (opts.template ? ',已初始化template属性为' + opts.template :'') );
        }else{
            that.win = windowEle.kendoWindow( opts ).data('kendoWindow');
            that.win.triggerEle = that.trigger;
            that.win.data = function(d){
                var newcontent =  kendo.template(tmpl)(d);
                that.win.content(newcontent);
            }
        }

        that.win.show = function(){
            kendo.init(that.win.element);
            that.win.center().open(); 
            return that.win;
        }
    },
    bindClick:function(){
        var that = this;
        $('body').delegate(that.triggerText,'click',function(e){
            that.win.target = $(this);
            e.preventDefault();
            if(that.settings.isPropagationStopped)
            {
                e.stopPropagation();
            }
            if(that.settings.reload){
                that.win.refresh();
            }
            that.win.center();
            that.win.open();

            if(that.settings.autoHide){
                setTimeout(function(){
                    that.win.close();
                },that.settings.autoHide);
            }

            kendo.init(that.win.element);
        });

        that.win.element.on('click','.'+that.settings.closeClassName,function(){
            that.win.close();
        });
    }
};

// 表格封装
function GridTable(trigger, customSettings){
    var kendoWinDefaults = {
        scrollable:false,
        pageable:{
            buttonCount:5,
            pageSize: 5,
            messages: {
                display: " 共 <b>{2}</b> 条记录，<b>{0}</b> - <b>{1}</b> 条",
                empty: "共 <b>0</b> 条记录",
                page: "页",
                of: "共 <b>{0}</b>",
                itemsPerPage: "每页",
                first: "第一页",
                previous: "前一页",
                next: "后一页",
                last: "最后页",
                refresh: "刷新"
            }
        }
    }
    this.kendoWinSettings = kendoWinDefaults;
    this.triggerText = trigger;
    this.trigger = $(trigger);
    this.customSettings = customSettings;
    this.grid = null;
};
GridTable.prototype = {
    init:function(){
        var isCreated = false;
        if(this.trigger.attr('role') === 'grid'){
            isCreated = true;
        }

        if(!isCreated){
            this.render();
            return this.grid;
        }
    },
    render:function(){
        var customSettings = this.customSettings || eval('('+this.trigger.data('options')+')');
        var opts;
        opts = $.extend(true, {}, this.kendoWinSettings, customSettings);
        if( typeof opts.dataSource === 'undefined' ){
            throw new Error(this.triggerText + '缺少dataSource属性');
        }else{
            this.grid = this.trigger.kendoGrid( opts ).data('kendoGrid');
        }
    }
};

// 定高函数：超过3行时隐藏
var Collpase = function(opts){
    /*
    * container: Element
    * limitHeight : NUM
    * trigger : Element
    * triggerexp : the className while it is expanded
    * */
    this.limitHeight = 100;
    this.triggerexp = 'col-expp';
    this.duration = 0;
    this.trigger = '.content-collapse';
    $.extend(this,opts);
    this.container = $(this.container);
};
Collpase.prototype = {
    init:function(){
        if(this.getContainerHeight()<this.limitHeight) return this;
        this.render();
        this.bindClick();
        return this;
    },
    render:function(){
        var container = this.container;
        this.originHeight = container[0].style.height;
        this.padHeight = container[0].scrollHeight-container.height();
        var trigger= this.triggerEle = $('<div class="'+(this.trigger+'_').slice(1,-1)+'"><div /></div>');
        trigger.appendTo(container).show();
        container.css({
            position:'relative',
            height:this.limitHeight + 'px',
            overflow:'hidden'
        });
    },
    getContainerHeight:function(){
        return this.container.height();
    },
    setHeight:function(){
        if (this.triggerEle.hasClass(this.triggerexp)){
            this.triggerEle.removeClass(this.triggerexp);
            this.container.animate({height:this.limitHeight},this.duration).removeClass('collapse-expand');
        }
    },
    resetHeight:function(){
        var that = this;
        if (!this.triggerEle.hasClass(this.triggerexp)){
            var scrollHeight = this.container[0].scrollHeight - this.padHeight;
            this.triggerEle.addClass(this.triggerexp);
            this.container.animate({height:scrollHeight},this.duration,function(){
                $(this).height(that.originHeight).addClass('collapse-expand');
            });
        }
    },
    bindClick:function(){
        var that = this;
        // collopse;
        $('body').delegate(that.trigger, 'click',function(){
            if ($(this).hasClass(that.triggerexp)){
                that.setHeight();
            }else{
                that.resetHeight();
            }
        });

    }
};


// 浮层模块
var FloatLayer = function(opts){
    var opts = $.extend({
        trigger:"",
        className:"",
        id:"",
        data:{},
        template:"",
        async:false,
        type:'click',
        offsetX:0,
        offsetY:20,
        toggle:true,
        open:$.noop,
        close:$.noop,
        css:"",
        openEffect:"",
        closeEffect:""
    },opts);

    var tpl;

    opts.trigger = opts.trigger.jquery ? opts.trigger : $(opts.trigger);

    if($(opts.template).prop('tagName') == 'SCRIPT'){
        opts.template = $(opts.template).html();
    }

    var layer = $('<div' + (opts.id ? ' id="' + opts.id + '"' : '') + ' class="ac-floatlayer'+ (opts.className ? " " + opts.className : "") +'" style="display:none;position:absolute;"/>');

    if(opts.css) {
        layer.css(opts.css);
    }

    // 非异步则立即加载默认数据
    if(!opts.async){
        tpl = kendo.template( opts.template );
        layer.html(tpl(opts.data));
    }

    $('body').append(layer);

    kendo.init(layer);

    $(document).on('click',function(e){
        t = e.target;
        var ele = opts.trigger.length > 0 ? opts.trigger:opts.trigger.selector;
        if ( !$(t).is(ele) && opts.trigger.has(t).length===0 ){
            if ( $(t).closest('.ac-floatlayer').length !==1 ){
                layer.close();
            }
        }
    });

    function set_pos(ele){
        var pos = ele.offset();
        layer.css({
            left:pos.left + opts.offsetX,
            top:pos.top + opts.offsetY
        });
    }

    if(opts.trigger.length>0){
        opts.toggle=false;
    };

    // todo: support more type;
    if(opts.trigger.selector != ""){
        if(opts.type === "hover"){
           $('body').delegate(opts.trigger.selector,'mouseenter',function(e){
               var that = $(e.target);
               layer.input = that;
               set_pos(that);
               layer.open();
           });

           $('body').delegate(opts.trigger.selector,'mouseleave',function(e){
               if($(e.relatedTarget).closest('.ac-floatlayer').length<1){
                   layer.close();
               }
           });

           layer.on('mouseleave',function(){
                layer.close();
           });

        }else{
            $('body').delegate(opts.trigger.selector,opts.type,function(e){
                var that = $(e.target);

                // fix children clicked;
                var container = that.closest(opts.trigger.selector);
                if(container.length){
                    that = container;
                }
                // endfix

                set_pos(that);
                layer.input = that;
                if(opts.toggle){
                    layer.toggle();
                }else{
                    layer.open();
                }
            });
        }
    }else{
        opts.trigger.bind(opts.type,function(){
            var that = $(this);
            set_pos(that);
            layer.input = that;
            if(opts.toggle){
                layer.toggle();
            }else{
                layer.open();
            }
        });
    }

    layer.data = function(d){
        tpl = kendo.template( opts.template );
        layer.html(tpl(d));
        kendo.init(layer);
    };

    layer.content = function(d){
        layer.html(d);
        kendo.init(layer);
    };

    layer.open = function(){
        if(opts.openEffect)
        {
            layer[opts.openEffect]();
        }
        else{
            layer.show();
        }
        opts.open.apply(this);
    };

    layer.close = function(){
        if(opts.closeEffect)
        {
            layer[opts.closeEffect]();
        }
        else{
            layer.hide();
        }
        opts.close.apply(this);
    };

    layer.on('click','.close',function(){
        layer.close();
    });

    return layer;
};

//常旅客模块
var frequentFlyer=function(opts){
    var opts=$.extend({trigger:"zAutocomplete",offsetX:-1,offsetY:19},opts);
    var layer=FloatLayer(opts);
    kendo.init(layer);

    $("#ffc_input").focusin(function(){
        $(this).trigger("zAutocomplete");
    })

    return layer;
}

/* 城市补全js 模板
<script type="text/x-kendo-template" id="city_popup">
<div id="tabstrip" class="tcy_tabstrip" style="display:none;">
<span class="tcy_title">
热门城市/国家（支持中文名/拼音/英文名/三字码）
</span>
<ul>
<li id="hot_city" class="k-state-active">热门</li>
# for (var i=0; i<data.group.length; i++ ) { #
<li>#= data.group[i] #</li>
# } #
</ul>
<div id="hot_city_tab">
<ul class="tcy_list clearfix">

# for(var j=0;j<hotcitylist.length;j++){ #
# var citylist_item = hotcitylist[j] #
<li class="item" title="#= citylist_item.code #" data-code="#= citylist_item.code #">#= citylist_item.name #</li>
# } #
</ul>
</div>

# for(var i=0;i<data.citylist.length;i++){ #
# var citylist_item = data.citylist[i] #
<div class="city_tab">
<ul class="tcy_list clearfix">
# for(var k=0;k<data.group[i].length;k++){ #
<li class="tcy_list_sep">#= data.group[i].split('')[k] #</li>
# for(var j=0;j<citylist_item.value.length;j++){ #
# var citylist_value_item = citylist_item.value[j]; #
# if(citylist_value_item.py.slice(0,1) == data.group[i].split('')[k]) {#
<li class="item" title="#= citylist_value_item.code #" data-code="#= citylist_value_item.code #">#= citylist_value_item.name #</li>
# } #
# } #
# } #
</ul>
</div>

# } #
</div>
</script>

模板结束 */

var CityAutocomplete = function(settings){
    var that = {};
    var inputs,opts,hot_tabs;

    var defaults = {
        input : ".suggest-city",
        url: './searchflight_files/data.txt',
        group: ["ABCD","EFGHJ", "KLMN", "PQRSTW", "XYZ"]
    };

    that.options = $.extend(defaults,settings);


    /* /FlightReserve/DataAssistant/GetCitys.aspx */

    function process_citydata(d){
        return jQuery.map(d.split(';'),function(n){
            if(n.indexOf('|')==-1) return;
            var item = n.split('|');
            return {'code':item[0],'search':item[0]+'|'+item[2]+'|'+item[1],'py':item[1],'name':item[2]};
        });
    }

    function process_pinyin(arr, group, col){
        var result = [],
        len = group.length,
        r = 0,
        item;

        for(; r<len; r++){
            item = group[r];
            result[r] = {
                name:"",
                value:[]
            };
            result[r]['name'] = group[r];

            // 如果是字母的tab
            if(item[0].charCodeAt(0)<=122){
                jQuery.each(arr, function(index,i) {
                    var str = i[col];
                    var first_letter = str.toUpperCase().charCodeAt(0);
                    var rangeStart = item.charCodeAt(0);
                    var rangeEnd = item.charCodeAt(group[r].length-1);

                    if (first_letter >= rangeStart && first_letter <= rangeEnd) {
                        result[r]['value'].push(i);
                    }
                });
            }else{
                jQuery.each(arr, function(index,i) {
                    var str = i[col];
                    if (item === str) {
                        result[r]['value'].push(i);
                    }
                });
            }
        }

        return result;
    }

    function render_hotcity_tabs(data,input){
        var templateID = opts.template || "#city_popup_tpl";

        hot_tabs = FloatLayer({
            toggle:false,
            trigger:input,
            type:'focus',
            offsetY:25,
            data:data,
            template:templateID,
            css : that.options.css ? that.options.css : {}
        });


        hot_tabs.delegate('.tcy_list li[data-code]','click',function(){
            var $t = $(this);
            var text = $t.text();

            hot_tabs.input.val(text).trigger('change');
            if (opts.codeEle){
                $(opts.codeEle).val($t.data('code'));
            }else{
                hot_tabs.input.data('code').val($t.data('code'));
            }
            hot_tabs.close();
        });

        hot_tabs.find('.tcy_tabstrip').kendoTabStrip({
            animation:false
        });
    }

    function render_suggest_city(data,input){
        var autocomplate_defaults = {
            dataTextField:'search',
            animation:false,
            filter:function(d,f){
                return {
                    filters:[{
                        ignoreCase: true,
                        value:  d,
                        operator: 'startswith',
                        field: 'code'
                    },{
                        ignoreCase: true,
                        value:  d,
                        operator: 'startswith',
                        field: 'py'
                    },{
                        ignoreCase: true,
                        value:  d,
                        operator: 'startswith',
                        field: 'name'
                    }],
                    logic:'or'
                }
            },
            template: '<span class="sg_py">${data.py}</span><span class="sg_name">${data.name}</span><span class="sg_code">（${data.code}）</span>',
            dataSource: data,
            highlightFirst: true,
            placeholder:"拼音/城市码/中文",
            select:opts.select || function(t){
                var dataItem = this.dataItem(t.item.index());
                t.preventDefault();
                t.sender.value(dataItem.name);
                if (opts.codeEle){
                    $(opts.codeEle).val(dataItem.code);
                }else{
                    $(t.sender.element).data('code').val(dataItem.code);
                }
            }
        };

        input.kendoAutoComplete($.extend(autocomplate_defaults,opts.autocomplete));

        if (opts.width){
            input.each(function(){
                var t = $(this);
                t.data('kendoAutoComplete').list.width(opts.width||200);
            });
        }

        input.on('keyup',function(){
            var $t = $(this);
            if( $t.val() === '' ){
                hot_tabs.open();
            }else{
                hot_tabs.close();
            }
        });

        input.on('focus',function(){
            hot_tabs.find('.tcy_tabstrip').data('kendoTabStrip').activateTab("#hot_city");
        });

    };


    opts = that.options;
    inputs = opts.input.jquery ? opts.input : $(opts.input);

    function main(d){
        that.init = function(inputEle){
            if(typeof opts.codeEle == 'undefined'){
                inputEle.each(function(){
                    var t = $(this);
                    var ele = $('<input type="hidden" name="'+(t.name||'')+'_code">');
                    t.data('code',ele);
                    t.after(ele);
                });
            }
            var citydata = d.split('@');
            var pcitydata = process_citydata(citydata[1]);
            var citygroup = opts.group;

            // 去掉热门
            citygroup.shift();

            render_hotcity_tabs({
                "group": citygroup,
                "citylist": process_pinyin(pcitydata, citygroup, "py"),
                "hotcitylist": process_citydata(citydata[0])
            },inputEle);

            render_suggest_city(pcitydata,inputEle);
            return that;
        };


        that.init(inputs);
    }

    if(!CityAutocomplete.data){
        $.get(opts.url,function(d){
            CityAutocomplete.data = d;
            main(d);
        });
    }else{
        main(CityAutocomplete.data);
    }


};



// 加载谈层
$.loadingbar = function(settings) {
    var defaults = {
        autoHide:true,
        replaceText:"正在刷新,请稍后...",
        container: 'body',
        showClose: true,
        wrapperClass:'',
        text:'数据加载中，请稍候…',
        template:'',
        templateData:{}
    };
    var xhr;
    var cfg = $.extend({},defaults,settings);
    var postext;


    if(cfg.container==='body'){
        postext = 'fixed';
    }else{
        postext = 'absolute';
        $(cfg.container).css({position:'relative'});
    }


    var spin_wrap,content_tpl;

    if(cfg.template && $(cfg.template).length){
        if(typeof kendo != 'undefined'){
            content_tpl = kendo.template($(cfg.template).html())(cfg.templateData);
        }else{
            content_tpl = $(cfg.template).html();
        }
    }else{
        content_tpl = '<div class="loading_box '+cfg.wrapperClass+'"><div class="lightbox-content">\
                          <span class="loading_close">×</span>\
                          <i class="loading_icon">&nbsp;</i><span class="loading_text">'+cfg.text+'</span>\
                          </div></div>';
    }

    spin_wrap  = $('<div class="lightbox" style="display:none;position:'+postext+'">\
        <table cellspacing="0" class="ct"><tbody><tr><td class="ct_content"></td></tr></tbody></table>\
        </div>');

    spin_wrap.find(".ct_content").html(content_tpl);

    if(!cfg.showClose){
        spin_wrap.find(".loading_close").hide();
    }

    if(0 == $(cfg.container).find("> .lightbox").length){
        $(cfg.container).append(spin_wrap);
    }else{
        spin_wrap = $("> .lightbox",$(cfg.container));
    }

    $(document).ajaxSend(function(event, jqxhr, settings) {
        var surl = settings.url;
        var state = false;
        if(typeof cfg.urls != 'undefined'){
            $.each(cfg.urls,function(i,item){
                if($.type(item) === 'regexp'){
                    if(item.exec(surl)) {
                        state = true;
                        return false;
                    }
                }else if($.type(item) === 'string'){
                    if(item === surl) {
                        state = true;
                        return false;
                    }
                }else{
                    throw new Error('[urls] type error,string or regexp required');
                }
            });
        } else {
            spin_wrap.show();
        }

        if(state){
            spin_wrap.show();
        }

        if(cfg.showClose){
            $('.loading_close').on('click',function(e){
                jqxhr.abort();
                $.active = 0;
                spin_wrap.hide();
                $(this).off('click');
            });
        }
    });

    $(document).ajaxStop(function(e) {
        if(cfg.autoHide){
            spin_wrap.hide();
        }else{
            spin_wrap.find(".loading_text").html(cfg.replaceText);
        }
    });

    return spin_wrap;


};

// serialize_form
$.fn.serialize_form = function(){
    var result = [];
    var that = $(this);
    that.find('input,textarea,select').each(function(index) {
        var i = $(this);
        var name = i.attr('name');
        var eleType = i.attr('type');
        var isDisabled = i.attr('disabled');
        var value = i.attr('value');

        var isChecked = i.attr('checked');

        if (isDisabled || name == '' || typeof name == 'undefined' || name == '__MYVIEWSTATE') {
            return;
        }

        if ((eleType == 'checkbox' || eleType == 'radio') &&  isChecked != 'checked') {
            return;
        }

        // result[name]=value;
        result.push(name + '=' + value);

    });

    return result.join('&');
};

//
$.plainObjectSize = function(obj) {
    var size = 0, key;
    for (key in obj) {
        if (obj.hasOwnProperty(key)) size++;
    }
    return size;
};

//
$.fieldsetFormat = function(type,settings){
    // 纯对象数据长度
    // example : plainObjectSize({a:1,b:2}) == 2;
    var plainObjectSize = function(obj) {
        var size = 0, key;
        for (key in obj) {
            if (obj.hasOwnProperty(key)) size++;
        }
        return size;
    };

    var output = {};
    var type = type || 'get';
    var params={
        selector:'fieldset',
        item:'.group-item',
        items:'.group-items',
        data:''
    };

    params = $.extend(params,arguments[1]);

    var data = params.data;
    var selector = (typeof params.selector=='string')?$(params.selector):params.selector;
    var item = params.item;
    var items = params.items;
    var loop = function(nodeList,parent,pindex){
        var gobj = {};
        nodeList.each(function() {
            var that = $(this);
            var name = that.prop('name');
            var eleType = that.prop('type');
            var isDisabled= that.prop('disabled');

            if (name == '' ||  name=='__MYVIEWSTATE' || isDisabled){
                return false;
            }

            var value = that.prop('value');

            if (isDisabled || name == '' ||  name=='__MYVIEWSTATE'){
                return false;
            }

            if ( (eleType == 'checkbox' || eleType == 'radio') && that.prop('checked')==false ){
                return false;
            }

            if(type=="get"){
                gobj[name] = value;
            }

            if(type=="set"){
                if(pindex!=undefined){
                    that.prop('value',data[parent][pindex][name]);
                }else{
                    that.prop('value',data[parent][name]);
                }
            }
        });

        return gobj;
    };

    $.each(selector,function(index) {
        var i = $(this);
        if(index==0 &&i.prop('tagName')!='FIELDSET'){
            return false;
        }
        if (i.prop('name') == '') {
            return false;
        }
        if (i.find(item).size()>0) {
            if(i.find(items).size()>0){
                var obj = loop(i.find('input,select,textarea').filter(':not('+items+' input)').filter(':not('+items+' select)').filter(':not('+items+' textarea)'));

                i.find(items).each(function(vp){
                    var vp = $(this);
                    var arr = [];
                    vp.find(item).each(function(pindex) {
                        var v = $(this);
                        if(v.hasClass('disabled')||v.prop('disabled')) return false;
                        var vpobj = loop(v.find('input,select,textarea'),vp.prop('rel'),pindex);
                        if(plainObjectSize(vpobj)){
                            arr.push(vpobj);
                        }

                    });

                    obj[vp.prop('rel')] = arr;

                });

                output[i.prop('name')] = obj;
            }else{
                var arr = [];
                i.find(item).each(function(pindex) {
                    var v = $(this);
                    var obj = loop(v.find('input,select,textarea'),i.prop('name'),pindex);
                    arr.push(obj);
                });

                output[i.attr('name')] = arr;
            }

        } else {
            var obj = loop(i.find('input,select,textarea'),i.prop('name'));
            output[i.attr('name')] = obj;
        }

    });
    return output;
};

// [demo](http://dev.b2b.com/%E7%A2%8E%E7%89%87/%E8%AE%A2%E5%8D%95%E7%8A%B6%E6%80%81%E6%8C%87%E7%A4%BA%E6%A0%87%E8%AF%86.html)
var lensf = function (settings){
    var defaults = {
        container:'.instr',
        highlight:'highlight'
    };
    var opts = $.extend(defaults,settings);

    $.each($(opts.container),function(){
        var that = $(this);
        var current = that.find('[class]').eq(-1);
        var ele = that.find('span');
        var firstWidth = ele.eq(0).outerWidth(true);
        var lastWidth = ele.eq(-1).outerWidth(true);
        var containerWidth = that.innerWidth()-firstWidth/2-lastWidth/2;

        function addLine(){

            var allprev = current.prevAll();
            if( opts.highlight ){
                allprev.addClass(opts.highlight);
            }
            hi = allprev.andSelf();

            var wid = (function(){
                var w=0;
                hi.each(function(){
                    w+=$(this).outerWidth(true);
                });
                return w;
            })();

            if(current.length>0){
                 wid = wid-firstWidth/2-current.outerWidth(true)/2;
            }

            that.append('<em class="line" style="left:'+firstWidth/2+'px;width:'+containerWidth+'px"><div style="width:'+wid+'px" /></em>');
        }
        addLine();
        that.addClass('lensf_status');
    });
};

// [demo](http://dev.b2b.com/%E7%A2%8E%E7%89%87/checkgroup.html)
var checkGroup = function(settings){
    var opts = $.extend({
        group : '[data-role=checkgroup]',
        name : '[data-role=checkgroup-item]'
    },settings);
    opts.group = opts.group.jquery?opts.group:$(opts.group);
    opts.name = opts.name.jquery?opts.name:$(opts.name);
    var renderAsGroup = function(){
        $.each(opts.group,function(index){
            var items = $(this).find('[type=checkbox]');
            bindChange(items);
        })
    }
    var renderAsSameName = function(){
        var names = {};
        $.each(opts.name,function(index){
            var that = $(this);
            var name = that[0].name;
            if(names){

            }
            // var items = $(this).find('[type=checkbox]');
            // bindChange(items);
        });
    }

    var bindChange = function(items){
        items.on('change',function(){
            items.filter(':checked').not(this).prop('checked',false);
        });
    };

    var init = function(){
        renderAsGroup();
        // renderAsSameName();
    };

    init();

    return {


    };
};

/*弹出提示框*/
$.prompt=function(options){
    var defaults= {autoClose:true,delay:3000,width:400,height:30,zIndex:1000,bgColor:"#000",bgOpacity:0.1,content:"",offsetX:0,offsetY:0,closeSpeed:500,openSpeed:500,effect:"fade",openEvent:$.noop,closeEvent:$.noop,opacity:1};
    var opts = $.extend({},defaults,options);
    var pt=$("#promptWindow");
    function init(){
        if(!pt.length)
        {
            $("body").append("<div id='promptWindow'><div id='promptWindow_bg'></div><div id='promptWindow_content'></div></div>");
            pt=$("#promptWindow").css({zIndex:opts.zIndex});
            pt.css({/*width:opts.width+"px",height:opts.height+"px",*/display:"none",position:"absolute"});
            $("#promptWindow_bg").css({background:opts.bgColor,opacity:opts.bgOpacity,width:"100%",height:"100%",filter:"alpha(opacity="+opts.opacity*10+")"});
        }

        //$("#promptWindow_content").html(opts.content).css({/*width:"100%",height:"100%",*/position:"absolute",top:0,left:0,zIndex:1001});
        $("#promptWindow_content").html(opts.content);
    }


    init();


    return {
        open:function(){
            switch(opts.effect)
            {
                case "fade":$("#promptWindow").fadeIn(opts.openSpeed,opts.openEvent);break;
                case "slide":$("#promptWindow").slideDown(opts.openSpeed,opts.openEvent);break;
                default :$("#promptWindow").show(opts.openSpeed,opts.openEvent);break;
            }

            if(opts.autoClose)
            {
                setTimeout(this.close,opts.delay);
            }
            return this;
        },
        setOptions:function(options){
            $("#promptWindow").css(options);
            return this;
        },
        close:function(){
            switch(opts.effect)
            {
                case "fade":$("#promptWindow").fadeOut(opts.openSpeed,opts.openEvent);break;
                case "slide":$("#promptWindow").slideUp(opts.openSpeed,opts.openEvent);break;
                default :$("#promptWindow").hide(opts.openSpeed,opts.openEvent);break;
            }
            return this;
        },
        setContent:function(content){
            $("#promptWindow_content").html(content);
            return this;
        },
        center:function(){

            $("#promptWindow").css("top",((document.documentElement.clientHeight/2)-($("#promptWindow").height()/2)+(document.body.scrollTop || document.documentElement.scrollTop)+opts.offsetY));
            $("#promptWindow").css("left",((document.body.scrollWidth/2)-($("#promptWindow").width()/2)+opts.offsetX));
            return this;
        },
        setOpacity:function(opacity){
            $("#promptWindow_bg").css({opacity:opacity,filter:"alpha(opacity="+opacity*10+")"});
            return this;
        },
        setBgColor:function(bgColor){
            $("#promptWindow_bg").css({backgroundColor:bgColor});
            return this;
        }
    }
}

// checkFamily
;(function ($) {
    $.fn.cbFamily = function (children) {
        return this.each(function () {
            var $this = $(this);
            var els;
            if ($.isFunction(children)) {
                els = children.call($this);
            } else {
                els = $(children);
            }
            $this.bind("click.cbFamily", function () {
                els.prop('checked', this.checked).change();
            });

            function checkParent() {
                $this.prop('checked',
                    els.length == els.filter("input:checked").length);
            }

            els.bind("click.cbFamily", function () {
                if ($this.prop('checked') && !this.checked) {
                    $this.prop('checked', false).change();
                }
                if (this.checked) {
                    checkParent();
                    $this.change();
                }
            });

            // Check parents if required on initialization
            checkParent();
        });
    };
})(jQuery);

// style input:file
$.fn.fileInput = function(settings){
    var settings = $.extend({
        browseButton:'<b>浏览</b>'
    },settings);

    var wrapper = $('<div/>').css({height:0,width:0,'overflow':'hidden'});
    settings.browseButton = settings.browseButton ? settings.browseButton : '';

    this.each(function(){
        var that= $(this);
        var pathInput = $('<span class="filePath"><i></i>'+settings.browseButton+'</span>').insertBefore(that);
        var fileInput = that.wrap(wrapper);

        fileInput.change(function(){
            var val = $(this).val();
            pathInput.children('i').text(val).attr('title',val);
        })

        pathInput.click(function(){
            fileInput.click();
        });
    });

}

// data-role ctype
$.role_ctype = function(settings){
    var settings = $.extend({
        changeClass:false,
        evt:'click',
        callback:function(){}
    },settings);

    $('body').delegate('[data-role="ctype"]', settings.evt, function(e){
        var index = $(this).data('index');
        var group = $(this).data('group');
        var all = $('[data-role="ctype-item"][data-group="'+group+'"]');

        all.hide();
        all.filter('[data-index~="'+index+'"]').show();

        if(this.type == 'radio' || this.type == 'checkbox'){
            $(this).change();
        }

        if(settings.changeClass){
            $('[data-role="ctype"][data-group="'+group+'"]').removeClass(settings.changeClass);
            $(this).addClass(settings.changeClass);
        }

        settings.callback.call(this,{
            index:index,
            group:group
        });
    });
}

// data-role ctoggle
$.role_ctoggle = function(settings){
    var opt = $.extend({
        evt:'click'
    },settings);

    $('body').on(opt.evt+'.ctoggle','[data-role="ctoggle"]', function(){
        var that = $(this);
        var text = that.data('text');
        var textArray;
        var textEle = that.find('b');
        var target = that.data('target');
        $(target).toggle();
        that.toggleClass('expanded');

        if(!text){
            text='显示|隐藏';
        }

        textArray = text.split('|');
        if(textArray.length===2){
            if(textEle.text() == textArray[0] ){
                textEle.text(textArray[1]);
            }else{
                textEle.text(textArray[0]);
            }
        }

    });
}

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


// 悬浮条
$.fn.fixedBar = function(settings){
    var defaults = {
        inverse:false,
        fixed:false,  //是否始终固定位置
        css:"position:fixed;top:0;",
        endAt:0,
        offsetTop:0,
        createShadow:'fixBarShadow'
    };

    var callback,opts;

    if(this.length==0) return;

    if( Object.prototype.toString.call(settings) == "[object Function]" ){
        callback = settings;
    }

    if(settings && settings.inverse){
        defaults.css = 'position:fixed;bottom:0;';
    }

    opts = $.extend(defaults, settings);

    if (window.ActiveXObject) {
        window.isIE = window[window.XMLHttpRequest ? 'isIE7' : 'isIE6'] = true;
    }

    if (window.isIE6) try {document.execCommand("BackgroundImageCache", false, true);} catch(e){}

    var ele = $(this).length > 1 ? $(this).eq(0) : $(this);
    var shadow;

    function init(){
        var eleOffsetTop = ele.offset().top;
        var elePositionTop = ele.position().top;

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


        function getEndPos(){
            var end;
            if(typeof opts.endAt === 'number'){
                end = opts.endAt;
            }else{
                end = $(opts.endAt).offset().top + $(opts.endAt).height();
            }

            return end;
        }


        $(window).bind("scroll.fixedBar",function(e){
            if(ele.is(':hidden')){
                return;
            }

            var that = $(this);
            var scrollTop = that.scrollTop() + opts.offsetTop;
            var winHeight = $(window).innerHeight();

            var changeBar = function(){
                if(!ele.hasClass("fixedBar")){
                    shadow && shadow.show();
                    ele.addClass("fixedBar").attr("style",opts.css);
                    if(opts.offsetTop!==0){
                       ele.css('top',opts.offsetTop);
                    }
                }
                // todo ie6
                if(window.isIE6) ele.css({"top":scrollTop - eleOffsetTop + elePositionTop + "px","position":"absolute"});
            };

            var resetBar = function(){
                shadow && shadow.hide();
                ele.removeClass("fixedBar").removeAttr("style");
            };

            // if(opts.endAt && ele.outerHeight() < winHeight-opts.offsetTop){
            if(opts.endAt){
                // if(scrollTop + ele.outerHeight() >= getEndPos() && scrollTop > eleOffsetTop){
                if( scrollTop + ele.outerHeight() >= getEndPos() && scrollTop > eleOffsetTop){
                    resetBar();
                    ele.addClass('fixedBarEndAt');
                }else{
                    ele.removeClass('fixedBarEndAt');
                }
            }

            if(!ele.hasClass('fixedBarEndAt')){
                if(!opts.inverse){
                    if(scrollTop > eleOffsetTop){
                        changeBar();
                    }else{
                        resetBar();
                    }
                }else{
                    if(scrollTop + winHeight - ele.outerHeight() < eleOffsetTop){
                        changeBar();
                    }else{
                        resetBar();
                    }
                }
            }


            if(callback) callback.call(ele,scrollTop);

        });

        if(opts.inverse){
            $(function(){
                $(window).trigger('scroll.fixedBar');
            })
        }

        if(window.isIE){
            $(document).on('click.fixedBar',function(){
                if(ele.hasClass("fixedBar")){
                    $(window).trigger('scroll.fixedBar');
                }
            });
        }

    }

    init();

    var api = {
        reset:function(){
            ele.removeClass("fixedBar").removeAttr("style");
            $(window).unbind("scroll.fixedBar");
            opts.createShadow && shadow.remove();
            return this;
        },
        init:function(){
            init();
            return this;
        }
    };

    ele.data('fixedBar',api);

    return this;
};

/*
**	Anderson Ferminiano
**	contato@andersonferminiano.com -- feel free to contact me for bugs or new implementations.
**	jQuery ScrollPagination
**	28th/March/2011
**	http://andersonferminiano.com/jqueryscrollpagination/
**	You may use this script for free, but keep my credits.
**	Thank you.
*/

(function( $ ){


 $.fn.scrollPagination = function(options) {

		var opts = $.extend($.fn.scrollPagination.defaults, options);
		var target = opts.scrollTarget;
		if (target == null){
			target = obj;
	 	}
		opts.scrollTarget = target;

		return this.each(function() {
		  $.fn.scrollPagination.init($(this), opts);
		  
			if(opts.loadFirst){
				$(target).data('loadFirst',true);
				$.fn.scrollPagination.loadContent($(this),opts);
			}
		});

  };

  $.fn.stopScrollPagination = function(){
	  return this.each(function() {
	 	$(this).attr('scrollPagination', 'disabled');
	  });

  };

  $.fn.scrollPagination.loadContent = function(obj, opts){
	 var target = opts.scrollTarget;

	 var mayLoadContent = $(target).scrollTop()+opts.heightOffset >= $(document).height() - $(target).height();
	 var loadFirst = $(target).data('loadFirst');
	 
	 if (mayLoadContent || loadFirst){

		 $(target).removeData('loadFirst');

		 if (opts.beforeLoad != null){
			opts.beforeLoad();
		 }

         if(!$(obj).data('scrollPagination')){
             var removeData = function(){
                 $(obj).removeData('scrollPagination');
             };

             var xhr =  $.ajax({
                 type: 'POST',
                 url: opts.contentPage,
                 data: opts.contentData,
                 success: function(data){
                     var newdata = data;
                     if(opts.dataType == "html"){
                         newdata = $(data);
                         $(obj).append(newdata);
                     }

                     if (opts.afterLoad != null){
                         opts.afterLoad(newdata);
                     }
                 },
                 error:function(xhr, textStatus){
                        opts.debug ? console.log("Scroll Pagination Load Error .." + textStatus) : ''  ;
                        removeData();
                 },
                 complete:function(xhr,textStatus){
                        opts.debug ? console.log("Scroll Pagination Load Complete .." + textStatus) : ''  ;
                        removeData();
                },
                 dataType: opts.dataType
             });

             $(obj).data('scrollPagination',xhr);
         }

	 }

  };

  $.fn.scrollPagination.init = function(obj, opts){
	 var target = opts.scrollTarget;
	 $(obj).attr('scrollPagination', 'enabled');

	 $(target).scroll(function(event){
		if ($(obj).attr('scrollPagination') == 'enabled'){
	 		$.fn.scrollPagination.loadContent(obj, opts);
		}
		else {
			event.stopPropagation();
		}
	 });

	 $.fn.scrollPagination.loadContent(obj, opts);

 };

 $.fn.scrollPagination.defaults = {
      	 'contentPage' : null,
     	 'contentData' : {},
		 'beforeLoad': null,
		 'afterLoad': null	,
		 'scrollTarget': null,
		 'heightOffset': 0,
         'dataType': 'html'
 };

$.fn.fadeInWithDelay = function(){
    var delay = 0;
    return this.each(function(){
        $(this).delay(delay).animate({opacity:1}, 200);
        delay += 100;
    });
};

})( jQuery );




