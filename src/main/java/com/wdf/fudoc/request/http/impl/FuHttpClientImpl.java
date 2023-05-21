package com.wdf.fudoc.request.http.impl;


import com.intellij.httpClient.http.request.HttpRequestPsiFile;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.request.http.FuHttpClient;

/**
 * @author wangdingfu
 * @date 2023-05-21 23:10:52
 */
public class FuHttpClientImpl implements FuHttpClient {

    private final HttpRequestPsiFile httpRequestPsiFile;

    private final Project project;

    private final PsiElement psiElement;

    public FuHttpClientImpl(HttpRequestPsiFile httpRequestPsiFile, Project project, PsiElement psiElement) {
        this.httpRequestPsiFile = httpRequestPsiFile;
        this.project = project;
        this.psiElement = psiElement;
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
