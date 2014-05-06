<#import "/icloud/icloud-main-container.ftl" as imc/>
<#import "/stock/stock-template/stock-menu-list.ftl" as sm/>
<#import "/stock/stock-template/stock-list.ftl" as sl/>
<@imc.mainContainer current = "行情" jsFiles=["icloud/menu.js"] cssFiles=["icloud/stock.css"]>
	<@sm.sMenu current=""/>
	<@sl.sList current=""/>
</@imc.mainContainer>