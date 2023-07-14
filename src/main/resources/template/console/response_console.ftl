
${httpType!'HTTP/1.1'} ${httpStatus!''}
<#if headers??>
    <#list headers as header>
${header.key!''}: ${header.value!''}
    </#list>
</#if>

