$(function(){
    $.get(encodeURI("/运营商/子订单.html"),{},function(data){
        $(data).appendTo("body");

        /* 鼠标移动效果 */
        $("#child_orders div.item").each(function(){
                $(this).hover(function(){
                    $(this).css({"background-color":"#ffffdb","border-color":"#cccccc"});
                },function(){
                    if($(this).hasClass("last")){
                        $(this).css({"background-color":"#fff5f5","border-color":"#FF4E00"});
                    }else{
                        $(this).css({"background-color":"#ffffff","border-color":"#B8F1FF"});
                    }
                });
        });
        $("#child_order_trigger").click(function(e){
             var pos = $(this).offset();
             $("#child_orders").css({top:pos.top + "px",left: (pos.left - 277) + "px"}).show();
             e.stopPropagation();
        });
        
        $("body").click(function(e){
            $("#child_orders").hide();
            e.stopPropagation();
        });
    });
});
