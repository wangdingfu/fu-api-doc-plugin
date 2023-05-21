package com.wdf.fudoc.request.http;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

/**
 * @author wangdingfu
 * @date 2023-05-21 23:09:55
 */
public interface FuHttpClient {

    Project getProject();

    PsiClass getPsiClass();

    PsiMethod getPsiMethod();
}
