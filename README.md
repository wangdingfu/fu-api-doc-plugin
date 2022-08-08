
# fu-api-doc-plugin

[![JetBrains Plugins](https://img.shields.io/jetbrains/plugin/v/19269-fu-doc.svg)](https://plugins.jetbrains.com/plugin/19269-fu-doc)Cancel changes
[![Version](http://phpstorm.espend.de/badge/19269/version)](https://plugins.jetbrains.com/plugin/19269-fu-doc/versions)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/19269-fu-doc.svg)](https://plugins.jetbrains.com/plugin/19269-fu-doc)
[![License](https://img.shields.io/badge/license-MIT-red.svg)]()


> Fu Doc是一个根据 `JAVA` 代码一键生成 `markdown` 格式接口文档的 Idea 插件。你只需要在你的开发工具里安装上Fu Doc这个插件即可，对你的项目完全零侵入。当然需要有一个良好的开发习惯（写注释的好习惯），此时你只需要一个快捷键就可以生成你平时需要耗费大量时间去复制粘贴编写的接口文档



演示
---
![演示](https://user-images.githubusercontent.com/100477650/171110724-8a653d36-ee3d-4337-a662-1dc68d400e98.gif)

![插件截图1](https://user-images.githubusercontent.com/100477650/171110675-0822fee0-7a3e-4c59-b7cc-d645ac9feaee.png)

![插件截图2](https://user-images.githubusercontent.com/100477650/171111420-cc94d6de-7d83-4132-a97a-23b1ab6c5408.png)

![插件生成的接口文档](https://user-images.githubusercontent.com/100477650/171110794-b1aacc55-8ca1-4795-a018-9e429b62fa9f.png)


安装步骤
---

- **在线安装:**
    - `File` -> `Setting` -> `Plugins` -> 搜索 `Fu Doc`

- **手动安装:**
    - [下载插件（Github下载,速度慢）](https://github.com/wangdingfu/fu-api-doc-plugin/releases)
    - [下载插件（蓝奏云下载,速度快）](https://wwi.lanzoup.com/b0dy2hktg) 密码：`8vec`
    - [下载插件（百度云下载）](https://pan.baidu.com/s/1cC7thCMMdcRjh24sqU59tA?pwd=8888) 密码：`8888`
    - 进入插件市场安装本地插件： `File` -> `Setting` -> `Plugins`
      -> `Install Plugin from Disk...`
      

使用
----

- 快捷键`ALT+D`
- 右键菜单选择 `Fu Doc`
- 去接口文档系统直接将内容粘贴即可（例如ShowDoc或者YApi等）

未来版本计划
----

- 支持Feign、Dubbo类生成接口文档
- 一键将接口文档同步至ShowDoc、YApi、第三方文档系统（提供统一对外api）
- 支持自定义高级扩展配置（例如解析自定义注解在接口文档中展示等扩展功能）
- 支持接口模板动态配置

其他
---

- Intellij Idea 版本支持2018.1 以上
- 鼠标需要在Controller类代码块内 否则有可能会获取不到当前类导致无法生成接口文档
- 当鼠标停留在方法体内或则选中方法一部分内容在点击生成接口文档. 则只会生成当前方法的接口文档
- 小伙伴们如果使用有任何问题可以给我提Issues. 我会及时回复并解决
