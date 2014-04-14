//面向对象js,私有属性放入构造函数中
$(function(){
/*	var validatable = $("#form").kendoValidator().data("kendoValidator");
	$("#submit").click(function(){
		if(validatable.validate()){
			$("#form").submit();
		}
	})*/
	
	
	//ajax验证
	var callback = function(){			//定义callback函数
		$("#form").submit();
	};
	var ajaxValidation = new AjaxValidation(callback,basepath+"/form/ajaxValidate");		//实例化验证器
	var name = new text(1,5,false,'#name','#nameerror');									//实例化验证元素对象
	ajaxValidation.addValidateElement(name);												//注册验证元素
	
	$("#submit").click(function(){
		ajaxValidation.validate();															//调用验证方法
	})
})
	

