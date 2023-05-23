package com.wdf.fudoc.request.http.impl;


import com.intellij.httpClient.http.request.HttpRequestPsiFile;
import com.intellij.httpClient.http.request.psi.HttpRequest;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.request.http.FuHttpClient;
import com.wdf.fudoc.util.FuRequestUtils;

/**
 * @author wangdingfu
 * @date 2023-05-21 23:10:52
 */
public class FuHttpClientImpl implements FuHttpClient {

    private final HttpRequestPsiFile httpRequestPsiFile;

    private final Project project;

    private final HttpRequest httpRequest;

    public FuHttpClientImpl(Project project, HttpRequestPsiFile httpRequestPsiFile, PsiElement psiElement) {
        this.httpRequestPsiFile = httpRequestPsiFile;
        this.project = project;
        this.httpRequest = FuRequestUtils.getHttpRequest(httpRequestPsiFile, psiElement);
    }


    @Override
    public Project getProject() {
        return this.project;
    }

    @Override
    public PsiClass getPsiClass() {
        return null;
    }

    @Override
    public PsiMethod getPsiMethod() {
        return null;
    }
}
