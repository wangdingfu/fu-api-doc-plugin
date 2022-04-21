package com.wdf.apidoc.service.impl;

import com.intellij.psi.*;
import com.wdf.apidoc.execute.ParseObjectExecutor;
import com.wdf.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.apidoc.pojo.context.ApiDocContext;
import com.wdf.apidoc.pojo.data.ApiDocObjectData;
import com.wdf.apidoc.service.AbstractApiDocParseService;

/**
 * @author wangdingfu
 * @Descption Controller解析器
 * @Date 2022-04-21 20:53:55
 */
public class ApiDocParseControllerServiceImpl extends AbstractApiDocParseService {

    /**
     * 请求参数转换器
     *
     * @param apiDocContext 全局上下文
     * @param psiMethod     指定的方法
     * @return 解析后的请求参数
     */
    @Override
    protected ApiDocObjectData requestParse(ApiDocContext apiDocContext, PsiMethod psiMethod) {
        PsiParameterList parameterList = psiMethod.getParameterList();
        //判断当前请求参数是否为@RequestBody注解
        for (PsiParameter parameter : parameterList.getParameters()) {
            PsiAnnotation[] annotations = parameter.getAnnotations();
            ParseObjectBO parseObjectBO = new ParseObjectBO();
            parseObjectBO.setPsiParameter(parameter);
            ApiDocObjectData apiDocObjectData = ParseObjectExecutor.execute(parameter.getType(), parseObjectBO);
            System.out.println(apiDocObjectData);
            return apiDocObjectData;
        }
        return null;
    }


    /**
     * 解析响应参数
     *
     * @param apiDocContext 全局上下文
     * @param psiMethod     指定的方法
     * @return 解析后的响应参数对象
     */
    @Override
    protected ApiDocObjectData responseParse(ApiDocContext apiDocContext, PsiMethod psiMethod) {
        PsiType returnType = psiMethod.getReturnType();
        ApiDocObjectData execute = ParseObjectExecutor.execute(returnType, new ParseObjectBO());
        System.out.println(execute);
        return execute;
    }
}
