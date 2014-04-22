Array.prototype.inArray = function(p_val){

    for(var i = 0, l = this.length; i < l; i++) {
		if(this[i] == p_val) {
			return true;
		}
	}
	return false;

};
var dataSource = {};

dataSource.hotel = new kendo.data.DataSource({
		data: [
        {
			id: 1,
			city: "上海1",
			position: "闸北区",
			name: "宝华国际酒店",
			star: "5",
			brand: "宝华国际",
			sign_manager: "张三"
		},
        {
			id: 2,
			city: "上海2",
			position: "闸北区",
			name: "宝华国际酒店",
			star: "5",
			brand: "宝华国际",
			sign_manager: "张三"
		},
        {
			id: 3,
			city: "上海3",
			position: "闸北区",
			name: "宝华国际酒店",
			star: "5",
			brand: "宝华国际",
			sign_manager: "张三"
		},
        {
			id: 4,
			city: "上海4",
			position: "闸北区",
			name: "宝华国际酒店",
			star: "5",
			brand: "宝华国际",
			sign_manager: "张三"
		},
        {
			id: 5,
			city: "上海5",
			position: "闸北区",
			name: "宝华国际酒店",
			star: "5",
			brand: "宝华国际",
			sign_manager: "张三"
		},
        {
			id: 6,
			city: "上海6",
			position: "闸北区",
			name: "宝华国际酒店",
			star: "5",
			brand: "宝华国际",
			sign_manager: "张三"
		},
        {
			id: 7,
			city: "上海7",
			position: "闸北区",
			name: "宝华国际酒店",
			star: "5",
			brand: "宝华国际",
			sign_manager: "张三"
		},
        {
			id: 8,
			city: "上海8",
			position: "闸北区",
			name: "宝华国际酒店",
			star: "5",
			brand: "宝华国际",
			sign_manager: "张三"
		},
        {
			id: 9,
			city: "上海9",
			position: "闸北区",
			name: "宝华国际酒店",
			star: "5",
			brand: "宝华国际",
			sign_manager: "张三"
		},
        {
			id: 10,
			city: "上海10",
			position: "闸北区",
			name: "宝华国际酒店",
			star: "5",
			brand: "宝华国际",
			sign_manager: "张三"
		},
        {
			id: 11,
			city: "上海11",
			position: "闸北区",
			name: "宝华国际酒店",
			star: "5",
			brand: "宝华国际",
			sign_manager: "张三"
		}

        ],
        page:1,
        pageSize: 2
	}
);
dataSource.customer = new kendo.data.DataSource({
		data: [
        {
			id: 1,
			city: "上海1",
			name: "大有票务1",
			create_time: "2013-02-01",
			account_type: "月结",
			blance: "20000.00",
            link_man:"张三"
		},
        {
			id: 2,
			city: "上海2",
			name: "大有票务2",
			create_time: "2013-02-01",
			account_type: "月结",
			blance: "20000.00",
            link_man:"张三"
		},
        {
			id: 3,
			city: "上海3",
			name: "大有票务3",
			create_time: "2013-02-01",
			account_type: "月结",
			blance: "20000.00",
            link_man:"张三"
		},
        {
			id: 4,
			city: "上海4",
			name: "大有票务4",
			create_time: "2013-02-01",
			account_type: "月结",
			blance: "20000.00",
            link_man:"张三"
		}
        ],
        page:1,
        pageSize: 2
	}
);

/* 已排除列表 */
var filterd_data = {
    city:[],
    hotel:[],
    customer:[]
};

var filter_win = { };

var exclude_item_tpl = kendo.template($("#exclude_item_tpl").html());  

