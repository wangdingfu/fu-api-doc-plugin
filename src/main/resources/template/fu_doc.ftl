
### ${docNo!1}、 ${title!''}

<#if detailInfo??>
**接口描述**
- ${detailInfo}
</#if>




<#if urlList?? >
**请求URL**
    <#list urlList as url>
- ` ${url} `

    </#list>
</#if>


**请求方式**
- ${requestType!''}


<#if requestParams??>
**参数**

|参数名|类型|必选|说明|
|:----    |:---|:----- |-----   |
    <#list requestParams as param>
|${param.paramPrefix!''} ${param.paramName!''} | ${param.paramType!''}  |${param.paramRequire!''} |${param.paramDesc!''}  |
    </#list>
</#if>


<#if requestExample??>
**请求示例**

```
${requestExample!''}
```
</#if>


<#if responseParams??>
**返回参数说明**

|参数名|类型|说明|
|:-----  |:-----|----- |
    <#list responseParams as param>
        <#if param.paramName??>
|${param.paramPrefix!''} ${param.paramName!''} | ${param.paramType!''}  |${param.paramDesc!''}   |
        </#if>
    </#list>
</#if>

<#if responseExample??>
**返回示例**

```
${responseExample!''}
```
</#if>


