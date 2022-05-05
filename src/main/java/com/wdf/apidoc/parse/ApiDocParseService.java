package com.wdf.apidoc.parse;

import com.intellij.psi.PsiClass;
import com.wdf.apidoc.pojo.context.ApiDocContext;
import com.wdf.apidoc.pojo.data.ApiDocData;

import java.util.List;

/**
 * 解析java类(Controller | Feign | Dubbo) 将java类中的数据转换为生成接口文档需要的数据
 *
 * @author wangdingfu
 * @Date 2022-04-21 20:21:50
 */
public interface ApiDocParseService {

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
