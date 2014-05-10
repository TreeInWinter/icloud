<#macro sList current>
<div class="stockList-header">
	<a href="${basepath}/stock/stockMenu">行情</a>&nbsp;>&nbsp;
    <span>${(baseStockMenu.fatherName)!''}</span>&nbsp;>&nbsp;
    <span>${(baseStockMenu.name)!''}</span>
</div>
<#assign cateId="${(cateId)}"/>
<div class="stockList">
    <div class="stockList-content new-portfolio" style="display: block;">
        <table cellspacing="0" cellpadding="0" class="portfolio">
            <thead>
                <tr>
                    <th class="sortable" data-key="symbol" style="min-width: 48px">
                        <s></s>股票代码
                    </th>
                    <th class="sortable" data-key="name" style="min-width: 48px">
                        <s></s>股票名称
                    </th>
                    <th class="sortable" data-key="current" style="min-width: 36px">
                        <s></s>当前价
                    </th>
                    <th class="sortable" data-key="change" style="min-width: 36px">
                        <s></s>涨跌额
                    </th>
                    <th class="sortable desc" data-key="percent" style="min-width: 36px">
                        <s></s>涨跌幅</th>
                    <th class="" data-key="today" style="min-width: 72px">
                        <s></s>当日股价幅度</th>
                    <th class="" data-key="week52" style="min-width: 72px">
                        <s></s>52周股价幅度</th>
                    <th class="sortable" data-key="marketcapital" style="min-width: 24px">
                        <s></s>市值</th>
                    <th class="sortable" data-key="pe_ttm" style="min-width: 36px">
                        <s></s>市盈率
                    </th>
                    <th class="sortable" data-key="volume" style="min-width: 36px">
                        <s></s>成交量
                    </th>
                    <th class="sortable" data-key="amount" style="min-width: 36px">
                        <s></s>成交额
                    </th>
                    <th class="" data-key="hasexist" style="min-width: 24px">
                        <s></s>操作
                    </th>
                </tr>
            </thead>
            <tbody>
            <#if (pagination.data)??>
              <#list pagination.data as stock>
              	<#assign a="stock_index"/>
                <tr class="<#if stock_index%2==0>even_stock_up<#else>odd_stock_up</#if>" code="${(stock.stockCode)!''}">
                    <td>
                        <a target="_blank" href="${(stock.stockCode)!''}">${(stock.stockName)!''}</a>
                    </td>
                    <td>
                        <a target="_blank" href="/S/SZ002387">${(stock.stockAllCode)!''}</a>
                    </td>
                    <td class="stock-color">11.50</td>
                    <td class="stock-color">+0.55</td>
                    <td class="stock-color">+5.02%</td>
                    <td class="stock-color">10.76 - 11.65</td>
                    <td class="stock-color">6.63 - 11.64</td>
                    <td class="nocolor">35.99亿</td>
                    <td class="stock-color">247.01</td>
                    <td class="nocolor">1317.03万</td>
                    <td class="nocolor">1.49亿</td>
                    <td>
                        <a href="#" class="followStock" target="_blank" data-stockid="${(stock.stockCode)!''}" data-stockname="${(stock.stockName)!''}" data-stockcurrent="11.5">
                            <span></span>关注
                        </a>
                    </td>
                </tr>
			</#list>
			</#if>
            </tbody>
        </table>
    </div>
    <#if (pageView)??>
    <div class="pageList">
        <div class="pager-wrapper">
            <ul class="pager">
            	<#if (pageView.prePage)??>
                  <li class="last">
                    <a onclick="stockListloading(${cateId},${(pageView.prePage.pageNo)})" href="#">上一页</a>
                  </li>
                </#if>
                <#if (pageView.firstPage)??>
                	<li>
                        <a onclick="stockListloading(${cateId},${(pageView.firstPage.pageNo)})" href="#">${(pageView.firstPage.pageNo+1)}</a>
                    </li>
                    <li>...</li>
                </#if>
                <#if (pageView.pageList)??>
                	<#list pageView.pageList as pageItem>
                	    <#if pageItem.hasUrl=false>
                		 <li class="active">
                    		<a href="#">${(pageItem.pageNo+1)}</a>
                		 </li>
                		 <#else>
                		   <li>
                              <a onclick="stockListloading('${cateId}','${(pageItem.pageNo)}')" href="#">${(pageItem.pageNo+1)}</a>
                           </li>
                		 </#if>
                	</#list>
                </#if>
                <#if (pageView.lastPage)??>
                    <li>...</li>
                	<li>
                        <a onclick="stockListloading(${cateId},${(pageView.lastPage.pageNo)})" href="#">${(pageView.lastPage.pageNo+1)}</a>
                    </li>
                </#if>
                <#if (pageView.nextPage)??>
                   <li class="next">
                      <a onclick="stockListloading(${(cateId)},${(pageView.nextPage.pageNo)})" href="#">下一页</a>
                   </li>
                </#if>
            </ul>
        </div>
    </div>
    </#if>
</div>
</#macro>