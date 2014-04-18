/*@author:wangmeng
 * ajax后台验证
*/
/*
 * 基对象，所有对象继承该对象
 */
function isNull(arg){
	if(arg == null || typeof(arg)=="undefined" ||arg==""){
		return true;
	}else{
		return false;
	}
}
function isNotNull(arg){
	return !isNull(arg);
}
function ValidateElement(name,nullable,inputSelector,errorInfoSelector,value){
	this.inputSelector = inputSelector;
	this.name = name;
	this.nullable = nullable;
	this.errorInfoSelector = errorInfoSelector;
	this.value = value;
}


/*
 * 字符串验证对象
 */
function text(minLength,maxLength,nullable,inputSelector,errorInfoSelector,regex){
	this.inputSelector = inputSelector;
	this.nullable = nullable;
	this.errorInfoSelector = errorInfoSelector;
	this.minLength = minLength;
	this.maxLength = maxLength;
	this.regex = regex;
	this.value = "";
	this.name = "text";
}
//继承基类
text.prototype = new ValidateElement(name,this.nullable,this.inputSelector,this.errorInfoSelector,this.value);

/*
 * 数字对象
 */
function number(min,max,nullable,inputSelector,errorInfoSelector){
	this.inputSelector = inputSelector;
	this.nullable = nullable;
	this.errorInfoSelector = errorInfoSelector;
	this.min = min;
	this.max = max;
	this.value = "";
	this.name = "number";
}
number.prototype = new ValidateElement(name,this.nullable,this.inputSelector,this.errorInfoSelector,this.value);

/*
 *邮箱 
 */
function email(nullable,inputSelector,errorInfoSelector){
	this.inputSelector = inputSelector;
	this.nullable = nullable;
	this.errorInfoSelector = errorInfoSelector;
	this.value = "";
	this.name = "email";
}
email.prototype = new ValidateElement(name,this.nullable,this.inputSelector,this.errorInfoSelector,this.value);

/*
 * 固定电话
 */
function fixedPhone(nullable,inputSelector,errorInfoSelector,format){
	this.inputSelector = inputSelector;
	this.nullable = nullable;
	this.errorInfoSelector = errorInfoSelector;
	this.name = "fixedPhone";
	if(isNull(format)){
		this.format = '*-*';
	}else{
		this.format = format;
	}
	this.value = "";
}
fixedPhone.prototype = new ValidateElement(name,this.nullable,this.inputSelector,this.errorInfoSelector,this.value);

/*
 * 移动电话
 */
function mobilePhone(nullable,inputSelector,errorInfoSelector,length){
	this.inputSelector = inputSelector;
	this.nullable = nullable;
	this.errorInfoSelector = errorInfoSelector;
	this.name = "mobilePhone";
	if(isNull(length)){
		this.length = '11';
	}else{
		this.length = length;
	}
	this.value = "";
}
mobilePhone.prototype = new ValidateElement(name,this.nullable,this.inputSelector,this.errorInfoSelector,this.value);

/*
 * 日期
 */
function date(nullable,inputSelector,errorInfoSelector,dateFormat){
	this.inputSelector = inputSelector;
	this.nullable = nullable;
	this.errorInfoSelector = errorInfoSelector;
	this.name = "date";
	if(isNull(dateFormat)){
		this.dateFormat = "yyyy-MM-dd";
	}else{
		this.dateFormat = dateFormat;
	}
	this.value = "";
}
date.prototype = new ValidateElement(name,this.nullable,this.inputSelector,this.errorInfoSelector,this.value);

/*
 * URL
 */
function url(nullable,inputSelector,errorInfoSelector){
	this.inputSelector = inputSelector;
	this.nullable = nullable;
	this.errorInfoSelector = errorInfoSelector;
	this.value = "";
	this.name = "url"
}
url.prototype = new ValidateElement(name,this.nullable,this.inputSelector,this.errorInfoSelector,this.value);



/**
 * 
 * @param successFunction	验证成功后执行的方法
 * @param formSelector		表单选择器
 * @param validateURL		后台验证url
 * @returns
 */
function AjaxValidation(successFunction,validateURL){
		window.alert(successFunction);
		window.alert(validateURL);
	if(isNull(successFunction)){
		alert("请输入successFunction[验证成功后执行的js函数]");
		return;
	}
	if(!(successFunction instanceof Function)){
		alert("successFunction[验证成功后执行的js函数]必须是函数类型");
		return;
	}
	
	
	if(isNull(validateURL)){
		validateURL = basepath+"/ajaxValidate";	
	}

	var validateElements = [];
	
	this.addValidateElement = function(validateElement){
		if(validateElement instanceof ValidateElement){
			validateElements.push(validateElement);
		}else{
			alert("请填入ValidateElement的实例");
			return;
		}
	};
	var ajaxValidationError = function(data){
		var errorInfo = eval(data);
		for(var i=0;i<errorInfo.length;i++){
			$(errorInfo[i].errorInfoSelector).html(errorInfo[i].errorInfo);
		}
	}
	var ajaxValidationSuccess = function(){
		successFunction();
	}

	this.validate = function(){
		var validateStr = "[";
		for(var i=0;i<validateElements.length;i++){
			var validateElement = validateElements[i];
			validateElement.value = $(validateElement.inputSelector).val();
			
			validateStr = validateStr+JSON.stringify(validateElement)+",";
		}
		
		$.post(validateURL,{"validate":validateStr+"]"},function(data){
			if(data == 'success'){
				ajaxValidationSuccess();
			}else{
				ajaxValidationError(data);
			}
			
		})
		
	}
	
}