package com.wdf.apidoc.parse;

import com.intellij.psi.PsiClass;
import com.wdf.apidoc.context.ApiDocContext;
import com.wdf.apidoc.data.ApiDocData;

import java.util.List;

/**
 * @descption: API接口参数解析器
 * @author wangdingfu
 * @date 2022-04-05 20:00:05
 */
public interface ApiDocParse {


    /**
     * 解析指定类的指定方法的参数(请求参数/响应结果)
     *
     * @param apiDocContext 全局上下文
     * @param psiClass 指定类的PsiClass
     * @param methodList    指定方法集合(为空则当前类所有方法都解析)
     * @return 解析生成的API接口文档数据对象
     */
    ApiDocData parse(ApiDocContext apiDocContext, PsiClass psiClass, List<String> methodList);
}
