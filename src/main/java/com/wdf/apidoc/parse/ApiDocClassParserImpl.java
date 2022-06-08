package com.wdf.apidoc.parse;

import com.google.common.collect.Lists;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.wdf.apidoc.constant.enumtype.JavaClassType;
import com.wdf.apidoc.helper.DocCommentParseHelper;
import com.wdf.apidoc.parse.field.ApiDocPsiClass;
import com.wdf.apidoc.parse.field.ApiDocPsiParameter;
import com.wdf.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.apidoc.pojo.context.ApiDocContext;
import com.wdf.apidoc.pojo.data.AnnotationData;
import com.wdf.apidoc.pojo.data.ApiDocCommentData;
import com.wdf.apidoc.pojo.desc.ClassInfoDesc;
import com.wdf.apidoc.pojo.desc.MethodInfoDesc;
import com.wdf.apidoc.pojo.desc.ObjectInfoDesc;
import com.wdf.apidoc.util.AnnotationUtils;
import com.wdf.apidoc.util.FuDocUtils;
import com.wdf.apidoc.util.PsiClassUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @descption: java类解析实现类
 * @date 2022-05-08 22:38:21
 */
public class ApiDocClassParserImpl implements ApiDocClassParser {

    /**
     * 解析java类 将java类的一些信息解析封装
     *
     * @param apiDocContext 全局上下文
     * @param psiClass      指定类的PsiClass
     * @param methodList    指定方法集合(为空则当前类所有方法都解析)
     * @return java类信息描述对象
     */
    @Override
    public ClassInfoDesc parse(ApiDocContext apiDocContext, PsiClass psiClass, List<PsiMethod> methodList) {
        ClassInfoDesc classInfoDesc = new ClassInfoDesc();
        if (Objects.nonNull(apiDocContext) && Objects.nonNull(psiClass)) {
            JavaClassType javaClassType = JavaClassType.get(psiClass);
            List<MethodInfoDesc> methodInfoDescList = Lists.newArrayList();
            //注解解析
            classInfoDesc.setAnnotationDataMap(annotationParse(psiClass));
            classInfoDesc.setMethodList(methodInfoDescList);
            classInfoDesc.setCommentData(DocCommentParseHelper.parseComment(psiClass.getDocComment()));
            for (PsiMethod method : psiClass.getMethods()) {
                if (!FuDocUtils.isValidMethod(javaClassType, method)) {
                    //过滤不需要解析的方法
                    continue;
                }
                if (CollectionUtils.isEmpty(methodList) || methodList.contains(method)) {
                    ApiDocCommentData apiDocCommentData = DocCommentParseHelper.parseComment(method.getDocComment());
                    MethodInfoDesc methodInfoDesc = new MethodInfoDesc();
                    //设置方法上的注释
                    methodInfoDesc.setCommentData(apiDocCommentData);
                    //设置方法上注解
                    methodInfoDesc.setAnnotationDataMap(annotationParse(method));
                    //设置请求参数
                    methodInfoDesc.setRequestList(requestParse(apiDocContext, method, apiDocCommentData));
                    //设置响应参数
                    methodInfoDesc.setResponse(responseParse(apiDocContext, method, apiDocCommentData));
                    methodInfoDescList.add(methodInfoDesc);
                }
            }
        }
        return classInfoDesc;
    }

    /**
     * 请求参数转换器
     *
     * @param apiDocContext 全局上下文
     * @param psiMethod     指定的方法
     * @return 解析后的请求参数
     */
    protected List<ObjectInfoDesc> requestParse(ApiDocContext apiDocContext, PsiMethod psiMethod, ApiDocCommentData apiDocCommentData) {
        PsiParameterList parameterList = psiMethod.getParameterList();
        List<ObjectInfoDesc> requestList = Lists.newArrayList();
        for (PsiParameter parameter : parameterList.getParameters()) {
            ParseObjectBO parseObjectBO = new ParseObjectBO();
            parseObjectBO.setApiDocField(new ApiDocPsiParameter(parameter, apiDocCommentData));
            parseObjectBO.setApiDocContext(apiDocContext);
            ObjectInfoDesc objectInfoDesc = ObjectParserExecutor.execute(parameter.getType(), parseObjectBO);
            if (Objects.nonNull(objectInfoDesc)) {
                requestList.add(objectInfoDesc);
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
    protected ObjectInfoDesc responseParse(ApiDocContext apiDocContext, PsiMethod psiMethod, ApiDocCommentData apiDocCommentData) {
        ParseObjectBO parseObjectBO = new ParseObjectBO();
        parseObjectBO.setApiDocContext(apiDocContext);
        PsiType returnType = psiMethod.getReturnType();
        if (PsiClassUtils.isVoid(returnType)) {
            //响应类型为void  则不解析
            return null;
        }
        PsiClass psiClass = PsiUtil.resolveClassInType(returnType);
        if (Objects.nonNull(psiClass)) {
            parseObjectBO.setApiDocField(new ApiDocPsiClass(psiClass, apiDocCommentData));
        }
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
            return AnnotationUtils.parse(modifierList.getAnnotations());
        }
        return new HashMap<>();
    }
}
