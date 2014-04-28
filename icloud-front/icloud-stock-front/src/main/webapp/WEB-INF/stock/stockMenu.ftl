<#import "/icloud/icloud-main-container.ftl" as imc/>
<#import "/stock/stock-template/stock-menu.ftl" as sm/>
<@imc.mainContainer current = "上海股票" jsFiles=["icloud/menu.js"] cssFiles=["icloud/stock.css"]>
	<@sm.sMenu current="你好"/>
</@imc.mainContainer>