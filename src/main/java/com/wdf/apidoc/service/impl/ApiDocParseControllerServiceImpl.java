package com.wdf.apidoc.service.impl;

import com.google.common.collect.Lists;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.wdf.apidoc.enumtype.RequestType;
import com.wdf.apidoc.execute.ObjectParserExecutor;
import com.wdf.apidoc.helper.AnnotationParseHelper;
import com.wdf.apidoc.parse.ApiDocPsiParameter;
import com.wdf.apidoc.pojo.bo.ParseObjectBO;
import com.wdf.apidoc.pojo.context.ApiDocContext;
import com.wdf.apidoc.pojo.data.AnnotationData;
import com.wdf.apidoc.pojo.data.ApiDocCommentData;
import com.wdf.apidoc.pojo.data.ApiDocObjectData;
import com.wdf.apidoc.service.AbstractApiDocParseService;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    protected List<ApiDocObjectData> requestParse(ApiDocContext apiDocContext, PsiMethod psiMethod, ApiDocCommentData apiDocCommentData) {
        PsiParameterList parameterList = psiMethod.getParameterList();
        List<ApiDocObjectData> bodyList = Lists.newArrayList();
        List<ApiDocObjectData> paramList = Lists.newArrayList();
        for (PsiParameter parameter : parameterList.getParameters()) {
            ParseObjectBO parseObjectBO = new ParseObjectBO();
            parseObjectBO.setApiDocField(new ApiDocPsiParameter(parameter, apiDocCommentData));
            parseObjectBO.setApiDocContext(apiDocContext);
            ApiDocObjectData apiDocObjectData = ObjectParserExecutor.execute(parameter.getType(), parseObjectBO);
            if (Objects.nonNull(apiDocObjectData) && !apiDocObjectData.isFilterObject()) {
                bodyList.add(apiDocObjectData);
            }
        }
        return Lists.newArrayList(convert(bodyList, RequestType.BODY), convert(paramList, RequestType.PARAM));
    }


    /**
     * 解析响应参数
     *
     * @param apiDocContext 全局上下文
     * @param psiMethod     指定的方法
     * @return 解析后的响应参数对象
     */
    @Override
    protected ApiDocObjectData responseParse(ApiDocContext apiDocContext, PsiMethod psiMethod, ApiDocCommentData apiDocCommentData) {
        ParseObjectBO parseObjectBO = new ParseObjectBO();
        parseObjectBO.setApiDocContext(apiDocContext);
        return ObjectParserExecutor.execute(psiMethod.getReturnType(), parseObjectBO);
    }

    private ApiDocObjectData convert(List<ApiDocObjectData> apiDocObjectDataList, RequestType requestType) {
        ApiDocObjectData apiDocObjectData = new ApiDocObjectData();
        List<ApiDocObjectData> childList = Lists.newArrayList();
        if (RequestType.BODY.equals(requestType)) {
            for (ApiDocObjectData docObjectData : apiDocObjectDataList) {
                List<ApiDocObjectData> children = docObjectData.getChildList();
                if(CollectionUtils.isNotEmpty(children)){
                    childList.addAll(children);
                }
            }
        } else {
            childList = apiDocObjectDataList;
        }
        apiDocObjectData.setChildList(childList);
        apiDocObjectData.setRequestType(requestType);
        return apiDocObjectData;
    }
}
