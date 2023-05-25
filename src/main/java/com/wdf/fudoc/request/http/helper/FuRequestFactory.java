package com.wdf.fudoc.request.http.helper;

import com.google.common.collect.Lists;
import com.intellij.httpClient.http.request.HttpRequestPsiFile;
import com.intellij.httpClient.http.request.psi.HttpRequest;
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
import com.wdf.fudoc.util.AnnotationUtils;
import com.wdf.fudoc.util.FuRequestUtils;
import com.wdf.fudoc.util.PsiClassUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-05-23 20:48:25
 */
public class FuRequestFactory {

    public static FuRequest createFuRequest(AnActionEvent e) {
        //获取来源文件
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        if (Objects.isNull(psiFile)) {
            return null;
        }
        if (psiFile instanceof HttpRequestPsiFile) {
            return create(e, (HttpRequestPsiFile) psiFile);
        } else if (psiFile instanceof PsiJavaFile) {
            return create(e, (PsiJavaFile) psiFile);
        }
        return create(e);
    }


    public static FuRequest create(AnActionEvent e, HttpRequestPsiFile httpRequestPsiFile) {
        //从.http文件或者是.rest文件发起请求
        HttpRequest httpRequest = FuRequestUtils.getHttpRequest(httpRequestPsiFile, e.getData(LangDataKeys.EDITOR));
        if (Objects.nonNull(httpRequest)) {
            return new FuRequestImpl(new FuHttpClientImpl(e.getProject(), httpRequest));
        }
        return null;
    }

    public static FuRequest create(AnActionEvent e, PsiJavaFile psiJavaFile) {
        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
        Project project = e.getProject();
        //从Controller文件的方法体上发起请求
        PsiClass psiClass = psiJavaFile.getClasses()[0];
        //光标所在的方法体
        PsiMethod targetMethod;
        if (JavaClassType.CONTROLLER.equals(JavaClassType.get(psiClass)) && Objects.nonNull(targetMethod = PsiClassUtils.getTargetMethod(psiElement))) {
            List<String> mappingUrlList = PsiClassUtils.findMappingUrlList(targetMethod);
            if (CollectionUtils.isNotEmpty(mappingUrlList)) {
                //搜索httpClient
                FuHttpClient fuHttpClient = FuRequestStorage.read(project, psiClass, targetMethod, mappingUrlList);
                return new FuRequestImpl(psiClass, targetMethod, fuHttpClient, e.getProject());
            }
        }
        return create(e);
    }


    public static FuRequest create(AnActionEvent e) {
        //如果以上途径都无法获取到FuRequest对象 则默认去请求记录中寻找最近一次的请求并组装成FuRequest对象
        Project project = e.getProject();
        HttpRecentDTO recentRecord = FuRequestStorage.readRecent(project);
        if (Objects.isNull(recentRecord)) {
            return null;
        }
        PsiClass psiClass = PsiClassUtils.findJavaFile(project, recentRecord.getControllerPkg());
        PsiMethod targetMethod = PsiClassUtils.findTargetMethod(psiClass, recentRecord.getMethodName(), recentRecord.getMappingUrl());
        if (Objects.isNull(targetMethod)) {
            return null;
        }
        //搜索httpClient
        FuHttpClient fuHttpClient = FuRequestStorage.read(project, psiClass, targetMethod, Lists.newArrayList(recentRecord.getMappingUrl()));
        return new FuRequestImpl(psiClass, targetMethod, fuHttpClient, project);
    }

}
