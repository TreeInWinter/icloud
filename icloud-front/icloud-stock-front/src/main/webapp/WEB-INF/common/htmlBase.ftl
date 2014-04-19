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
	<script type="text/javascript" src="${staticHost}/js/json2.js?v=${getStaticResourceVersion()}"></script>

	<script type="text/javascript" src="${staticHost}/js/kendo.web.js?v=${getStaticResourceVersion()}"></script>
	<script type="text/javascript" src="${staticHost}/js/i18n/jquery.ui.datepicker-zh-CN.js?v=${getStaticResourceVersion()}"></script>
	<script type="text/javascript" src="${staticHost}/js/global.js?v=${getStaticResourceVersion()}"></script>
	<script type="text/javascript" src="${staticHost}/js/store/store.js?v=${getStaticResourceVersion()}"></script>
	<!--[if IE]>
	<script src="${staticHost}/js/html5.js?v=${getStaticResourceVersion()}"></script>
	<script src="${staticHost}/js/vendor/IE7.min.js?v=${getStaticResourceVersion()}"></script>
	<![endif]-->

	<script src="${staticHost}/js/jquery-ui-1.10.3.custom.js?v=${getStaticResourceVersion()}"></script>
	<script src="${staticHost}/js/i18n/jquery.ui.datepicker-zh-CN.js?v=${getStaticResourceVersion()}"></script>

	<#-- global js vars -->
	<script type="text/javascript">
		var basepath = '${basepath}';
		var mediaserver = '${mediaserver}';
		var purchaserServer = '${purchaserServer}';
		var host = '${staticHost}';
		var isLogin = <#if currentUser()??> true <#else> false </#if>;
		var username= '${currentUser().username!}';
		var userKey= '${currentUser().userData.key!}';
		var sessionId='${(session.id)!}';
	</script>

	<script src="${basepath}/resources/javascripts/common/date.js?v=${getStaticResourceVersion()}"></script>
	<script src="${basepath}/resources/javascripts/common/Ratio-0.4.0.min.js?v=${getStaticResourceVersion()}"></script>


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

	<#--Log 日志-->
	<script src="${basepath}/resources/javascripts/common/log.js?v=${getStaticResourceVersion()}"></script>
	<script src="${basepath}/resources/javascripts/common/init.js?v=${getStaticResourceVersion()}"></script>
	<script type="text/javascript" src="${basepath}/resources/javascripts/common/comet.js?v=${getStaticResourceVersion()}"></script>


</head>
<body>
	<#nested/>
</body>
</html>
</#escape>
</#macro>