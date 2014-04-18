<#macro htmlBase>
<#escape x as x?html> 
<#escape x as x?html> 
<!doctype html>
	<html>
		<body>
			<#nested>
			</body>
	</html>
</#escape>
</#escape>
</#macro>
