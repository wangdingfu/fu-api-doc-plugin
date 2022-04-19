package com.github.wangdingfu.fuapidocplugin.services

import com.intellij.openapi.project.Project
import com.github.wangdingfu.fuapidocplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
