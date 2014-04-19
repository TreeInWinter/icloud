<#macro substring str="" length=10 showtitle=false>
<#if (str?length)==0>
<#return>
</#if>
<#assign len = 0/>
<#assign flag = (str?length)/>
<#list 0..(str?length-1) as i>
<#if !(str[i]?matches("[u00-uFF]"))>
		<#assign len = (len+2)/>
	<#else>
	<#assign len = (len+1)/>
</#if>
<#if (len>length) >
<#assign flag = i/>
<#break>
</#if>
</#list>
		<#if (flag < str?length)>
				<#if showtitle?string == 'true'>
						<span alt="${str}" title="${str}">${str?substring(0,flag)}...</span>
				<#else>
						<span>${str?substring(0,flag)}...</span>
				</#if>
		<#else>
				${str}
		</#if>
</#macro>
