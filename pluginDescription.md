<!-- Plugin description -->
# Generate MarkDown API DOC


[&emsp; GitHub   &emsp;|](https://github.com/wangdingfu/fu-api-doc-plugin)
[&emsp; Document     &emsp;|](https://wangdingfu.github.io/)
[&emsp; Issues   &emsp;|](https://github.com/wangdingfu/fu-api-doc-plugin/issues)
[&emsp; Releases &emsp;|](https://github.com/wangdingfu/fu-api-doc-plugin/releases)

## English introduction
> 【fudoc】is an IDEA plugin that generates API documentation entirely based on code. It supports one-click initiation of HTTP requests to debug APIs and also allows for one-click synchronization of API documentation to third-party systems such as YApi, ShowDoc, and Apifox.

### generate api doc
- 1、press `alt+D` or right click `Fu Doc` in java class
- 2、paste in api doc system(`Show doc` or `YApi` or `MarkDown` or `others` )

### debug api
- press `alt+R` or right click `Fu Request` in java class

### sync api doc
- press `alt+S` or right click`Fu Doc Sync` in java class


### Support
- 1、Support Idea version later than 2020.3.4
- 2、Support `Controller` `Interface` `Object` `Enum` generate markdown api doc
- 3、Support for dynamic templates
- 4、Support for custom configuration of the data you need


## 中文介绍
> 【fudoc】是一个完全基于代码生成接口文档的IDEA插件. 支持一键发起http请求调试接口, 支持一键同步接口文档至YApi、ShowDoc、Apifox等第三方接口文档系统


### 生成接口文档
- 1、在java类中按下快捷键`alt+D` 或者 右键 `Fu Doc`（`光标在指定方法中则会只生成该接口的文档`）.
- 2、查看右下角是否提示当前类已生成接口文档至剪贴板
- 3、直接去自己平时使用的接口文档系统中粘贴接口文档即可


### 调试API接口（一键发起http请求）
- 将光标至于需要调试的接口方法体内按下快捷键`alt+R` 或则右键 `Fu Request`(`可以在任意地方按下该快捷键打开最近一次请求`)
- 在弹出的http请求窗体中修改对应请求数据后点击`Send`按钮发送请求

### 同步接口文档
- 先进入 `Settings`-->`Fu Doc`-->`Http Request`中配置对应第三方文档系统信息
- 在java类中按下快捷键`alt+S` 或则右键`Fu Doc Sync`
- 在弹出的窗体中选择需要将文档同步至哪一个分类下 点击`OK`按钮就会同步文档至第三方文档系统
- 在右下角弹出的同步成功通知中可以点击进入第三方文档系统查看同步后的文档



## 支持功能
- 1、当前只支持Intellij idea 2020.3.4 之后的版本
- 2、支持根据 `Controller` `Interface` `Object` `Enum` 生成markdown格式的接口文档
- 3、支持动态配置接口文档模板
- 4、支持自定义配置你需要的数据
- 5、支持调式接口（一键发起http请求）
- 6、支持同步接口文档至第三方文档系统(目前支持YApi. ShowDoc和ApiFox和语雀正在适配中)


### 其他
- 小伙伴们如果有问题可以直接给我提<a href="https://github.com/wangdingfu/fu-api-doc-plugin/issues">Issues</a>. 我会及时回复并解决


<!-- Plugin description end -->