<#macro headBase>
<head>
	<meta charset="utf-8" />
	<title></title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes" >
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<#global basepath>${req.contextPath}</#global>
	<script type="text/javascript">
		var basepath = '${basepath}';
	</script>
	<script src="${basepath}/resources/javascripts/common/jquery-2.0.1.min.js" type="text/javascript"></script>
	<script src="${basepath}/resources/javascripts/common/String.js" type="text/javascript"></script>
	<script src="${basepath}/resources/javascripts/common/kendo.all.min.js" type="text/javascript"></script>
	<#nested>
</head>
</#macro>