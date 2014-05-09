<#import "/icloud/icloud-main-container.ftl" as imc/>
<#import "/stock/stock-template/stock-menu-list.ftl" as sm/>
<#import "/stock/stock-template/stock-list.ftl" as sl/>
<@imc.mainContainer current = "行情" jsFiles=['icloud/menu.js','icloud/stocklist.js'] cssFiles=["icloud/stock.css"]>
	<@sm.sMenu current=""/>
	<div class="mainPageContent">
		 <div id="stock-list-view">
		 </div>
		 <#include "/stock/stock-template/stock-loading.ftl" />
	</div>
</@imc.mainContainer>
