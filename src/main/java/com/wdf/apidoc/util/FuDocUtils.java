package com.wdf.apidoc.util;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.wdf.apidoc.constant.AnnotationConstants;
import com.wdf.apidoc.constant.enumtype.JavaClassType;

import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption Fu Doc工具类
 * @Date 2022-06-08 20:16:46
 */
public class FuDocUtils {

    /**
     * 校验一个方法是否需要解析
     *
     * @param psiClass  类对象
     * @param psiMethod 方法对象
     * @return true 需要解析
     */
    public static boolean isNeedParse(PsiClass psiClass, PsiMethod psiMethod) {
        JavaClassType javaClassType = JavaClassType.get(psiClass);
        if (JavaClassType.isNone(javaClassType)) {
            return false;
        }
        return isValidMethod(javaClassType, psiMethod);
    }


    /**
     * 校验java类是否为Controller
     *
     * @param psiClass java类
     * @return true 是一个Controller
     */
    public static boolean isController(PsiClass psiClass) {
        if (Objects.nonNull(psiClass)) {
            for (PsiAnnotation annotation : psiClass.getAnnotations()) {
                if (AnnotationConstants.CONTROLLER.equals(annotation.getQualifiedName())
                        || AnnotationConstants.REST_CONTROLLER.equals(annotation.getQualifiedName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 校验java类是否为Dubbo接口
     *
     * @param psiClass java类
     * @return true 是一个Controller
     */
    public static boolean isDubbo(PsiClass psiClass) {
        return true;
    }

    /**
     * 校验java类是否为Feign接口
     *
     * @param psiClass java类
     * @return true 是一个Controller
     */
    public static boolean isFeign(PsiClass psiClass) {
        if (Objects.nonNull(psiClass)) {
            for (PsiAnnotation annotation : psiClass.getAnnotations()) {
                if (AnnotationConstants.FEIGN_CLIENT.equals(annotation.getQualifiedName())) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 是否为有效的方法(即需要被解析的方法)
     * @param javaClassType 该方法所处的java类 类型
     * @param psiMethod 方法对象
     * @return true 是有效的方法 需要被解析
     */
    public static boolean isValidMethod(JavaClassType javaClassType, PsiMethod psiMethod) {
        switch (javaClassType) {
            case CONTROLLER:
                return isControllerMethod(psiMethod);
            case DUBBO:
            case FEIGN:
                return true;
            case NONE:
            default:
                return false;
        }
    }


    /**
     * 是否为Controller有效的请求方法
     *
     * @param psiMethod 方法对象
     * @return true 是
     */
    public static boolean isControllerMethod(PsiMethod psiMethod) {
        if (Objects.nonNull(psiMethod)) {
            PsiAnnotation[] annotations = psiMethod.getAnnotations();
            if (annotations.length > 0) {
                for (String mapping : AnnotationConstants.MAPPING) {
                    for (PsiAnnotation annotation : annotations) {
                        if (mapping.equals(annotation.getQualifiedName())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
