<html>
<head>
<script type="text/javascript" src="${req.contextPath}/resources/javascripts/common/jquery-2.0.1.min.js"></script>
</head>
<body>
<#if demo1??>	
<p>参数1：${demo1}</p>
</#if>

<#if name??>	
<p>参数2：${name}</p>
</#if>

<#if cookie??>	
<p>cookie：${cookie}</p>
</#if>

<#if encoding??>	
<p>Request Header的Encoding信息：${encoding}</p>
</#if>

<#if requestHeader??>
<p>Request Header信息：${requestHeader}</p>
</#if>

<#if requestBody??>
<p>Request body信息：${requestBody}</p>
</#if>

<p>-----------------绑定bean-------------</p>
<form action="${req.contextPath}/method/demo2" method="post">
<p>name:<input name="name"/> 
<#if (demo2.name)??>
 传入的参数:${demo2.name}
</#if>
</p>
  <p>code:<input name="code"/>
<#if (demo.code)??>
传入的参数:${demo2.code}
</#if></p>
<input type="submit" value="提交"/>
</form>

 <p></p>
</body>
</html>











