<#import "/template/htmlBase.ftl" as html/>
<#import "/template/headBase.ftl" as head/>
<#import "/template/spring.ftl" as spring/>

<#assign spring=JspTaglibs["http://www.springframework.org/tags"]>
<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]> 

<@html.htmlBase>
	<@head.headBase>
		<script src="${basepath}/resources/javascripts/page/demo.js" type="text/javascript"></script>
		<script src="${basepath}/resources/javascripts/common/ajaxValidation.js" type="text/javascript"></script>
	</@head.headBase>
	<#escape x as x?html>
		<p>freemarker 测试！</p>
		</br>
		
		<p>----------------保存hotel--------------</p>
		<@form.form modelAttribute="demo" action="${req.contextPath}/form/saveHotel" id="form">
			<p>名称:<@form.input path="name" class="input_txt span_250" required=true  minLength="1" maxLength="10" type="text"/><font color="red"><span id="nameerror"></span></font>   <@form.errors path="name" htmlEscape="false" />  </p>
			<p>code:<@form.input path="code" class="input_txt span_250" />   <@form.errors path="code" htmlEscape="false" />  </p>
			<p>email:<@form.input path="email" class="input_txt span_250" type="email" />   <@form.errors path="email" htmlEscape="false" />  </p>
			<input type="hidden" name="nested.nestedName" value="a">
			<p><button id="submit" width="50" type="submit">提交</button></p>
		</@form.form>
		
		
		<p>----------------查询hotel--------------</p>
		<#if hotels??>
		<#list hotels as hotels>
		  <p>酒店code:${hotels.propertyCode}-------酒店名称:${hotels.name}</p>
		   <#assign x = "Hello ${hotels.propertyCode}">
		</#list>
		</#if>
	</#escape>
</@html.htmlBase>


