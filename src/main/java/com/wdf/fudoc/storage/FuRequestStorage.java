package com.wdf.fudoc.storage;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.request.http.FuHttpClient;
import com.wdf.fudoc.request.http.dto.HttpRecentDTO;
import org.apache.commons.lang3.StringUtils;

/**
 * 【Fu Request】模块持久化
 * <p>
 * 持久化目录：${baseHome}/fudoc/${projectName}/${moduleName}/${controllerName}/${methodName}.http
 *
 * @author wangdingfu
 * @date 2023-05-21 23:26:16
 */
public class FuRequestStorage {

    /**
     * 读取HttpClient文件 并返回该对象
     *
     * @param psiClass  Controller
     * @param psiMethod 对应的接口方法体
     * @return 该接口对应http请求对象
     */
    public static FuHttpClient read(Project project, PsiClass psiClass, PsiMethod psiMethod) {
        return null;
    }


    /**
     * 读取最近一次请求记录
     *
     * @param project 当前项目
     * @return 最近一次请求记录 ${ControllerPath}#${methodName}#${mappingUrl}
     */
    public static HttpRecentDTO readRecent(Project project) {

        return null;
    }
}
