<html>
<head>
<script type="text/javascript" src="${req.contextPath}/resources/javascripts/common/jquery-2.0.1.min.js"></script>
</head>
<body>
	<p>----------getDemo：get请求,必须有参数--------------</p>
	<#if arg??>
	${arg}
	</#if>
	<p></p>
	
	<p>----------getDemo1，get请求,参数可以为空--------------</p>
	<#if arg1??>
	${arg1}
	</#if>
	<p></p>
	
	<p>----------getDemo2:post请求--------------</p>
	 <form action="${req.contextPath}/requestmapping/demo2" method="post">
	  demo:<input name="demo"/>
	 	 <#if arg3??>
			${arg3}
		 </#if>
	  <input type="submit" value="提交">
	</form>
	<p></p>
	
	<p>----------getRest:rest风格的请求--------------</p>
	<#if arg4??>
			${arg4}
	</#if>
	<p></p>
	
	<p>----------getRegex:正则表达式请求--------------</p>
	<#if arg5??>
			${arg5}
	</#if>
	<p></p>
	
	<p>----------指定request Content-type类型--------------</p>
	<input type="button" id="ajax" value="指定Content-type" width="100px" height="50"/>
	
	<script type="text/javascript">
		$("#ajax").click(function(){
			$.ajax({
				 type: "POST",
            	 url: "/tops-springmvc/requestmapping/consume",
            	 data:"{name:json}",
            	
            	 success:function(data){
            	 	alert(data);
            	 }
			})
		})
	</script>
	<p></p>
	
	
		<p>----------指定request Accept类型--------------</p>
	<input type="button" id="ajax1" value="指定Accept" width="100px" height="50"/>
	
	<script type="text/javascript">
		$("#ajax1").click(function(){
			$.get("/tops-springmvc/requestmapping/produce", { name: "John"},
			  function(data){
			    alert("Data Loaded: " + data);
			  },"script");
		})
		
		
	</script>
	<p></p>
</body>
</html>











