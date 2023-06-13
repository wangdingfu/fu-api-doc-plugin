package com.wdf.fudoc.request.http.impl;

import com.intellij.httpClient.http.request.psi.HttpRequest;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.request.http.FuHttpClient;
import com.wdf.fudoc.request.http.FuRequest;
import com.wdf.fudoc.util.FuRequestUtils;

/**
 * @author wangdingfu
 * @date 2023-05-21 22:59:06
 */
public class FuRequestImpl implements FuRequest {

    private final PsiClass psiClass;

    private final PsiMethod psiMethod;

    private final FuHttpClient fuHttpClient;

    private final Project project;


    public FuRequestImpl(PsiClass psiClass, PsiMethod psiMethod, FuHttpClient fuHttpClient, Project project) {
        this.psiClass = psiClass;
        this.psiMethod = psiMethod;
        this.fuHttpClient = fuHttpClient;
        this.project = project;
    }

    /**
     * 获取当前请求记录持久化路径
     *
     * @return http文件路径（${projectPath}/.idea/fudoc/api/${moduleName}/${controllerName}）
     */
    @Override
    public String getPath() {
        return FuRequestUtils.getRequestPath(this.project, this.psiClass);
    }

    @Override
    public String getHttpFileName() {
        return psiMethod.getName();
    }


    @Override
    public String httpContent() {
        HttpRequest httpRequest = fuHttpClient.getHttpRequest();
        return null;
    }
}
