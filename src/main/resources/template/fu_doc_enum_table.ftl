
<#if title?? >
- ${title!'枚举'}
</#if>


<#if itemList??>
|编码|说明|
|:-----|:-----|
<#list itemList as item>
|${item.code!''}| ${item.msg!''}  |
</#list>
</#if>


