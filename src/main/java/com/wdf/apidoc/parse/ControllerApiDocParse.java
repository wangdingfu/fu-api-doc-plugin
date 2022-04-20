package com.wdf.apidoc.parse;

import com.intellij.psi.*;
import com.wdf.apidoc.bo.ParseObjectBO;
import com.wdf.apidoc.context.ApiDocContext;
import com.wdf.apidoc.data.ApiDocObjectData;
import com.wdf.apidoc.execute.ParseObjectExecutor;

/**
 * @author wangdingfu
 * @descption: Controller解析器
 * @date 2022-04-05 21:54:12
 */
public class ControllerApiDocParse extends AbstractApiDocParse {

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
