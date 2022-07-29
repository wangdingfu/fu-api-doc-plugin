
<#if itemList??>
<#list itemList as item>`${item.code!''}-${item.msg!''}` </#list>
</#if>

