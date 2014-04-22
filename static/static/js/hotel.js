/**
 * Created with JetBrains WebStorm.
 * User: TRAVELZEN-IT-ZHANGZZ
 * Date: 13-6-25
 * Time: 上午10:09
 * To change this template use File | Settings | File Templates.
 */

function isOldIE() {
    var e = document.createElement("DIV"),
        c = {
            haspointerlock: "pointerLockElement" in document || "mozPointerLockElement" in document || "webkitPointerLockElement" in document
        };
    c.isie = "all" in document && "attachEvent" in e && !c.isopera;
    c.isieold = c.isie && !("msInterpolationMode" in e.style);

    return c.isieold;
};
$(function(){
    var dt=new Date().getMilliseconds();

    $("body").delegate(".popwin","click",function(){
        var tar= $.parseJSON($(this).attr("data"));
        var kendoWindowData=$("#window").data("kendoWindow");
        if(!kendoWindowData)
        {
            $("#window").kendoWindow({
                animation:false,
                iframe:true,
                content:function(tar){return tar.content?{url:'',template:tar.contentFunction?eval(tar.content):tar.content}:tar.url+"?dt="+dt}(tar),
                modal:tar.modal || true,
                width:tar.width,
                height:tar.height,
                title:tar.title,
                visible:false,
                refresh:function(){
                    $("#window").data("kendoWindow").center();
                    $("#window").data("kendoWindow").open();
                    $("#window").data("kendoWindow").options.modal?showOverLay():'';
                },
                close:function(){
                    $("#window").data("kendoWindow").options.modal?$("#iframeShadow").hide():'';
                }
            });
        }
        else
        {
            kendoWindowData.options.modal?showOverLay():'';
            tar.content?kendoWindowData.refresh({url:'',template:tar.contentFunction?eval(tar.content):tar.content}):kendoWindowData.refresh(tar.url+"?dt="+dt);
            kendoWindowData.setOptions({
                width:tar.width,
                height:tar.height,
                title:tar.title
            });
        }
        return false;
    });

    function showOverLay()
    {
        if(isOldIE())
        {
            if(!$("#iframeShadow").length)
            {
                $("body").append("<div id='iframeShadow' style='z-index:1000;width:100%;position:absolute;top:0;left:0;;filter:alpha(opacity=0);'><iframe src='' width='100%' height='100%'></iframe> </div>")
            }
            $(".k-overlay,#iframeShadow").height(Math.max($("body").height(),$(window).height()));
        }
        $(".k-overlay,#iframeShadow").show();
    }

    //$(".topFixedToolsBar").scrollFix("top","top");

    $.fn.setPosition=function(options){
        var defaults={tar:this,pos:"",offsetL:0,offsetT:0};
        var opt= $.extend(defaults,options);
        var offset=opt.tar.offset();
        var tarw=opt.tar.outerWidth();
        var tarh=opt.tar.outerHeight();
        var w=$(this).outerWidth();
        var h=$(this).outerHeight();
        switch(opt.pos)
        {
            case "l":$(this).css({left:offset.left+opt.offsetL-w,top:offset.top+opt.offsetT});break;
            case "r":$(this).css({left:offset.left+opt.offsetL+tarw,top:offset.top+opt.offsetT});break;
            case "b":$(this).css({left:offset.left+opt.offsetL,top:offset.top+opt.offsetT+tarh});break;
            case "rb":$(this).css({left:offset.left+opt.offsetL+tarw,top:offset.top+opt.offsetT+tarh});break;
            case "lb":$(this).css({left:offset.left+opt.offsetL-w,top:offset.top+opt.offsetT+tarh});break;
            case "lt":$(this).css({left:offset.left+opt.offsetL-w,top:offset.top+opt.offsetT-h});break;
            case "t":$(this).css({left:offset.left+opt.offsetL,top:offset.top+opt.offsetT-tarh});break;
            case "rt":$(this).css({left:offset.left+opt.offsetL+tarw,top:offset.top+opt.offsetT-h});break;
            case "tc":$(this).css({left:offset.left+opt.offsetL-w/2+tarw/2,top:offset.top+opt.offsetT-h});break;
            default :$(this).css({left:offset.left+opt.offsetL,top:offset.top+opt.offsetT});break;
        }


    }
});

function moveElement(lb,rb,targetE,targetInnerE)
{
    var moveFlag=true;
    var startPos=targetE.position().left;
    targetInnerEWidth=targetInnerE.outerWidth(true);
    lb.click(function(){
        if(lb.hasClass("active") && moveFlag)
            targetE.animate({"left":"-="+targetInnerEWidth},100);
        setTimeout(changeBtnStatus,110);
    });
    rb.click(function(){
        if(rb.hasClass("active") && moveFlag)
        {
            moveFlag=false;
            targetE.animate({"left":"+="+targetInnerEWidth},100);
            setTimeout(changeBtnStatus,110);
        }
    });

    function changeBtnStatus()
    {
        if(targetE.position().left<startPos) rb.addClass("active"); else rb.removeClass("active");

        if(Math.abs(targetE.position().left)<targetInnerE.length*targetInnerEWidth-targetE.parent().width()) lb.addClass("active"); else lb.removeClass("active");

        moveFlag=true;
    }
    changeBtnStatus();
}

function checkboxSingleAndAll()
{
    $("body").delegate(":checkbox","click",function(){
        var dg=$(this).attr("data-group");
        if(dg!="")
        {
            if($(this).hasClass("single"))
            {
                if($(this).prop("checked")==true)
                {

                    $(":checkbox[data-group="+dg+"]").not(this).prop("checked",false);
                }
                else
                {
                    $(this).prop("checked",false);
                }
            }
            var features=$(this).attr("data-features");
            if(features!="")
            {
                switch(features)
                {
                    case "all":
                        if($(this).prop("checked")==true)
                        {
                            $(":checkbox[data-group="+dg+"]").prop("checked",true);
                        }
                        else
                        {
                            $(":checkbox[data-group="+dg+"]").prop("checked",false);
                        }
                        break;
                }
            }
        }
    });
}
