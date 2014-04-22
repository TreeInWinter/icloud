$(function(){
    kendo.culture('zh-CN');
    kendo.init($('body'));
    $('.actions').fixedBar();

    var t = /proto|dev\.b2b\.com/i;
    if(t.test(location.href)){
        lensf();
    }

    $('.ac-toggle').bind('click',function(){
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


    $('.ac-edit').bind('click',function(){
        var that = $(this);
        var target = that.data('target');
        var scrollClass;
        $(target).toggle();
        $(target).siblings().filter('td span').toggle();

        if(that.data('scroll')){
            $(target).closest('table').parent().toggleClass('xscroll');
        }

        if(that.find('b').text() == '编辑'){
            that.find('b').text('保存');
        }else{
            that.find('b').text('编辑');
        }

        setTimeout(function(){
            that.toggleClass('ac-save');
        },0);
    });


});

function grid_manipulate(settings){

    var wrap = $(settings.container);

    var model = kendo.observable({
        data:settings.data
    });

    var index = 0;
    model.data.forEach(function(e){
        e.set('index',index); 
        index++;
    });

    var struct = createStruct(settings.data) || null;

    model.data.bind('change',function(e){
        if(e.field=='index') return;
        var index = 0;
        var ev = {'add':1,'remove':1};

        if(ev[e.action]){
            if(jQuery.type(settings.rowNumChange) == 'function'){
                settings.rowNumChange.apply(null,arguments);
            }
            e.sender.forEach(function(i){
                i.set('index',index);
                index++;
            }); 
        }else{
            if( e.action == 'itemchange' && jQuery.type(settings.change) == 'function'){
                settings.change.apply(null,arguments);
            }
        }
    });

    function createStruct(arr){
        var item,result={}; 
        if( arr && arr.length < 1) return;

        item = arr[0];
        if(jQuery.type(item) == 'object'){
            for (key in item){
                result[key] = null; 
            }
        }

        return result;
    }

    function init(){
        if(jQuery.type(settings.init) == 'function'){
            settings.init.apply(null,arguments);
        }

        wrap.find('tbody').attr('data-bind','source:data');
        wrap.find('tbody').attr('data-template',settings.template);
        kendo.bind(wrap,model);
        kendo.init(wrap);
    }

    init(model);

    return {
        data:model.data,
        appendRow:function(d){
            if(d){
                this.data.push(d);
            }else if(struct){
                this.data.push(struct);
            }else{
                throw Error('没有数据\nstruct:'+struct+'\nargument:'+d); 
            }
        },
        copyRow:function(){
            var data = $.map(this.data.toJSON(),function(ele,index){
                if($(settings.container).find('tr:eq('+(index)+') input:checked').length) return ele;
            });

            this.data.push.apply(this.data,data);
        },
        removeSelected:function(){
            var data = this.data;
            $.each($(settings.container).find('input:checked').closest('tr'),function(){
                $(this).find('[data-role]').each(function(){
                    $(this).data('kendoDropDownList').list.remove(); 
                    $('.k-animation-container:empty').remove();
                });
                data.splice($(this).index(),1);
            });
        },
        remove:function(){
           var args = arguments;  
           var len = args.length,i=0;
           for (;i<len;i++){
                if(typeof args[i]=='number'){
                    this.data.splice(args[i],1);
                } 
           }
        },
        removeUnSelected:function(){
            var data = this.data;
            $.each($(settings.container).find('input:checkbox:not(:checked)').closest('tr'),function(index,ele){
                $(this).find('[data-role]').each(function(){
                    $(this).data('kendoDropDownList').list.remove(); 
                    $('.k-animation-container:empty').remove();
                });
                data.splice($(this).index(),1);
            });
        }
    }
}

