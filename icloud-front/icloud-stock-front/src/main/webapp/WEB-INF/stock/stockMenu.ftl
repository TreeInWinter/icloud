<#import "/icloud/icloud-main-container.ftl" as imc/>
<#import "/stock/stock-template/stock-menu.ftl" as sm/>
<#import "/stock/stock-template/stock-list.ftl" as sl/>
<@imc.mainContainer current = "上海股票" jsFiles=["icloud/menu.js"] cssFiles=["icloud/stock.css"]>
	<@sm.sMenu current="你好"/>
	<@sl.sList current="你好"/>
</@imc.mainContainer>