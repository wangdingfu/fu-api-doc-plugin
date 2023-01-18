package com.wdf.fudoc.apidoc.mock;

import cn.hutool.json.JSONUtil;
import com.intellij.openapi.module.ModuleUtil;
import com.wdf.fudoc.apidoc.constant.AnnotationConstants;
import com.wdf.fudoc.apidoc.constant.enumtype.MockResultType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.pojo.bo.MockResultBo;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.pojo.desc.MethodInfoDesc;
import com.wdf.fudoc.apidoc.pojo.desc.ObjectInfoDesc;
import com.wdf.fudoc.request.manager.FuRequestManager;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestBodyData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.request.pojo.FuResponseData;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.util.FuDocUtils;
import com.wdf.fudoc.util.ProjectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2022-11-16 22:49:46
 */
public abstract class AbstractMockDataService implements MockDataService {


    /**
     * mock请求参数数据
     *
     * @param methodInfoDesc 接口描述信息
     * @return mock后的数据
     */
    protected abstract String mockRequest(RequestType requestType, MethodInfoDesc methodInfoDesc);

    /**
     * mock响应结果数据
     *
     * @param methodInfoDesc 接口描述信息
     * @return mock后的数据
     */
    protected abstract String mockResponse(MethodInfoDesc methodInfoDesc);


    @Override
    public MockResultBo mockData(MethodInfoDesc methodInfoDesc, FuDocItemData fuDocItemData) {
        MockResultBo mockResultBo = new MockResultBo();
        //1、从【Fu Request】中读取请求或响应数据示例 如果有则直接返回 没有则调用具体实现类生成mock数据
        //获取当前接口的唯一标识
        String moduleId = FuDocUtils.getModuleId(ModuleUtil.findModuleForPsiElement(methodInfoDesc.getPsiMethod()));
        String methodId = methodInfoDesc.getMethodId();
        RequestType requestType = RequestType.getRequestType(fuDocItemData.getRequestType());
        FuHttpRequestData request = FuRequestManager.getRequest(ProjectUtils.getCurrProject(), FuDocUtils.genApiKey(moduleId, methodId));
        boolean requestFlag = false, responseFlag = false;
        if (Objects.nonNull(request)) {
            requestFlag = mockRequest(mockResultBo, requestType, request.getRequest());
            responseFlag = mockResponse(mockResultBo, request.getResponse());
        }
        if (!requestFlag) {
            mockResultBo.setRequestExample(mockRequest(requestType, methodInfoDesc));
            mockResultBo.setRequestExampleType(calMockRequestDataType(requestType, methodInfoDesc).getCode());
        }
        if (!responseFlag) {
            mockResultBo.setResponseExample(mockResponse(methodInfoDesc));
            mockResultBo.setResponseExampleType(MockResultType.JSON.getCode());
        }
        return mockResultBo;
    }


    private boolean mockResponse(MockResultBo mockResultBo, FuResponseData response) {
        if (Objects.isNull(response)) {
            return false;
        }
        String content = response.getContent();
        if (StringUtils.isNotBlank(content)) {
            mockResultBo.setResponseExample(content);
            mockResultBo.setResponseExampleType(JSONUtil.isTypeJSON(content) ? MockResultType.JSON.getCode() : MockResultType.DEFAULT.getCode());
        }
        return false;
    }


    private boolean mockRequest(MockResultBo mockResultBo, RequestType requestType, FuRequestData requestData) {
        if (Objects.isNull(requestType) || Objects.isNull(requestData)) {
            return false;
        }
        switch (requestType) {
            case GET:
                mockResultBo.setRequestExampleType(MockResultType.PROPERTIES.getCode());
                mockResultBo.setRequestExample(requestData.getParamUrl());
                return true;
            case POST:
                return mockRequestByBody(mockResultBo, requestData.getBody());
            case PUT:
            case DELETE:
                if (!mockRequestByBody(mockResultBo, requestData.getBody())) {
                    mockResultBo.setRequestExampleType(MockResultType.PROPERTIES.getCode());
                    mockResultBo.setRequestExample(requestData.getParamUrl());
                    return StringUtils.isNotBlank(requestData.getParamUrl());
                }
                return true;
        }
        return false;
    }


    private boolean mockRequestByBody(MockResultBo mockResultBo, FuRequestBodyData body) {
        if (Objects.nonNull(body)) {
            String json = body.getJson();
            if (StringUtils.isNotBlank(json)) {
                mockResultBo.setRequestExample(json);
                mockResultBo.setRequestExampleType(MockResultType.JSON.getCode());
                return true;
            }
            mockResultBo.setRequestExampleType(MockResultType.YAML.getCode());
            String requestExample = buildParamData(body.getFormDataList());
            if (StringUtils.isBlank(requestExample)) {
                requestExample = buildParamData(body.getFormUrlEncodedList());
            }
            mockResultBo.setRequestExample(requestExample);
            return StringUtils.isNotBlank(requestExample);
        }
        return false;
    }

    private String buildParamData(List<KeyValueTableBO> formDataList) {
        if (CollectionUtils.isNotEmpty(formDataList)) {
            return formDataList.stream().map(this::buildYamlData).collect(Collectors.joining("\r\n"));
        }
        return StringUtils.EMPTY;
    }


    private String buildYamlData(KeyValueTableBO keyValueTableBO) {
        return keyValueTableBO.getKey() + ": " + keyValueTableBO.getValue();
    }


    private MockResultType calMockRequestDataType(RequestType requestType, MethodInfoDesc methodInfoDesc) {
        if (RequestType.GET.equals(requestType)) {
            return MockResultType.PROPERTIES;
        }
        List<ObjectInfoDesc> requestList = methodInfoDesc.getRequestList();
        if (CollectionUtils.isNotEmpty(requestList) && requestList.stream().anyMatch(a -> a.exists(AnnotationConstants.REQUEST_BODY))) {
            return MockResultType.JSON;
        }
        return MockResultType.DEFAULT;
    }
}
