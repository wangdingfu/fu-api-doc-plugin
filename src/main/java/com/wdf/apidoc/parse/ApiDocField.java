package com.wdf.apidoc.parse;

import com.intellij.psi.PsiAnnotation;

/**
 * @author wangdingfu
 * @Descption
 * @Date 2022-04-28 22:18:28
 */
public interface ApiDocField {

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



}
