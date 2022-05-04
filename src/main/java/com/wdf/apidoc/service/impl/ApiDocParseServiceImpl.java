package com.wdf.apidoc.service.impl;

import com.google.common.collect.Lists;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.wdf.apidoc.execute.ObjectParserExecutor;
import com.wdf.apidoc.helper.AnnotationParseHelper;
import com.wdf.apidoc.helper.DocCommentParseHelper;
import com.wdf.apidoc.parse.ApiDocPsiClass;
import com.wdf.apidoc.parse.ApiDocPsiParameter;
import com.wdf.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.apidoc.pojo.context.ApiDocContext;
import com.wdf.apidoc.pojo.data.*;
import com.wdf.apidoc.service.ApiDocParseService;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @Descption Controller解析器
 * @Date 2022-04-21 20:53:55
 */
public class ApiDocParseServiceImpl implements ApiDocParseService {

    /**
     * 解析java类(controller/dubbo接口/feign接口)
     *
     * @param apiDocContext 全局上下文
     * @param psiClass      指定类的PsiClass
     * @param methodList    指定方法集合(为空则当前类所有方法都解析)
     * @return 解析指定java类后的属性数据对象
     */
    @Override
    public ApiDocData parse(ApiDocContext apiDocContext, PsiClass psiClass, List<String> methodList) {
        ApiDocData apiDocData = new ApiDocData();
        if (Objects.nonNull(apiDocContext) && Objects.nonNull(psiClass)) {
            List<ApiDocMethodData> apiDocMethodDataList = Lists.newArrayList();
            //设置类上的注解
            apiDocData.setAnnotationDataMap(annotationParse(psiClass));
            apiDocData.setApiDocMethodDataList(apiDocMethodDataList);
            for (PsiMethod method : psiClass.getMethods()) {
                if (CollectionUtils.isNotEmpty(methodList) && !methodList.contains(method.getName())) {
                    //过滤没有指定的方法
                    continue;
                }
                ApiDocCommentData apiDocCommentData = DocCommentParseHelper.parseComment(method.getDocComment());
                ApiDocMethodData apiDocMethodData = new ApiDocMethodData();
                //设置方法上注解
                apiDocMethodData.setAnnotationDataMap(annotationParse(method));
                //设置请求参数
                apiDocMethodData.setRequestList(requestParse(apiDocContext, method, apiDocCommentData));
                //设置响应参数
                apiDocMethodData.setResponse(responseParse(apiDocContext, method, apiDocCommentData));
                apiDocMethodDataList.add(apiDocMethodData);
            }
        }
        return apiDocData;
    }


    /**
     * 请求参数转换器
     *
     * @param apiDocContext 全局上下文
     * @param psiMethod     指定的方法
     * @return 解析后的请求参数
     */
    protected List<ApiDocObjectData> requestParse(ApiDocContext apiDocContext, PsiMethod psiMethod, ApiDocCommentData apiDocCommentData) {
        PsiParameterList parameterList = psiMethod.getParameterList();
        List<ApiDocObjectData> requestList = Lists.newArrayList();
        for (PsiParameter parameter : parameterList.getParameters()) {
            ParseObjectBO parseObjectBO = new ParseObjectBO();
            parseObjectBO.setApiDocField(new ApiDocPsiParameter(parameter, apiDocCommentData));
            parseObjectBO.setApiDocContext(apiDocContext);
            ApiDocObjectData apiDocObjectData = ObjectParserExecutor.execute(parameter.getType(), parseObjectBO);
            if (Objects.nonNull(apiDocObjectData) && !apiDocObjectData.isFilterObject()) {
                requestList.add(apiDocObjectData);
            }
        }
        return requestList;
    }


    /**
     * 解析响应参数
     *
     * @param apiDocContext 全局上下文
     * @param psiMethod     指定的方法
     * @return 解析后的响应参数对象
     */
    protected ApiDocObjectData responseParse(ApiDocContext apiDocContext, PsiMethod psiMethod, ApiDocCommentData apiDocCommentData) {
        ParseObjectBO parseObjectBO = new ParseObjectBO();
        parseObjectBO.setApiDocContext(apiDocContext);
        PsiType returnType = psiMethod.getReturnType();
        parseObjectBO.setApiDocField(new ApiDocPsiClass(PsiUtil.resolveClassInType(returnType), apiDocCommentData));
        return ObjectParserExecutor.execute(returnType, parseObjectBO);
    }


    /**
     * 注解解析
     *
     * @param psiModifierListOwner psi
     * @return key: 注解名  value:解析后的注解对象
     */
    private Map<String, AnnotationData> annotationParse(PsiModifierListOwner psiModifierListOwner) {
        PsiModifierList modifierList = psiModifierListOwner.getModifierList();
        if (Objects.nonNull(modifierList)) {
            //解析注解
            return AnnotationParseHelper.parse(modifierList.getAnnotations());
        }
        return new HashMap<>();
    }
}
