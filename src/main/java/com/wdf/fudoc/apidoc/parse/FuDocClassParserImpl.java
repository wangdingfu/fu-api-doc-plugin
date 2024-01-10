package com.wdf.fudoc.apidoc.parse;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import com.wdf.fudoc.apidoc.mock.real.JsonRealDataHandler;
import com.wdf.fudoc.apidoc.mock.real.KeyValueRealDataHandler;
import com.wdf.fudoc.apidoc.mock.real.MockRealData;
import com.wdf.fudoc.apidoc.parse.field.FuDocPsiClass;
import com.wdf.fudoc.apidoc.parse.field.FuDocPsiParameter;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.apidoc.constant.enumtype.JavaClassType;
import com.wdf.fudoc.apidoc.constant.enumtype.ParamType;
import com.wdf.fudoc.apidoc.helper.DocCommentParseHelper;
import com.wdf.fudoc.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.AnnotationData;
import com.wdf.fudoc.apidoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.apidoc.pojo.desc.ClassInfoDesc;
import com.wdf.fudoc.apidoc.pojo.desc.MethodInfoDesc;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;
import com.wdf.fudoc.common.exception.FuDocException;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.request.manager.FuRequestManager;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestBodyData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.request.pojo.FuResponseData;
import com.wdf.fudoc.util.AnnotationUtils;
import com.wdf.fudoc.util.FuDocUtils;
import com.wdf.api.util.ProjectUtils;
import com.wdf.fudoc.util.PsiClassUtils;
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
            classInfoDesc.setPsiClass(psiClass);
            //注解解析
            classInfoDesc.setAnnotationDataMap(annotationParse(psiClass));
            classInfoDesc.setMethodList(methodInfoDescList);
            classInfoDesc.setCommentData(DocCommentParseHelper.parseComment(psiClass));
            for (PsiMethod method : psiClass.getMethods()) {
                if (!FuDocUtils.isValidMethod(javaClassType, method)) {
                    //过滤不需要解析的方法
                    continue;
                }
                if (CollectionUtils.isEmpty(methodList) || methodList.contains(method)) {
                    ApiDocCommentData apiDocCommentData = DocCommentParseHelper.parseComment(method);
                    MethodInfoDesc methodInfoDesc = new MethodInfoDesc();
                    methodInfoDesc.setPsiMethod(method);
                    methodInfoDesc.setMethodId(PsiClassUtils.getMethodId(method));
                    //设置方法上的注释
                    methodInfoDesc.setCommentData(apiDocCommentData);
                    //设置方法上注解
                    methodInfoDesc.setAnnotationDataMap(annotationParse(method));
                    //【Fu Request】发起请求的真实数据
                    FuHttpRequestData requestData = FuRequestManager.getRequest(ProjectUtils.getCurrProject(), FuDocUtils.genApiKey(method));
                    //设置请求参数
                    methodInfoDesc.setRequestList(requestParse(fuDocContext, method, apiDocCommentData, Objects.nonNull(requestData) ? requestData.getRequest() : null));
                    //设置响应参数
                    methodInfoDesc.setResponse(responseParse(fuDocContext, method, apiDocCommentData, Objects.nonNull(requestData) ? requestData.getResponse() : null));
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
    protected List<ObjectInfoDesc> requestParse(FuDocContext fuDocContext, PsiMethod psiMethod, ApiDocCommentData apiDocCommentData, FuRequestData fuRequestData) {
        PsiParameterList parameterList = psiMethod.getParameterList();
        List<ObjectInfoDesc> requestList = Lists.newArrayList();
        //从【Fu Request】模块中获取当前接口实际请求示例数据
        MockRealData keyValueRealData = buildKeyValueParamRealData(fuRequestData);
        for (PsiParameter parameter : parameterList.getParameters()) {
            MockRealData mockRealData = (parameter.getAnnotation(AnnotationConstants.REQUEST_BODY) != null) ? buildJsonRealData(parameter.getName(), fuRequestData) : keyValueRealData;
            ParseObjectBO parseObjectBO = new ParseObjectBO(fuDocContext, mockRealData);
            parseObjectBO.setFuDocField(new FuDocPsiParameter(parameter, apiDocCommentData));
            parseObjectBO.setParamType(ParamType.REQUEST_PARAM);
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

    private MockRealData buildJsonRealData(String fieldName, FuRequestData requestData) {
        FuRequestBodyData body;
        if (Objects.nonNull(requestData) && Objects.nonNull(body = requestData.getBody())) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOnce(fieldName, JSONUtil.parse(body.getJson()));
            return new JsonRealDataHandler(jsonObject);
        }
        return null;
    }

    /**
     * 构建key-value格式的mock数据
     *
     * @param requestData 接口真实请求数据
     * @return 真实数据mock对象
     */
    private MockRealData buildKeyValueParamRealData(FuRequestData requestData) {
        List<KeyValueTableBO> paramDataList = Lists.newArrayList();
        if (Objects.nonNull(requestData)) {
            //将请求参数分成两类 一类是key value格式的 一类是json或者raw格式的
            List<KeyValueTableBO> params = requestData.getParams();
            List<KeyValueTableBO> pathVariables = requestData.getPathVariables();
            if (CollectionUtils.isNotEmpty(params)) {
                paramDataList.addAll(params);
            }
            if (CollectionUtils.isNotEmpty(pathVariables)) {
                paramDataList.addAll(pathVariables);
            }
            FuRequestBodyData body = requestData.getBody();
            if (Objects.nonNull(body)) {
                List<KeyValueTableBO> formDataList = body.getFormDataList();
                List<KeyValueTableBO> formUrlEncodedList = body.getFormUrlEncodedList();
                if (CollectionUtils.isNotEmpty(formDataList)) {
                    paramDataList.addAll(formDataList);
                }
                if (CollectionUtils.isNotEmpty(formUrlEncodedList)) {
                    paramDataList.addAll(formUrlEncodedList);
                }
            }
        }
        return new KeyValueRealDataHandler(paramDataList);
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
    protected ObjectInfoDesc responseParse(FuDocContext fuDocContext, PsiMethod psiMethod, ApiDocCommentData apiDocCommentData, FuResponseData response) {
        PsiType returnType = psiMethod.getReturnType();
        PsiClass psiClass = PsiUtil.resolveClassInType(returnType);
        if (Objects.isNull(psiClass)) {
            //响应类型为void 直接返回null
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce(psiClass.getName(), Objects.nonNull(response) ? JSONUtil.parse(response.getContent()) : null);
        ParseObjectBO parseObjectBO = new ParseObjectBO(fuDocContext, new JsonRealDataHandler(jsonObject));
        parseObjectBO.setFuDocField(new FuDocPsiClass(psiClass, apiDocCommentData));
        parseObjectBO.setParamType(ParamType.RESPONSE_PARAM);
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