function select_item(obj){
    var self_name = $(obj).attr("name"); 
    var ds = dataSource[self_name];
    var data = ds.data(); 
    var data_offset = (ds.page() - 1) * ds.pageSize();
    var index = $(obj).closest("tr").index() + data_offset;
    var item = data[index]; 

    var f = $(obj).closest(".filter").find(".filtered_data");

    if($(obj).prop("checked")){
        var node = exclude_item_tpl({id:item.id,group:self_name,name:item.name,city:item.city});
        if(!filterd_data[self_name].inArray(item.id)){
            filterd_data[self_name].push(item.id);
            f.append(node);
        }
    }else{
        if(filterd_data[self_name].inArray(item.id)){

            for(var i =0,l = filterd_data[self_name].length; i < l; i++){
                if(filterd_data[self_name][i] == item.id){
                    break;
                }
            }
            var a = filterd_data[self_name].slice(0,i);
            var b = filterd_data[self_name].slice(i + 1);
            filterd_data[self_name] = a.concat(b); 
            $("#" + self_name + item.id).remove();
        }
    }

    console.log(filterd_data[self_name]);
};

$(function() {

    /****************************************************************/

    var model=kendo.observable({
        city:[{name:"ABCDEF",data:[{name:"上海"},{name:"山东"},{name:"山西"},{name:"北京"},{name:"成都"},{name:"大连"},{name:"福建"}]},{name:"GHIJKL",data:[{name:"广州"}]}],
        deletedCity:[{name:"成都"}],
        deletingCity:[],
        clearDeletingCity:function(){
            $("#location-layer li.item").removeClass("selected");
            this.deletingCity.splice(0,this.deletingCity.length);
        }
    })



    function checkCityStatus(selectCity){
        for(var i=0;i<model.deletingCity.length;i++)
        {
            if(model.deletingCity[i].name==selectCity.name)
                return i;
        }
        return -1;
    }


    function renderCityTab(el,modifiable){
        kendo.destroy($(el));
        $(el).html("");
        var el_ul=$("<ul></ul>");
        $(el).append(el_ul);
        for(var i=0;i<model.city.length;i++)
        {
            var cityHeadTpl=new kendo.View("cityHead",{model:model.city[i],tagName:"li"});
            el_ul.append(cityHeadTpl.render());
            var cityItems=new kendo.View("cityContainer",{model:model.city[i]});
            $(el).append(cityItems.render());
        }
        kendo.init($(el));
        $(el).data("kendoTabStrip").select(0);
        if(modifiable)
        {

            $("#location-layer").delegate("li.item","click",function(){
                $(this).toggleClass("selected")
                var selectCity=model.city[$(this).closest("div.tcy-tabitem").parent().index()-1].data[$(this).closest("li").index()];
                var result=checkCityStatus(selectCity);
                if(result!=-1)
                {
                    model.deletingCity.splice(result,1);
                }
                else
                {
                    model.deletingCity.push(selectCity);
                }
            })
            clearArrayData(model.deletingCity);
            cloneData(model.deletedCity,model.deletingCity);
            initCityStatus();
        }
    }

    function initCityStatus(){
        $("#location-layer li.item").each(function(){
            for(var i=0;i<model.deletingCity.length;i++)
            {
                if(model.deletingCity[i].name==$(this).text())
                    $(this).addClass("selected");
            }
        })
    }

    function cloneData(data1,data2)
    {
        for(var i=0;i<data1.length;i++)
        {
            data2.push(data1[i]);
        }
    }

    function clearArrayData(data)
    {
        data.splice(0,data.length);
    }

    filter_win["city"] =new PopWindow("#filter_city", {
        title: "选择排除城市",
        width: 560,
        height:300,
        template: "#citySelectTemplete",
        activate:function(){
            //
        },
        open: function() {
            renderCityTab($("#location-layer"),true);
            kendo.bind($("#deletingCityContainer,.bindModel"),model);
        }
    }).init();

    new PopWindow("#btn_checkCity", {
        title: "选择排除城市",
        width: 560,
        height:300,
        template: "#cityCheckTemplete",
        activate:function(){
            //
        },
        open: function() {
            renderCityTab($("#cityCheck-layer"),false);
        }
    }).init();

    /****************************************************************/


    filter_win["hotel"] = new PopWindow("#filter_hotel", {
		title: "勾选排除酒店",
		width: 730,
		template: "#filter_hotel_tpl",
        activate:function(){
            //
        },
		open: function() {
			hotel_table = new GridTable('#hotel_list', {
				dataSource:dataSource['hotel'],
                dataBounding:function(e){
                },
				dataBound: function(e) {
                    $(".filter tbody input[name=hotel]").each(function(index){
                        if(filterd_data["hotel"].inArray($(this).val())){
                            $(this).prop("checked",true);
                        }
                    });

					filter_win["hotel"].center();
				},
				rowTemplate: kendo.template($("#hotel_row_tpl").html())
			}).init();
		}
	}).init();
    filter_win["checkhotel"] = new PopWindow("#check_hotel", {
		title: "勾选排除酒店",
		width: 730,
		template: "#check_hotel_tpl",
        activate:function(){
            //
        },
		open: function() {
			hotel_table = new GridTable('#checkhotel_list', {
				dataSource:dataSource['hotel'],
                dataBounding:function(e){
                },
				dataBound: function(e) {
                   filter_win["checkhotel"].center();
				},
				rowTemplate: kendo.template($("#checkhotel_row_tpl").html())
			}).init();
		}
	}).init();

	filter_win["customer"] = new PopWindow("#filter_customer", {
		title: "勾选排除客户",
		width: 730,
		template: "#filter_customer_tpl",
        activate:function(){
            //
        },
		open: function() {
			hotel_table = new GridTable('#customer_list', {
				dataSource:dataSource['customer'],
                dataBounding:function(e){
                },
				dataBound: function(e) {
                    $(".filter tbody input[name=customer]").each(function(index){
                        if(filterd_data["customer"].inArray($(this).val())){
                            $(this).prop("checked",true);
                        }
                    });

					filter_win["customer"].center();
				},
				rowTemplate: kendo.template($("#customer_row_tpl").html())
			}).init();
		}
	}).init();
	filter_win["checkcustomer"] = new PopWindow("#check_customer", {
		title: "勾选排除客户",
		width: 730,
		template: "#check_customer_tpl",
        activate:function(){
            //
        },
		open: function() {
			hotel_table = new GridTable('#checkcustomer_list', {
				dataSource:dataSource['customer'],
                dataBounding:function(e){
                },
				dataBound: function(e) {
                    filter_win["checkcustomer"].center();
				},
				rowTemplate: kendo.template($("#checkcustomer_row_tpl").html())
			}).init();
		}
	}).init();

	$("body").delegate("a.clean_data", "click", function() {
		/* clean data */
        var name = $(this).attr("name");
        $(this).closest(".filter").find(".filtered_data").html("");
        filterd_data[name].length = 0;

        $(".filter .filter_num").html(filterd_data[name].length);
        $(".filter input[name=" + name + "]").prop("checked",false);
        $(".filter input[name=" + name + "_checkall]").prop("checked",false);
	});


    $("body").delegate(".filter .btns a","click",function(){

        var index = $(this).index();
        var name = $(this).attr("name");
        var group = name.substring(0,name.indexOf('_'));

        if(0 == index){
            //保存按钮
            $(this).closest(".filter").find(".item_data").appendTo("." + group + "_wrapper");
        }
        filter_win[group].close();
        
    });

    $("body").delegate(".filter table input[type=checkbox]","click",function(){
        var checkbox_name = $(this).attr("name"); 
        var check_status = $(this).prop("checked");
        if(/checkall/.test(checkbox_name)){
            //全选
            checkbox_name = checkbox_name.replace('_checkall','');

            $(".filter tbody input[name=" + checkbox_name + "]").each(function(){
                $(this).prop("checked",check_status);
                select_item(this);
            });

        }else{
            // 单条
            select_item(this);
        }

        $(".filter .filter_num").html(filterd_data[checkbox_name].length);

    });
});

