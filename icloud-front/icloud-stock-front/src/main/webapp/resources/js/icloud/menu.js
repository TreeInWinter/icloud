function menuloading(){
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
}
//$(function () {
//   
//});
$(document).ready(function () {
    menuloading();
});
