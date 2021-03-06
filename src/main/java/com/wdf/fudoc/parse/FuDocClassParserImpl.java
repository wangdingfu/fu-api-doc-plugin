package com.wdf.fudoc.parse;

import com.google.common.collect.Lists;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.wdf.fudoc.constant.FuDocConstants;
import com.wdf.fudoc.constant.enumtype.JavaClassType;
import com.wdf.fudoc.helper.DocCommentParseHelper;
import com.wdf.fudoc.parse.field.FuDocPsiClass;
import com.wdf.fudoc.parse.field.FuDocPsiParameter;
import com.wdf.fudoc.pojo.bo.ParseObjectBO;
import com.wdf.fudoc.pojo.context.FuDocContext;
import com.wdf.fudoc.pojo.data.AnnotationData;
import com.wdf.fudoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.pojo.desc.ClassInfoDesc;
import com.wdf.fudoc.pojo.desc.MethodInfoDesc;
import com.wdf.fudoc.pojo.desc.ObjectInfoDesc;
import com.wdf.fudoc.util.AnnotationUtils;
import com.wdf.fudoc.util.FuDocUtils;
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
public class FuDocClassParserImpl implements FuDocClassParser {

    /**
     * 解析java类 将java类的一些信息解析封装
     *
     * @param fuDocContext 全局上下文
     * @param psiClass     指定类的PsiClass
     * @param methodList   指定方法集合(为空则当前类所有方法都解析)
     * @return java类信息描述对象
     */
    @Override
    public ClassInfoDesc parse(FuDocContext fuDocContext, PsiClass psiClass, List<PsiMethod> methodList) {
        ClassInfoDesc classInfoDesc = new ClassInfoDesc();
        if (Objects.nonNull(fuDocContext) && Objects.nonNull(psiClass)) {
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
                    methodInfoDesc.setMethodId(psiClass.getQualifiedName() + "#" + method.getName());
                    //设置方法上的注释
                    methodInfoDesc.setCommentData(apiDocCommentData);
                    //设置方法上注解
                    methodInfoDesc.setAnnotationDataMap(annotationParse(method));
                    //设置请求参数
                    methodInfoDesc.setRequestList(requestParse(fuDocContext, method, apiDocCommentData));
                    //设置响应参数
                    methodInfoDesc.setResponse(responseParse(fuDocContext, method, apiDocCommentData));
                    methodInfoDescList.add(methodInfoDesc);
                }
            }
        }
        classInfoDesc.setClassId(psiClass.getQualifiedName());
        return classInfoDesc;
    }

    /**
     * 请求参数转换器
     *
     * @param fuDocContext 全局上下文
     * @param psiMethod    指定的方法
     * @return 解析后的请求参数
     */
    protected List<ObjectInfoDesc> requestParse(FuDocContext fuDocContext, PsiMethod psiMethod, ApiDocCommentData apiDocCommentData) {
        PsiParameterList parameterList = psiMethod.getParameterList();
        List<ObjectInfoDesc> requestList = Lists.newArrayList();
        for (PsiParameter parameter : parameterList.getParameters()) {
            ParseObjectBO parseObjectBO = new ParseObjectBO(fuDocContext);
            parseObjectBO.setFuDocField(new FuDocPsiParameter(parameter, apiDocCommentData));
            ObjectInfoDesc objectInfoDesc = ObjectParserExecutor.execute(parameter.getType(), parseObjectBO);
            if (Objects.nonNull(objectInfoDesc)) {
                //标识根节点
                objectInfoDesc.addExtInfo(FuDocConstants.ExtInfo.ROOT, true);
                //填充referenceDescId
                paddingReferenceDescId(objectInfoDesc, null);
                requestList.add(objectInfoDesc);
            }
        }
        return requestList;
    }


    /**
     * 递归填充referenceDescId
     *
     * @param objectInfoDesc  参数描述对象
     * @param referenceDescId 当前参数引用的descId
     */
    private void paddingReferenceDescId(ObjectInfoDesc objectInfoDesc, Integer referenceDescId) {
        List<ObjectInfoDesc> childList = objectInfoDesc.getChildList();
        if (CollectionUtils.isNotEmpty(childList)) {
            for (ObjectInfoDesc infoDesc : childList) {
                if (Objects.nonNull(referenceDescId) && infoDesc.getBooleanValue(FuDocConstants.ExtInfo.IS_ATTR)) {
                    infoDesc.addExtInfo(FuDocConstants.ExtInfo.REFERENCE_DESC_ID, referenceDescId);
                }
                paddingReferenceDescId(infoDesc, infoDesc.getDescId());
            }
        }
    }


    /**
     * 解析响应参数
     *
     * @param fuDocContext 全局上下文
     * @param psiMethod    指定的方法
     * @return 解析后的响应参数对象
     */
    protected ObjectInfoDesc responseParse(FuDocContext fuDocContext, PsiMethod psiMethod, ApiDocCommentData apiDocCommentData) {
        ParseObjectBO parseObjectBO = new ParseObjectBO(fuDocContext);
        PsiType returnType = psiMethod.getReturnType();
        PsiClass psiClass = PsiUtil.resolveClassInType(returnType);
        if (Objects.nonNull(psiClass)) {
            parseObjectBO.setFuDocField(new FuDocPsiClass(psiClass, apiDocCommentData));
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
