package com.wdf.fudoc.util;

import com.intellij.httpClient.http.request.HttpRequestPsiFile;
import com.intellij.httpClient.http.request.psi.HttpRequest;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.apidoc.constant.AnnotationConstants;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-05-21 23:37:03
 */
public class FuRequestUtils {

    /**
     * 判断Controller中的方法是否为http接口
     *
     * @param psiMethod 方法体
     * @return true：是http接口
     */
    public static boolean isHttpMethod(PsiMethod psiMethod) {
        return Objects.nonNull(getMapping(psiMethod));
    }

    public static String getMapping(PsiMethod psiMethod) {
        for (String mapping : AnnotationConstants.MAPPING) {
            if (psiMethod.hasAnnotation(mapping)) {
                return mapping;
            }
        }
        return null;
    }

    /**
     * 根据光标位置从.http或.rest文件中读取指定的接口
     *
     * @param httpRequestPsiFile httpClient文件
     * @param psiElement         光标所在的节点
     * @return 光标所在的接口对象
     */
    public static HttpRequest getHttpRequest(HttpRequestPsiFile httpRequestPsiFile, PsiElement psiElement) {
        return null;
    }


    /**
     * 根据url路径从.http或.rest文件中读取指定的接口
     *
     * @param httpRequestPsiFile httpClient文件
     * @param url                接口url
     * @return 接口url对应的接口对象
     */
    public static HttpRequest getHttpRequest(HttpRequestPsiFile httpRequestPsiFile, String url) {
        return null;
    }
}
