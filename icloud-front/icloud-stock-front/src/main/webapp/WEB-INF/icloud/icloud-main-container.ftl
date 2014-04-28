<#import "/common/htmlBase.ftl" as hb/>
<#import "/icloud/icloud-header.ftl" as ih/>
<#macro mainContainer current="" title="必有股票-可能是最懂股票的网站"  jsFiles=[] cssFiles=[]>
<@hb.htmlBase title=title jsFiles=jsFiles cssFiles=cssFiles  emedObjects=[]>
<div class="main-wrapper">
<@ih.header current = current/>
<section class="main-body clearfix">
	<#nested/>
</section>
<#include "/icloud/icloud-footer.ftl"/>
</div>
</@hb.htmlBase>
</#macro>