$(function(){
    var uploadsettings = {
        multiple:false,
        showFileList:false,
        select:function(e){
            e.sender.wrapper.prev().val(e.files[0].name);
        },
        complete:function(e){
            var url = e; 
            console.log(e);
        },
        async: {
            saveUrl: "/path/to/save.jsp",
            autoUpload: true
        },
        localization:{ 
            select: '上传' 
        } 
    };
    kendo.culture('zh-CN');
    $('.actions').fixedBar();
    $('.ac-add-agreement').bind('click',function(){
        var container = $(this).closest('.block-field');
        var html = kendo.template($('#ac-add-agreement-template').html())({});
        container.append(html);
        kendo.init(container);
        container.find('[type=file]').eq(-1).kendoUpload(uploadsettings)
    });
    $('body').delegate('.ac-remove-agreement','click',function(){
        $(this).closest('.flat-list').remove();
    });

    $('.ac-add-thirdagreement').bind('click',function(){
        var container = $(this).closest('.block-field');
        var html = kendo.template($('#ac-add-thirdagreement-template').html())({});
        container.append(html);
        kendo.init(container);
        container.find('[type=file]').eq(-1).kendoUpload(uploadsettings)
    });

    $('.ac-add-certificate-template').bind('click',function(){
        var container = $(this).closest('.block-field');
        var html = kendo.template($('#ac-add-cerificate-template').html())({});
        container.append(html);
        kendo.init(container);
        container.find('[type=file]').eq(-1).kendoUpload(uploadsettings)
    });

    $('body').delegate('.ac-remove-cerificate','click',function(){
        $(this).closest('.flat-list').remove();
    });

    kendo.init('body');
    $('[type=file]').kendoUpload(uploadsettings)
})
