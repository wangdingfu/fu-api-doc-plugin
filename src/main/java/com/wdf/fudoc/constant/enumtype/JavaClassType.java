package com.wdf.fudoc.constant.enumtype;

import com.intellij.psi.PsiClass;
import com.wdf.fudoc.util.FuDocUtils;

import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption java类 类型
 * @Date 2022-06-08 20:23:08
 */
public enum JavaClassType {


    /**
     * Spring Controller访问控制器
     */
    CONTROLLER,

    /**
     * Dubbo接口
     */
    DUBBO,

    /**
     * Spring Cloud feign接口
     */
    FEIGN,

    /**
     * 普通java类
     */
    OBJECT,

    /**
     * 其他类型
     */
    NONE;


    public static JavaClassType get(PsiClass psiClass) {
        if (Objects.nonNull(psiClass)) {
            if (FuDocUtils.isController(psiClass)) {
                return CONTROLLER;
            }
            if (FuDocUtils.isDubbo(psiClass)) {
                return DUBBO;
            }
            if (FuDocUtils.isFeign(psiClass)) {
                return FEIGN;
            }
            return OBJECT;
        }
        return NONE;
    }

    public static boolean isNone(JavaClassType javaClassType) {
        return Objects.isNull(javaClassType) || NONE.equals(javaClassType);
    }

    public static boolean isController(JavaClassType javaClassType) {
        return Objects.nonNull(javaClassType) && CONTROLLER.equals(javaClassType);
    }

    public static boolean isDubbo(JavaClassType javaClassType) {
        return Objects.nonNull(javaClassType) && DUBBO.equals(javaClassType);
    }

    public static boolean isFeign(JavaClassType javaClassType) {
        return Objects.nonNull(javaClassType) && FEIGN.equals(javaClassType);
    }
}
