package com.wdf.fudoc.request.http.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.request.http.FuHttpClient;
import com.wdf.fudoc.request.http.FuRequest;

/**
 * @author wangdingfu
 * @date 2023-05-21 22:59:06
 */
public class FuRequestImpl implements FuRequest {

    private final PsiClass psiClass;

    private final PsiMethod psiMethod;

    private final FuHttpClient fuHttpClient;

    private final Project project;

    public FuRequestImpl(FuHttpClient fuHttpClient) {
        this(fuHttpClient.getPsiClass(), fuHttpClient.getPsiMethod(), fuHttpClient, fuHttpClient.getProject());
    }

    public FuRequestImpl(PsiClass psiClass, PsiMethod psiMethod, FuHttpClient fuHttpClient, Project project) {
        this.psiClass = psiClass;
        this.psiMethod = psiMethod;
        this.fuHttpClient = fuHttpClient;
        this.project = project;
    }


}
