
<#if requestParams??>
**参数**
|参数名|类型|必选|说明|
|:----    |:---|:----- |-----   |
    <#list requestParams as param>
|${param.paramPrefix!''} ${param.paramName!''} | ${param.paramType!''}  |${param.paramRequire!''} |${param.paramDesc!''}  |
    </#list>
</#if>

