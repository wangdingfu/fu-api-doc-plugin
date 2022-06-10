package com.wdf.apidoc.parse.field;

import com.intellij.psi.PsiAnnotation;

/**
 * @author wangdingfu
 * @Descption
 * @Date 2022-04-28 22:18:28
 */
public interface FuDocField {

    /**
     * 获取字段名称
     */
    String getName();


    /**
     * 获取字段注释
     */
    String getComment();


    /**
     * 获取注解集合
     */
    PsiAnnotation[] getAnnotations();


    /**
     * 是否存在修饰属性
     *
     * @param name 修饰关键字 例如 static final
     * @return true 存在
     */
    boolean hasProperty(String name);


}
