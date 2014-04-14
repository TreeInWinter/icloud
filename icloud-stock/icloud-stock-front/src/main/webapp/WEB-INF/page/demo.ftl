<#import "/template/htmlBase.ftl" as html/>
<#import "/template/headBase.ftl" as head/>
<#import "/template/spring.ftl" as spring/>

<@html.htmlBase>
	<@head.headBase>
		<script src="${basepath}/resources/javascripts/common/demo.js" type="text/javascript"></script>
	</@head.headBase>
	<#escape x as x?html>
		<p>freemarker 测试！</p>
		</br>
		
		<p>----------------保存hotel--------------</p>
		<form action="${basepath}/demo/saveHotel" method="post">
			<p>名称:<@spring.formInput "demo.name"/>  <@spring.showErrors "" /> </p>
			<p>酒店code:<@spring.formInput "demo.code"/> <@spring.showErrors "" /> </p>
			<p>内部name:<@spring.formInput "demo.nested.nestedName"/> <@spring.showErrors "" /> </p>
			<p><input  type="submit" value="提交"></p>
		</form>
		
		
		<p>----------------查询hotel--------------</p>
		<#if hotels??>
		<#list hotels as hotels>
		  <p>酒店code:${hotels.propertyCode}-------酒店名称:${hotels.name}</p>
		   <#assign x = "Hello ${hotels.propertyCode}">
		</#list>
		</#if>
	</#escape>
</@html.htmlBase>


