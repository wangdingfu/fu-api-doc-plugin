package com.wdf.apidoc.helper;

import com.intellij.psi.PsiAnnotation;
import com.wdf.apidoc.pojo.context.ApiDocContext;
import com.wdf.apidoc.pojo.data.AnnotationData;

import java.util.HashMap;
import java.util.Map;

/**
 * @descption: 注解解析帮助类
 * @author wangdingfu
 * @date 2022-04-05 21:19:50
 */
public class AnnotationParseHelper {


    /**
     * 解析注解
     *
     * @param apiDocContext     全局上下文
     * @param psiAnnotations 注解集合
     * @return 解析后的注解
     */
    public static Map<String, AnnotationData> parse(ApiDocContext apiDocContext, PsiAnnotation[] psiAnnotations) {
        Map<String, AnnotationData> annotationDataMap = new HashMap<>();
        return annotationDataMap;
    }
}
