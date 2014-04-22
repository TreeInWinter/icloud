function load_header(){
    $(".header").load(encodeURI("/供应商/同步头.html"),{},function(){
        init_header();
    });
}
$(function(){
   if(typeof(init_header) == "undefined"){
       $.getScript(encodeURI("/static/js/supplier/supplier_header.js"),function(){
           load_header();
       });
   }else{
       load_header();
   }
});