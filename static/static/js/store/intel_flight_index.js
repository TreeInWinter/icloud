$(function() {

    $.role_ctype();

    $('.top_title input').on('change',function(){
        var li = $(this).closest('li'); 
        li.addClass('selected');
        li.siblings().removeClass('selected');
    });

    var uploadsettings = {
        multiple:false,
        showFileList:false,
        select:function(e){
            e.sender.wrapper.prev().val(e.files[0].name);
        },
        complete:function(e){
            var url = e; 
        },
        async: {
            saveUrl: "/path/to/save.jsp",
            //saveUrl: 'http://www.tz-local.com/test.php',
            autoUpload: true
        },
        localization:{ 
            select: '浏览' 
        } 
    };

    kendo.culture('zh-CN');

    $('.ac-add-line').bind('click',function(){
        var container = $('.muti_trip tbody');
        var html = kendo.template($('#ac-add-line-template').html())({});
        container.append(html);
        // kendo.init(container);
        // container.find('[type=file]').eq(-1).kendoUpload(uploadsettings)
        reorder();
        createCityAc(container.find('tr').last().find('.city_ac'));
    });

    $('body').delegate('.ac-remove-line','click',function(){
        var t = $(this);
        if (t.closest('table').find('tr').length <= 2){
            //return alert('只剩一条了'); 
            return;
        }
        t.closest('tr').remove();
        reorder();
    });

    function reorder(){
        $('.index_num').each(function(i){
            var t = $(this);
            t.text(i+1);
        }); 
    }

    function createCityAc(ele){
        CityAutocomplete({
            template: "#city_popup",
            input: ele,
            width: 200,
            // autocomplete:{
            //     dataSource:{
            //         transport:{
            //             read:{

            //             } 
            //         } 
            //     },
            //     placeholder:'三字吗/城市'
            // },
            // 以@分割热门非热门，以;分割条目，以|分割三字码拼音和中文
            url:'/static/js/data.txt',
            group: ["热门","GHJ", "ABCDEF", "KLMN", "PQSTW", "XYZ"]
        });
    }

    createCityAc(".city_ac");

    kendo.init('body');
    $('[type=file]').kendoUpload(uploadsettings)

});

