<#macro sMenu current>
<div class="leftMenu">
    <div id="sidebar-menu">
        <ul>
        <#if (mainMenus)??>
          <#list mainMenus as baseStockMenu>
             <li class="first" id="${(baseStockMenu.code)!''}">
                <div class="menu-item">
                    <span>${(baseStockMenu.name)!''}</span>
                </div>
				<input name="menu-id" type="hidden" value="${(baseStockMenu.code)!''}"/>
                <div class="menu-panel">
                </div>
              </li>
           </#list>
         </#if>
        </ul>
    </div>
</div>
</#macro>