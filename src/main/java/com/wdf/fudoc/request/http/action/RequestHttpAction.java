package com.wdf.fudoc.request.http.action;

import com.intellij.httpClient.http.request.HttpRequestPsiFile;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.wdf.fudoc.apidoc.constant.enumtype.JavaClassType;
import com.wdf.fudoc.request.http.FuHttpClient;
import com.wdf.fudoc.request.http.FuRequest;
import com.wdf.fudoc.request.http.dto.HttpRecentDTO;
import com.wdf.fudoc.request.http.impl.FuHttpClientImpl;
import com.wdf.fudoc.request.http.impl.FuRequestImpl;
import com.wdf.fudoc.storage.FuRequestStorage;
import com.wdf.fudoc.util.FuRequestUtils;
import com.wdf.fudoc.util.PsiClassUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 发起http请求
 *
 * @author wangdingfu
 * @date 2023-05-19 22:41:12
 */
public class RequestHttpAction extends AnAction {

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        //获取来源文件
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
        if (Objects.isNull(psiFile)) {
            return;
        }
        FuRequest fuRequest = null;
        if (psiFile instanceof HttpRequestPsiFile) {
            //从.http文件或者是.rest文件发起请求
            fuRequest = new FuRequestImpl(new FuHttpClientImpl(project, (HttpRequestPsiFile) psiFile, psiElement));
        } else if (psiFile instanceof PsiJavaFile) {
            //从Controller文件的方法体上发起请求
            fuRequest = genFuRequest(project, (PsiJavaFile) psiFile, psiElement);
        }
        //如果以上途径都无法获取到FuRequest对象 则默认去请求记录中寻找最近一次的请求并组装成FuRequest对象
        if (Objects.isNull(fuRequest)) {
            HttpRecentDTO recentRecord = FuRequestStorage.readRecent(project);
            if (Objects.isNull(recentRecord)) {
                return;
            }
            PsiClass psiClass = PsiClassUtils.findJavaFile(project, recentRecord.getControllerPkg());
            PsiMethod targetMethod = PsiClassUtils.findTargetMethod(psiClass, recentRecord.getMethodName(), recentRecord.getMappingUrl());
            if (Objects.isNull(targetMethod)) {
                return;
            }
            //搜索httpClient
            FuHttpClient fuHttpClient = FuRequestStorage.read(project, psiClass, targetMethod);
            fuRequest = new FuRequestImpl(psiClass, targetMethod, fuHttpClient, project);
        }

        //开始弹框展示http请求窗体
    }



    private FuRequest genFuRequest(Project project, PsiJavaFile psiJavaFile, PsiElement psiElement) {
        PsiClass psiClass = psiJavaFile.getClasses()[0];
        //光标所在的方法体
        PsiMethod targetMethod;
        if (JavaClassType.CONTROLLER.equals(JavaClassType.get(psiClass))
                && Objects.nonNull(targetMethod = PsiClassUtils.getTargetMethod(psiElement))
                && FuRequestUtils.isHttpMethod(targetMethod)) {
            //搜索httpClient
            FuHttpClient fuHttpClient = FuRequestStorage.read(project, psiClass, targetMethod);
            return new FuRequestImpl(psiClass, targetMethod, fuHttpClient, project);
        }
        return null;
    }


}
