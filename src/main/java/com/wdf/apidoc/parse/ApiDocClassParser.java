package com.wdf.apidoc.parse;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.wdf.apidoc.pojo.context.ApiDocContext;
import com.wdf.apidoc.pojo.desc.ClassInfoDesc;

import java.util.List;

/**
 * 该接口主要用于解析java类 将Java类的一些信息解析封装成公共的ClasInfoDesc对象
 *
 * @author wangdingfu
 * @descption: 类解析器
 * @date 2022-05-08 22:34:17
 */
public interface ApiDocClassParser {


    /**
     * 解析java类 将java类的一些信息解析封装
     *
     * @param apiDocContext 全局上下文
     * @param psiClass      指定类的PsiClass
     * @param methodList    指定方法集合(为空则当前类所有方法都解析)
     * @return java类信息描述对象
     */
    ClassInfoDesc parse(ApiDocContext apiDocContext, PsiClass psiClass, List<PsiMethod> methodList);
}
