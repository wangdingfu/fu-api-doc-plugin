
# fu-api-doc-plugin

[![JetBrains Plugins](https://img.shields.io/jetbrains/plugin/v/19269-fu-doc.svg)](https://plugins.jetbrains.com/plugin/19269-fu-doc)Cancel changes
[![Version](http://phpstorm.espend.de/badge/19269/version)](https://plugins.jetbrains.com/plugin/19269-fu-doc/versions)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/19269-fu-doc.svg)](https://plugins.jetbrains.com/plugin/19269-fu-doc)
[![License](https://img.shields.io/badge/license-MIT-red.svg)]()


> 这是一个idea插件 支持根据java代码一键生成markdown格式的接口文档 没有任何额外开发工作量 当然 前提是需要你有一个良好的编码习惯


演示
---
![演示](https://user-images.githubusercontent.com/100477650/171110724-8a653d36-ee3d-4337-a662-1dc68d400e98.gif)

![插件截图1](https://user-images.githubusercontent.com/100477650/171110675-0822fee0-7a3e-4c59-b7cc-d645ac9feaee.png)

![插件截图2](https://user-images.githubusercontent.com/100477650/171110766-40aa0c8e-20ae-4e7c-a3d1-152a99d71811.png)

![插件生成的接口文档](https://user-images.githubusercontent.com/100477650/171110794-b1aacc55-8ca1-4795-a018-9e429b62fa9f.png)


安装步骤
---

- **在线安装:**
    - `File` -> `Setting` -> `Plugins` -> 搜索 `FuDoc`

- **手动安装:**
    - [下载插件](https://github.com/wangdingfu/fu-api-doc-plugin/releases") 
    - 进入插件市场安装本地插件： `File` -> `Setting` -> `Plugins`
      -> `Install Plugin from Disk...`

使用
----

- 快捷键`ALT+D`
- 右键菜单选择 `生成接口文档`

注意事项
---

- 鼠标需要在Controller类代码块内 否则有可能会获取不到当前类导致无法生成接口文档
- 当鼠标停留在方法体内或则选中方法一部分内容在点击生成接口文档. 则只会生成当前方法的接口文档
