<ul>
 <#if (menuList)??>
   <#list menuList as stockMenuBean>
    <li>
        <h2>${(stockMenuBean.name)!''}</h2>
        <#if (stockMenuBean.menus)??>
         <div class="link-list">
        <#list stockMenuBean.menus as menu>
            <div class="link-wrapper">
                <span>|</span>
                <a href="${(menu.code)!''}">${(menu.name)!''}</a>
            </div>
        </#list>
         </div>
		</#if>
        <div class="clear"></div>
    </li>
 </#list>
 </#if>
</ul>