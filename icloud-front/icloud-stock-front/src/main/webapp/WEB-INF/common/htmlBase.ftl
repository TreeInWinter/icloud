<#macro htmlBase title jsFiles=[] cssFiles=[] localCssFiles=[] emedObjects=[]  ssl=false isMain=false>
<#assign staticHost=host>
<#escape x as x?html>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" >
	<title>${title}</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<#-- css -->
	<link rel="stylesheet" href="${staticHost}/css/smoothness/jquery-ui-1.10.3.custom.css?v=${getStaticResourceVersion()}" />

	<#list cssFiles as css>
		<link rel="stylesheet" href="${staticHost}/${css}?v=${getStaticResourceVersion()}" />
	</#list>

	<#list localCssFiles as localCss>
		<link rel="stylesheet" href="${basepath}/resources/css/${localCss}?v=${getStaticResourceVersion()}" />
	</#list>

	<#-- js -->
	<script type="text/javascript" src="${staticHost}/js/jquery.min.js?v=${getStaticResourceVersion()}"></script>
	<script type="text/javascript" src="${staticHost}/js/jquery-ui-1.10.3.custom.js?v=${getStaticResourceVersion()}"></script>

	<!--[if IE]>
	<script src="${staticHost}/js/html5.js?v=${getStaticResourceVersion()}"></script>
	<script src="${staticHost}/js/vendor/IE7.min.js?v=${getStaticResourceVersion()}"></script>
	<![endif]-->


	<#-- global js vars -->
	<script type="text/javascript">
		var basepath = '${basepath}';
		var host = '${staticHost}';
		var sessionId='${(session.id)!}';
	</script>

	<#list jsFiles as js>
		<#if js?starts_with('js')>
			<script src="${staticHost}/${js}?v=${getStaticResourceVersion()}"></script>
		<#else>
			<script src="${basepath}/resources/javascripts/${js}?v=${getStaticResourceVersion()}"></script>
		</#if>
	</#list>

	<#if emedObjects??>
	<#list emedObjects as emed>
		<#noescape>${emed}</#noescape>
	</#list>
	</#if>

</head>
<body>
	<#nested/>
</body>
</html>
</#escape>
</#macro>