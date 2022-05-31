
# fu-api-doc-plugin

[![JetBrains Plugins](https://img.shields.io/jetbrains/plugin/v/19269-fu-doc.svg)](https://plugins.jetbrains.com/plugin/19269-fu-doc)
[![Version](http://phpstorm.espend.de/badge/19269/version)](https://plugins.jetbrains.com/plugin/19269-fu-doc/versions)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/19269-fu-doc.svg)](https://plugins.jetbrains.com/plugin/19269-fu-doc)
[![License](https://img.shields.io/badge/license-MIT-red.svg)]()


> 这是一个idea插件 支持根据java代码一键生成markdown格式的接口文档 没有任何额外开发工作量 当然 前提是需要你有一个良好的编码习惯


演示
---


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