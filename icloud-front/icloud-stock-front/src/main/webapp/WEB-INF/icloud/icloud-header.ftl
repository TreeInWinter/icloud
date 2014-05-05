<#macro header current>
<header class="main-header">
    <div class="header-wrapper"/>
    <div class="main-menu">
       <a class="logo" href="/"><h1>必有网</h1></a>
       <nav class="main-nav">
         <ul class="clearfix">
            <li <#if current=="上海股票">class="first active"</#if>><a herf="#">上海股票</a></li>
            <li <#if current=="深圳股票">class="first active"</#if>><a herf="#">深圳股票</a></li>
            <li <#if current=="历史记录">class="first active"</#if>><a herf="#">历史记录</a></li>
            </ul>
       </nav>
       <nav class="main-links">
          <ul class="clearfix">
            <li class="first"><a href="#">注册</a></li>
            <li><a href="#">登录</a></li>
            <li><span class="hyper-link">
                <u>崔江宁</u>
                <i class="arrow-down"></i>
                <div class="hyper-text">
                    <a href="#">我的应用</a>
                    <a href="#">退出</a>
                </div>
            </span></li>
            </ul>
          </nav>
        </div>
</header>
</#macro>