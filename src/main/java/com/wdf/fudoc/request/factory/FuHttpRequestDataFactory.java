package com.wdf.fudoc.request.factory;

import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestParamType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.constant.enumtype.YesOrNo;
import com.wdf.fudoc.apidoc.data.FuDocRootParamData;
import com.wdf.fudoc.apidoc.pojo.annotation.*;
import com.wdf.fudoc.apidoc.pojo.bo.RootParamBO;
import com.wdf.fudoc.apidoc.pojo.data.FuDocParamData;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestBodyData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.request.pojo.FuResponseData;
import com.wdf.fudoc.spring.SpringConfigFileManager;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import com.wdf.fudoc.util.FuDocUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 构建HTTP请求数据的工厂类
 *
 * @author wangdingfu
 * @date 2022-09-18 19:24:15
 */
public class FuHttpRequestDataFactory {

    public static FuHttpRequestData build(FuDocRootParamData fuDocRootParamData, PsiClass psiClass) {
        return build(FuDocUtils.getModuleId(ModuleUtil.findModuleForPsiElement(psiClass)), fuDocRootParamData);
    }


    public static FuHttpRequestData build(String moduleId, FuDocRootParamData fuDocRootParamData) {
        FuHttpRequestData fuHttpRequestData = new FuHttpRequestData();
        //moduleId
        fuHttpRequestData.setModuleId(moduleId);
        //接口唯一标识
        fuHttpRequestData.setApiKey(moduleId + ":" + fuDocRootParamData.getApiId());
        FuRequestData fuRequestData = new FuRequestData();
        //接口名称
        fuHttpRequestData.setApiName(fuDocRootParamData.getTitle());
        //接口请求类型
        fuRequestData.setRequestType(RequestType.getRequestType(fuDocRootParamData.getRequestType()));
        //设置接口url
        String domainUrl = FuDocConstants.DEFAULT_HOST + ":" + SpringConfigFileManager.getServerPort(moduleId);
        fuRequestData.setBaseUrl(URLUtil.completeUrl(domainUrl, fuDocRootParamData.getUrlList().get(0)));
        //设置body内容
        fuRequestData.setBody(new FuRequestBodyData());
        fuHttpRequestData.setRequest(fuRequestData);
        //构建请求参数
        buildRequestParamsData(fuHttpRequestData, fuDocRootParamData);

        //初始化response
        fuHttpRequestData.setResponse(new FuResponseData());
        return fuHttpRequestData;
    }


    public static void buildRequestParamsData(FuHttpRequestData fuHttpRequestData, FuDocRootParamData fuDocRootParamData) {
        FuRequestData request = fuHttpRequestData.getRequest();
        List<KeyValueTableBO> getParamList = Lists.newArrayList();
        List<KeyValueTableBO> pathVariableList = Lists.newArrayList();
        List<KeyValueTableBO> postParamList = Lists.newArrayList();
        List<KeyValueTableBO> postFormDataList = Lists.newArrayList();
        RequestType requestType = RequestType.getRequestType(fuDocRootParamData.getRequestType());
        List<RootParamBO> rootParamBOList = fuDocRootParamData.getRootParamBOList();
        if (CollectionUtils.isNotEmpty(rootParamBOList)) {
            for (RootParamBO rootParamBO : rootParamBOList) {
                SpringAnnotationData springAnnotationData = rootParamBO.getSpringAnnotationData();
                if (springAnnotationData instanceof RequestBodyData) {
                    //如果是requestBody
                    paddingBody(fuHttpRequestData, rootParamBO.getMockData());
                    break;
                }
                //其他的应该是RequestParam 和PathVariable注解标识或无注解标识
                List<KeyValueTableBO> tableBOList = paramConvertToTableData(rootParamBO.getFuDocParamDataList());
                if (springAnnotationData instanceof PathVariableData) {
                    pathVariableList.addAll(tableBOList);
                } else if (springAnnotationData instanceof RequestParamData || springAnnotationData instanceof DefaultAnnotationData) {
                    if (RequestType.GET.equals(requestType)) {
                        getParamList.addAll(tableBOList);
                    } else if (RequestType.POST.equals(requestType)) {
                        postFormDataList.addAll(tableBOList);
                    }
                }
            }
        }
        request.setPathVariables(pathVariableList);
        request.setParams(getParamList);
        FuRequestBodyData body = request.getBody();
        body.setFormDataList(postParamList);
        body.setFormUrlEncodedList(postFormDataList);
    }

    private static List<KeyValueTableBO> paramConvertToTableData(List<FuDocParamData> fuDocParamDataList) {
        List<KeyValueTableBO> tableBOList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(fuDocParamDataList)) {
            for (FuDocParamData fuDocParamData : fuDocParamDataList) {
                String paramName = fuDocParamData.getParamName();
                boolean isSelect = YesOrNo.getByDesc(fuDocParamData.getParamRequire());
                RequestParamType requestParamType = "file".equals(fuDocParamData.getParamType()) ? RequestParamType.FILE : RequestParamType.TEXT;
                tableBOList.add(new KeyValueTableBO(isSelect, requestParamType.getCode(), paramName, fuDocParamData.getParamValue(), fuDocParamData.getParamDesc()));
            }
        }
        return tableBOList;
    }


    private static void paddingBody(FuHttpRequestData fuHttpRequestData, String paramValue) {
        FuRequestData request = fuHttpRequestData.getRequest();
        FuRequestBodyData body = request.getBody();
        if (Objects.isNull(body)) {
            body = new FuRequestBodyData();
            request.setBody(body);
        }
        //body 参数
        if (JSONUtil.isTypeJSON(paramValue)) {
            body.setJson(paramValue);
        } else {
            body.setRaw(paramValue);
        }
    }


}
