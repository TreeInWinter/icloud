/// <reference path="jquery.min.js" />

$.extend({
    //加载kendoGrid
    LoadGrid: function(divID, url, columns, pageSize) {
        var dataSource = new kendo.data.DataSource({
            transport: {
                read: {
                    type: "POST",
                    url: url,
                    dataType: "JSON"
                },
                parameterMap: function(options, operation) {
                    if (operation == "read") {
                        var parameter = {
                            page: options.page,    //当前页
                            pageSize: options.pageSize//每页显示个数

                        };
                        return kendo.stringify(parameter);
                    }
                }
            },
            pageSize: pageSize, //每页显示个数
            schema: {
                data: function(d) {
                    return d.Data;  //响应到页面的数据
                },
                total: function(d) {
                    return d.totalCount;   //总条数
                }
            },
            serverPaging: true,
            serverFiltering: true,
            serverSorting: true
        });

        $("#" + divID).kendoGrid({
            dataSource: dataSource,
            pageable: {
                messages: {
                    display: "{0} - {1} 共 {2} 条数据",
                    empty: "没有要显示的数据",
                    page: "Page",
                    of: "of {0}", // {0} is total amount of pages
                    itemsPerPage: "items per page",
                    first: "首页",
                    previous: "前一页",
                    next: "下一页",
                    last: "最后一页",
                    refresh: "刷新"
                }
            },
            columns: columns,
            sortable: true
        })
    },
    createWindow: function(divID, title, width, openFun) {
        $("#" + divID).kendoWindow({
            title: title,
            modal: true,
            visible: false,
            resizable: false,
            width: width,
            open: openFun //解决打开window后重新加载kendo的问题
            //actions: ["Custom", "Minimize", "Maximize", "Close"] 大小控制
        });
        return $("#" + divID).data("kendoWindow");
    },
	
	autoComplete:function(data,id) {
			$("#"+id).kendoAutoComplete({
                        dataSource: data,
                        filter: "startswith",
                        placeholder: "",
                        separator: ""
                    });
		}
});









