package com.wdf.fudoc.util;

import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.apidoc.constant.AnnotationConstants;

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
        for (String mapping : AnnotationConstants.MAPPING) {
            if (psiMethod.hasAnnotation(mapping)) {
                return true;
            }
        }
        return false;
    }
}
