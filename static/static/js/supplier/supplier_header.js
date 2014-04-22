function init_header(){
    currentMenuIndex = $("#top_menu > li.current").index();
    //菜单事件设置
    $("#top_menu > li").each(function(index){
        var obj = $("#top_menu > li.current");
        $(this).hover(function(){
            //记住当前是那个菜单
            if(currentMenuIndex < 0) {
                currentMenuIndex = $("#top_menu > li.current").index();
            }

            //$("#top_menu > li:eq(" + currentMenuIndex + ")").removeClass("current");

            $("#top_menu > li").removeClass("mnav");
            if(!$(this).hasClass("current")){
                $(this).addClass("mnav");
            }
            //$(".sub_menu").hide();
            //$(".sub_menu",this).show();
        },function(){

        });
    });

    /*
    $("#top_menu").mouseleave(function(){
        $("#top_menu > li").removeClass("mnav");
        $(".sub_menu").hide();
        $("#top_menu > li:eq(" + currentMenuIndex + ")").addClass("current");
        $("#top_menu > li:eq(" + currentMenuIndex + ") .sub_menu").show();
    });
    */

    $("#message h3 a,#message h3 span").mouseover(function(){
        $("#message_pop").show();
        $("#message_pop .ck a").click(function(){
            $("#message_pop").hide();
        });
    });
}
