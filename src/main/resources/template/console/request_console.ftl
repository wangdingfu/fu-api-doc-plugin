
${methodName} ${url}
<#if headers??>
    <#list headers as header>
${header.key!''}: ${header.value!''}
    </#list>
</#if>


