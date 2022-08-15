package com.wdf.fudoc.constant.enumtype;

import com.intellij.psi.PsiClass;
import com.wdf.fudoc.util.FuDocUtils;

import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption java类 类型
 * @date 2022-06-08 20:23:08
 */
public enum JavaClassType {


    /**
     * Spring Controller访问控制器
     */
    CONTROLLER,

    /**
     * Spring Cloud feign接口
     */
    FEIGN,

    /**
     * 接口
     */
    INTERFACE,

    /**
     * 普通java类
     */
    OBJECT,

    /**
     * 枚举类
     */
    ENUM,

    /**
     * 注解类
     */
    ANNOTATION,

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
                return INTERFACE;
            }
            if (FuDocUtils.isFeign(psiClass)) {
                return FEIGN;
            }
            if(psiClass.isEnum()){
                return ENUM;
            }
            if(psiClass.isAnnotationType()){
                //注解不支持解析
                return NONE;
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

    public static boolean isInterface(JavaClassType javaClassType) {
        return Objects.nonNull(javaClassType) && INTERFACE.equals(javaClassType);
    }

    public static boolean isFeign(JavaClassType javaClassType) {
        return Objects.nonNull(javaClassType) && FEIGN.equals(javaClassType);
    }
}
