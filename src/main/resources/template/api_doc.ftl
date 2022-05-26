<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    <title>核对结果</title>

</head>

<body>
<#if (itemList?size > 0 )>
    <#list itemList as item>
        接口参数为:
        ```
        ${item.requestExample}
        ```


        接口结果为:
        ```
        ${item.responseExample}
        ```
    </#list >
</#if>
</body>

</html>