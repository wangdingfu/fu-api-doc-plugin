package com.wdf.fudoc.request.factory;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.constant.enumtype.SpringParamAnnotation;
import com.wdf.fudoc.apidoc.constant.enumtype.YesOrNo;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocParamData;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestBodyData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import com.wdf.fudoc.util.FuDocUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 构建HTTP请求数据的工厂类
 *
 * @author wangdingfu
 * @date 2022-09-18 19:24:15
 */
public class FuHttpRequestDataFactory {


    public static FuHttpRequestData build(FuDocItemData fuDocItemData, PsiClass psiClass) {
        FuHttpRequestData fuHttpRequestData = new FuHttpRequestData();
        //moduleId
        String moduleId = FuDocUtils.getModuleId(ModuleUtil.findModuleForPsiElement(psiClass));
        fuHttpRequestData.setModuleId(moduleId);
        JSONObject fuDoc = JSONUtil.parseObj(fuDocItemData.getFudoc());
        //接口唯一标识
        Object apiId = fuDoc.get(FuDocConstants.API_ID);
        fuHttpRequestData.setApiKey(moduleId + ":" + (Objects.nonNull(apiId) ? apiId.toString() : fuDocItemData.getDocNo()));
        FuRequestData fuRequestData = new FuRequestData();
        //接口名称
        fuHttpRequestData.setApiName(fuDocItemData.getTitle());
        //接口请求类型
        fuRequestData.setRequestType(RequestType.getRequestType(fuDocItemData.getRequestType()));
        //设置接口url
        fuRequestData.setApiUrl(fuDocItemData.getUrlList().get(0));
        fuHttpRequestData.setRequest(fuRequestData);
        //构建请求参数
        buildRequestParamsData(fuHttpRequestData, fuDocItemData);
        return fuHttpRequestData;
    }


    public static void buildRequestParamsData(FuHttpRequestData fuHttpRequestData, FuDocItemData fuDocItemData) {
        FuRequestData request = fuHttpRequestData.getRequest();
        List<KeyValueTableBO> getParamList = Lists.newArrayList();
        List<KeyValueTableBO> pathVariableList = Lists.newArrayList();
        List<KeyValueTableBO> postParamList = Lists.newArrayList();
        List<KeyValueTableBO> postFormDataList = Lists.newArrayList();
        RequestType requestType = RequestType.getRequestType(fuDocItemData.getRequestType());
        List<FuDocParamData> requestParams = fuDocItemData.getRequestParams();
        for (FuDocParamData requestParam : requestParams) {
            SpringParamAnnotation springParam = getSpringParam(requestParam);
            String paramName = requestParam.getParamName();
            boolean isSelect = YesOrNo.getByDesc(requestParam.getParamRequire());
            KeyValueTableBO keyValueTableBO = new KeyValueTableBO(isSelect, paramName, requestParam.getParamValue(), requestParam.getParamDesc());
            switch (springParam) {
                case PATH_VARIABLE:
                    pathVariableList.add(keyValueTableBO);
                    break;
                case REQUEST_BODY:
                    //TODO body 参数
                    break;
                case REQUEST_PARAM:
                case NONE:
                    // requestParam 参数处理
                    if (RequestType.GET.equals(requestType)) {
                        getParamList.add(keyValueTableBO);
                    } else if (RequestType.POST.equals(requestType)) {
                        postFormDataList.add(keyValueTableBO);
                    }
                    break;
            }
        }
        request.setPathVariables(pathVariableList);
        request.setParams(getParamList);
        FuRequestBodyData body = request.getBody();
        if (Objects.isNull(body)) {
            body = new FuRequestBodyData();
        }
        body.setParams(postParamList);
        body.setFormData(postFormDataList);
    }


    private static SpringParamAnnotation getSpringParam(FuDocParamData requestParam) {
        Map<String, Object> fuDoc = requestParam.getFudoc();
        Object value = fuDoc.get(FuDocConstants.SPRING_PARAM);
        if (Objects.nonNull(value) && value instanceof SpringParamAnnotation) {
            return (SpringParamAnnotation) value;
        }
        return SpringParamAnnotation.NONE;
    }


}
