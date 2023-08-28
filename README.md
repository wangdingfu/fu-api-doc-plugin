
# fu-api-doc-plugin

[![JetBrains Plugins](https://img.shields.io/jetbrains/plugin/v/19269-fu-doc.svg)](https://plugins.jetbrains.com/plugin/19269-fu-doc)
[![Version](http://phpstorm.espend.de/badge/19269/version)](https://plugins.jetbrains.com/plugin/19269-fu-doc/versions)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/19269-fu-doc.svg)](https://plugins.jetbrains.com/plugin/19269-fu-doc)
[![License](https://img.shields.io/badge/license-MIT-red.svg)]()


> Fu Doc是一个根据 `JAVA` 代码一键生成接口文档，并且支持同步接口文档到`ApiFox`、`YApi`、`ShowDoc`等第三方文档系统，支持一键发起HTTP请求的 IDEA插件。你只需要在你的开发工具里安装上Fu Doc这个插件即可，他对你的项目完全零侵入。

查看[Fu Doc官方文档](http://www.fudoc.cn/)详细了解

![img.png](/img/guide.png)


快速开始
----

##### 生成接口文档
- 快捷键`ALT+D` 或则 右键菜单选择 `Fu Doc`
- 去接口文档系统直接将内容粘贴即可（例如ShowDoc或者YApi等）


##### 快速同步接口文档
- 配置文档系统地址（已配置直接跳过该步骤）
- 快捷键`ALT+S` 或则 右键菜单选择 `Fu Doc Sync`--->`Sync Api`
- 在弹框中选中需要同步到文档系统的哪个目录 点击确定即可同步至第三方文档系统


##### 快速调试接口
- 快捷键`ALT+R` 或则 右键菜单选择 `Fu Request`
- 在弹出窗体中发起请求即可

##### 快速搜索接口
- 快捷键`CTRL+Alt+\` 或则 `ALT+\` 或则 双击 `Shift` -> 点击`Fu Api`
- 输入接口地址即可快速搜索


##### 代码补全(对象拷贝)
> 示例：需要将A对象拷贝至B对象

- **通过`A.`唤醒代码补全列表，搜索`beanCopy`并回车**
- **通过`beanCopy.`唤醒代码补全列表, 选择`B`变量并回车**


演示
---

**生成接口文档**
![演示](https://user-images.githubusercontent.com/100477650/171110724-8a653d36-ee3d-4337-a662-1dc68d400e98.gif)

**发起接口请求**
![img.png](/img/request.png)


**快速搜素接口**
![img.png](/img/search.png)


安装步骤
---

- **在线安装:**
![img.png](img/install.png)
    - `File` -> `Setting` -> `Plugins` -> 搜索 `fudoc`

- **手动安装:**
    - [下载插件（Github下载,速度慢）](https://github.com/wangdingfu/fu-api-doc-plugin/releases)
    - [下载插件（蓝奏云下载,速度快）](https://wwi.lanzoup.com/b0dy2hktg) 密码：`8vec`
    - [下载插件（百度云下载）](https://pan.baidu.com/s/1cC7thCMMdcRjh24sqU59tA?pwd=8888) 密码：`8888`
    - 进入插件市场安装本地插件： `File` -> `Setting` -> `Plugins` -> `Install Plugin from Disk...`
      


未来目标
----
- 支持快速调试Spring容器中所有对象的方法(解放编写单元测试)
- 支持团队协作

其他
---
- 仅支持IDEA 2020.2以上的版本
- 鼠标需要在Controller类代码块内 否则有可能会获取不到当前类导致无法生成接口文档
- 当鼠标停留在方法体内或则选中方法一部分内容在点击生成接口文档. 则只会生成当前方法的接口文档
- 小伙伴们如果使用有任何问题可以给我提Issues. 我会及时回复并解决
