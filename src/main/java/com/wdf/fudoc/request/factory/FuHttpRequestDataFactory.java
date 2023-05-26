package com.wdf.fudoc.request.factory;

import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.wdf.fudoc.apidoc.constant.enumtype.ContentType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestParamType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.constant.enumtype.YesOrNo;
import com.wdf.fudoc.apidoc.data.FuDocRootParamData;
import com.wdf.fudoc.apidoc.helper.DocCommentParseHelper;
import com.wdf.fudoc.apidoc.pojo.annotation.*;
import com.wdf.fudoc.apidoc.pojo.bo.RootParamBO;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.ApiDocCommentData;
import com.wdf.fudoc.apidoc.pojo.data.FuDocParamData;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.request.manager.FuRequestManager;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestBodyData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.request.pojo.FuResponseData;
import com.wdf.fudoc.spring.SpringConfigManager;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.storage.FuStorageExecutor;
import com.wdf.fudoc.util.FuDocUtils;
import com.wdf.fudoc.util.GenFuDocUtils;
import com.wdf.fudoc.util.PsiClassUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 构建HTTP请求数据的工厂类
 *
 * @author wangdingfu
 * @date 2022-09-18 19:24:15
 */
public class FuHttpRequestDataFactory {

    public static FuHttpRequestData buildEmptyHttpRequestData() {
        FuHttpRequestData fuHttpRequestData = new FuHttpRequestData();
        FuRequestData fuRequestData = new FuRequestData();
        fuRequestData.setBody(new FuRequestBodyData());
        fuRequestData.setRequestType(RequestType.POST);
        fuHttpRequestData.setRequest(fuRequestData);
        fuHttpRequestData.setResponse(new FuResponseData());
        return fuHttpRequestData;
    }


    public static FuHttpRequestData build(Project project, PsiClass psiClass, FuDocContext fuDocContext) {
        FuHttpRequestData request;
        //获取当前接口的唯一标识
        Module module = ModuleUtil.findModuleForPsiElement(psiClass);
        String moduleId = FuDocUtils.getModuleId(module);
        //获取当前操作的方法
        PsiMethod targetMethod = PsiClassUtils.getTargetMethod(fuDocContext.getTargetElement());
        if (Objects.isNull(targetMethod)) {
            return null;
        }
        ApiDocCommentData apiDocCommentData = DocCommentParseHelper.parseComment(targetMethod.getDocComment());
        String commentTitle = apiDocCommentData.getCommentTitle();
        FuHttpRequestData fuHttpRequestData = FuStorageExecutor.readFile(commentTitle);
        if (Objects.nonNull(fuHttpRequestData)) {
            return fuHttpRequestData;
        }
        String methodId = PsiClassUtils.getMethodId(targetMethod);
        //当前接口的唯一标识
        String apiKey = FuDocUtils.genApiKey(moduleId, methodId);
        request = FuRequestManager.getRequest(project, apiKey);
        if (Objects.isNull(request)) {
            request = build(fuDocContext, psiClass, module);
        }
        return request;
    }

    public static FuHttpRequestData build(FuDocContext fuDocContext, PsiClass psiClass) {
        return build(fuDocContext, psiClass, ModuleUtil.findModuleForPsiElement(psiClass));
    }

    public static FuHttpRequestData build(FuDocContext fuDocContext, PsiClass psiClass, Module module) {
        List<FuDocRootParamData> fuDocRootParamDataList = GenFuDocUtils.genRootParam(fuDocContext, psiClass);
        if (CollectionUtils.isEmpty(fuDocRootParamDataList)) {
            //没有可以请求的方法
            return null;
        }
        FuDocRootParamData fuDocRootParamData = fuDocRootParamDataList.get(0);
        //获取当前所属模块
        return FuHttpRequestDataFactory.build(module, fuDocRootParamData);
    }


