### ${apiName}

${methodName} ${url}
<#if headers??>
    <#list headers as header>
${header.key}: ${header.value}
    </#list>
</#if>

${requestBody!''}

<#if responseFiles??>
    <#list responseFiles as responsefile>
<> ${responsefile}
    </#list>
</#if>


