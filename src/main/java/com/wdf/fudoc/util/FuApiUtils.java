package com.wdf.fudoc.util;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.java.stubs.index.JavaAnnotationIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.pojo.data.AnnotationData;
import com.wdf.fudoc.apidoc.pojo.data.annotation.AnnotationArrayValueData;
import com.wdf.fudoc.common.enumtype.ControllerAnnotation;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-05-26 11:21:01
 */
public class FuApiUtils {

    /**
     * 获取所有Controller
     *
     * @param project 当前项目
     * @return 所有的controller
     */
    public static List<PsiClass> findAllController(Project project, GlobalSearchScope searchScope) {
        List<PsiClass> psiClassList = Lists.newArrayList();
        for (ControllerAnnotation value : ControllerAnnotation.values()) {
            Collection<PsiAnnotation> psiAnnotations = JavaAnnotationIndex.getInstance().get(value.getName(), project, searchScope);
            if (CollectionUtils.isNotEmpty(psiAnnotations)) {
                for (PsiAnnotation psiAnnotation : psiAnnotations) {
                    String qualifiedName = psiAnnotation.getQualifiedName();
                    if (!value.getQualifiedName().equals(qualifiedName)) {
                        continue;
                    }
                    PsiModifierList psiModifierList = (PsiModifierList) psiAnnotation.getParent();
                    PsiElement psiElement = psiModifierList.getParent();
                    if (Objects.isNull(psiElement) || !(psiElement instanceof PsiClass)) {
                        continue;
                    }
                    psiClassList.add((PsiClass) psiElement);
                }
            }
        }
        return psiClassList;
    }


    public static List<String> getClassUrl(PsiClass psiClass) {
        AnnotationData annotationData = AnnotationUtils.parse(psiClass.getAnnotation(AnnotationConstants.REQUEST_MAPPING));
        if (Objects.nonNull(annotationData)) {
            return annotationData.array().constant().stringValue();
        }
        return Lists.newArrayList();
    }

    public static RequestType getMethodUrl(PsiMethod psiMethod, List<String> urlList) {
        for (String mapping : AnnotationConstants.MAPPING) {
            AnnotationData annotationData = AnnotationUtils.parse(psiMethod.getAnnotation(mapping));
            if (Objects.isNull(annotationData)) {
                continue;
            }
            urlList.addAll(annotationData.array().constant().stringValue());
            RequestType requestType = RequestType.getByAnnotationName(mapping);
            if(AnnotationConstants.REQUEST_MAPPING.equals(mapping)){
                //分析RequestMapping注解
                AnnotationArrayValueData.ArrayEnumValue enumValue = annotationData.array("method").enumValue();
                if(!AnnotationConstants.HTTP_METHOD.equals(enumValue.enumClassName())){
                    return requestType;
                }
                List<String> enumValueList = enumValue.enumValueList();
                if(enumValue.isEmpty() || CollectionUtils.isEmpty(enumValueList)){
                    //如果没有指定请求方法 则默认为GET
                    requestType = RequestType.GET;
                }else{
                    requestType = RequestType.getRequestType(enumValueList.get(0));
                }
            }
            return requestType;
        }
        return null;
    }


    /**
     * 拼接请求地址(将controller上的请求地址和方法上的请求地址拼接成一个完成的请求地址)
     *
     * @param controllerUrls controller上的请求地址集合
     * @param methodUrlList  方法体上的请求地址
     * @return 该请求存在的请求地址集合
     */
    public static List<String> joinUrl(List<String> controllerUrls, List<String> methodUrlList) {
        if (CollectionUtils.isEmpty(controllerUrls)) {
            return methodUrlList;
        }
        List<String> urlList = Lists.newArrayList();
        for (String controllerUrl : controllerUrls) {
            for (String methodUrl : methodUrlList) {
                urlList.add(PathUtils.urlJoin(controllerUrl, methodUrl));
            }
        }
        return urlList;
    }

}