    public static FuHttpRequestData build(Module module, FuDocRootParamData fuDocRootParamData) {
        FuHttpRequestData fuHttpRequestData = new FuHttpRequestData();
        String moduleId = FuDocUtils.getModuleId(module);
        //moduleId
        fuHttpRequestData.setModuleId(moduleId);
        //接口唯一标识
        fuHttpRequestData.setApiKey(moduleId + ":" + fuDocRootParamData.getApiId());
        FuRequestData fuRequestData = new FuRequestData();
        //接口名称
        String title = fuDocRootParamData.getTitle();
        if (StringUtils.isBlank(title)) {
            title = PsiClassUtils.getMethodName(fuDocRootParamData.getPsiMethod());
        }
        fuHttpRequestData.setApiName(title);
        //接口请求类型5
        fuRequestData.setRequestType(RequestType.getRequestType(fuDocRootParamData.getRequestType()));
        //设置接口url
        String domainUrl = FuDocConstants.DEFAULT_HOST + ":" + SpringConfigManager.getServerPort(module);
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

    public static void buildParamData(FuHttpRequestData fuHttpRequestData, FuDocRootParamData fuDocRootParamData, RequestType requestType) {
        List<KeyValueTableBO> paramList = Lists.newArrayList();
        List<KeyValueTableBO> pathVariableList = Lists.newArrayList();
        List<RootParamBO> rootParamBOList = fuDocRootParamData.getRootParamBOList();
        if (CollectionUtils.isNotEmpty(rootParamBOList)) {
            for (RootParamBO rootParamBO : rootParamBOList) {
                SpringAnnotationData springAnnotationData = rootParamBO.getSpringAnnotationData();
                List<KeyValueTableBO> tableBOList = paramConvertToTableData(rootParamBO.getFuDocParamDataList());
                if (CollectionUtils.isNotEmpty(tableBOList)) {
                    paramList.addAll(tableBOList);
                }
                if (springAnnotationData instanceof PathVariableData) {
                    pathVariableList.addAll(tableBOList);
                }
            }
        }
        FuRequestData request = fuHttpRequestData.getRequest();
        request.setPathVariables(pathVariableList);
        if(RequestType.GET.equals(requestType)){
            request.setParams(paramList);
        }
        if(RequestType.POST.equals(requestType)){
            ContentType contentType = fuDocRootParamData.getContentType();
            if (Objects.isNull(contentType)) {
                return;
            }
            FuRequestBodyData body = request.getBody();
            if(ContentType.FORM_DATA.equals(contentType)){
                body.setFormDataList(paramList);
            }else {
                body.setFormUrlEncodedList(paramList);
            }
        }
    }


    public static void buildPostParamData(FuHttpRequestData fuHttpRequestData, FuDocRootParamData fuDocRootParamData) {
        List<RootParamBO> rootParamBOList = fuDocRootParamData.getRootParamBOList();
        if (CollectionUtils.isEmpty(rootParamBOList)) {
            return;
        }
        RootParamBO rootParamBO = rootParamBOList.stream().filter(f -> isJson(f.getSpringAnnotationData())).findFirst().orElse(null);
        if (Objects.nonNull(rootParamBO)) {
            paddingBody(fuHttpRequestData, rootParamBO.getMockData());
            return;
        }
        buildParamData(fuHttpRequestData, fuDocRootParamData,RequestType.POST);
    }


    public static boolean isJson(SpringAnnotationData springAnnotationData) {
        return Objects.nonNull(springAnnotationData) && springAnnotationData instanceof RequestBodyData;
    }


    public static void buildRequestParamsData(FuHttpRequestData fuHttpRequestData, FuDocRootParamData fuDocRootParamData) {
        RequestType requestType = RequestType.getRequestType(fuDocRootParamData.getRequestType());
        if (Objects.isNull(requestType)) {
            return;
        }

        switch (requestType) {
            case GET:
                buildParamData(fuHttpRequestData, fuDocRootParamData,RequestType.GET);
                break;
            case POST:
                buildPostParamData(fuHttpRequestData, fuDocRootParamData);
                break;
            case PUT:
            case DELETE:
        }
    }

    private static List<KeyValueTableBO> paramConvertToTableData(List<FuDocParamData> fuDocParamDataList) {
        List<KeyValueTableBO> tableBOList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(fuDocParamDataList)) {
            for (FuDocParamData fuDocParamData : fuDocParamDataList) {
                String paramName = fuDocParamData.getParamName();
                boolean isSelect = YesOrNo.getByDesc(fuDocParamData.getParamRequire());
                RequestParamType requestParamType = FuDocConstants.FILE.equals(fuDocParamData.getParamType()) ? RequestParamType.FILE : RequestParamType.TEXT;
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
        body.setJson(paramValue);
    }


}
