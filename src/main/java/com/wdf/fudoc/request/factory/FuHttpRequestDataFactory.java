package com.wdf.fudoc.request.factory;

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
import com.wdf.fudoc.apidoc.pojo.annotation.PathVariableData;
import com.wdf.fudoc.apidoc.pojo.annotation.RequestBodyData;
import com.wdf.fudoc.apidoc.pojo.annotation.SpringAnnotationData;
import com.wdf.fudoc.apidoc.pojo.bo.RootParamBO;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.pojo.data.FuDocParamData;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.request.manager.FuRequestManager;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.pojo.*;
import com.wdf.fudoc.spring.SpringBootEnvLoader;
import com.wdf.fudoc.spring.SpringConfigManager;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.util.FuDocUtils;
import com.wdf.fudoc.util.GenFuDocUtils;
import com.wdf.fudoc.util.PsiClassUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
        FuHttpRequestData fuHttpRequestData;
        //获取当前接口的唯一标识
        Module module = ModuleUtil.findModuleForPsiElement(psiClass);
        String moduleId = FuDocUtils.getModuleId(module);
        //获取当前操作的方法
        PsiMethod targetMethod = PsiClassUtils.getTargetMethod(fuDocContext.getTargetElement());
        if (Objects.isNull(targetMethod)) {
            return null;
        }

        String methodId = PsiClassUtils.getMethodId(targetMethod);
        //当前接口的唯一标识
        String apiKey = FuDocUtils.genApiKey(moduleId, methodId);
        fuHttpRequestData = FuRequestManager.getRequest(project, apiKey);
        if (Objects.isNull(fuHttpRequestData)) {
            fuHttpRequestData = build(fuDocContext, psiClass, module);
        } else {
            paddingDomain(fuHttpRequestData, module);
        }
        return fuHttpRequestData;
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
        FuHttpRequestData fuHttpRequestData = FuHttpRequestDataFactory.build(module, fuDocRootParamData);
        paddingDomain(fuHttpRequestData, module);
        return fuHttpRequestData;
    }


    public static FuHttpRequestData build(Module module, FuDocRootParamData fuDocRootParamData) {
        FuHttpRequestData fuHttpRequestData = new FuHttpRequestData();
        String moduleId = FuDocUtils.getModuleId(module);
        //moduleId
        fuHttpRequestData.setModuleId(moduleId);
        //接口唯一标识
        fuHttpRequestData.setApiKey(moduleId + ":" + fuDocRootParamData.getApiId());
        //接口名称
        String title = fuDocRootParamData.getTitle();
        if (StringUtils.isBlank(title)) {
            title = PsiClassUtils.getMethodName(fuDocRootParamData.getPsiMethod());
        }
        fuHttpRequestData.setApiName(title);
        fuHttpRequestData.setRequest(buildFuRequestData(fuDocRootParamData));
        //构建请求参数
        buildRequestParamsData(fuHttpRequestData, fuDocRootParamData);
        //初始化response
        fuHttpRequestData.setResponse(new FuResponseData());
        return fuHttpRequestData;
    }


    private static FuRequestData buildFuRequestData(FuDocRootParamData fuDocRootParamData) {
        FuRequestData fuRequestData = new FuRequestData();
        List<String> urlList = fuDocRootParamData.getUrlList();
        fuRequestData.setBaseUrl(CollectionUtils.isEmpty(urlList) ? StringUtils.EMPTY : urlList.get(0));
        //接口请求类型
        fuRequestData.setRequestType(RequestType.getRequestType(fuDocRootParamData.getRequestType()));
        //设置body内容
        fuRequestData.setBody(new FuRequestBodyData());
        return fuRequestData;
    }

    public static void buildParamData(FuHttpRequestData fuHttpRequestData, FuDocRootParamData fuDocRootParamData, RequestType requestType) {
        List<KeyValueTableBO> paramList = Lists.newArrayList();
        List<KeyValueTableBO> pathVariableList = Lists.newArrayList();
        List<RootParamBO> rootParamBOList = fuDocRootParamData.getRootParamBOList();
        RootParamBO requestBodyParam = null;
        if (CollectionUtils.isNotEmpty(rootParamBOList)) {
            for (RootParamBO rootParamBO : rootParamBOList) {
                SpringAnnotationData springAnnotationData = rootParamBO.getSpringAnnotationData();
                if (Objects.nonNull(springAnnotationData) && springAnnotationData instanceof RequestBodyData) {
                    requestBodyParam = rootParamBO;
                    continue;
                }
                List<KeyValueTableBO> tableBOList = paramConvertToTableData(rootParamBO.getFuDocParamDataList());
                if (springAnnotationData instanceof PathVariableData) {
                    pathVariableList.addAll(tableBOList);
                    continue;
                }
                if (CollectionUtils.isNotEmpty(tableBOList)) {
                    paramList.addAll(tableBOList);
                }
            }
        }
        FuRequestData request = fuHttpRequestData.getRequest();
        String baseUrl = request.getBaseUrl();
        if (StringUtils.isNotBlank(baseUrl)) {
            String[] split = baseUrl.split("/");
            for (String urlItem : split) {
                if (urlItem.contains("{{") || urlItem.contains("}}")) {
                    continue;
                }
                if (StringUtils.startsWith(urlItem, "{") && StringUtils.endsWith(urlItem, "}")) {
                    String name = urlItem.replace("{", "").replace("}", "");
                    if (pathVariableList.stream().noneMatch(a -> a.getKey().equals(name))) {
                        pathVariableList.add(new KeyValueTableBO(true, name, ""));
                    }
                }
            }
        }
        request.setPathVariables(pathVariableList);
        if (RequestType.GET.equals(requestType)) {
            request.setParams(paramList);
            return;
        }
        ContentType contentType = fuDocRootParamData.getContentType();
        if (Objects.isNull(contentType)) {
            return;
        }
        if (Objects.nonNull(requestBodyParam)) {
            paddingBody(fuHttpRequestData, requestBodyParam.getMockData());
            return;
        }
        FuRequestBodyData body = request.getBody();
        if (ContentType.FORM_DATA.equals(contentType)) {
            body.setFormDataList(paramList);
        } else {
            body.setFormUrlEncodedList(paramList);
        }
    }


    public static void buildRequestParamsData(FuHttpRequestData fuHttpRequestData, FuDocRootParamData fuDocRootParamData) {
        RequestType requestType = RequestType.getRequestType(fuDocRootParamData.getRequestType());
        if (Objects.isNull(requestType)) {
            return;
        }
        buildParamData(fuHttpRequestData, fuDocRootParamData, requestType);
    }

    private static List<KeyValueTableBO> paramConvertToTableData(List<FuDocParamData> fuDocParamDataList) {
        List<KeyValueTableBO> tableBOList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(fuDocParamDataList)) {
            for (FuDocParamData fuDocParamData : fuDocParamDataList) {
                String paramName = fuDocParamData.getParamName();
                boolean isSelect = YesOrNo.getByDesc(fuDocParamData.getParamRequire());
                RequestParamType requestParamType = FuDocConstants.FILE.equals(fuDocParamData.getParamType()) ? RequestParamType.FILE : RequestParamType.TEXT;
                tableBOList.add(new KeyValueTableBO(isSelect, requestParamType.getCode(), paramName, fuDocParamData.getParamValue(), fuDocParamData.getParamDesc(), true));
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


    private static void paddingDomain(FuHttpRequestData fuHttpRequestData, Module module) {
        if (Objects.nonNull(fuHttpRequestData)) {
            fuHttpRequestData.setModule(module);
            FuRequestConfigPO fuRequestConfigPO = FuRequestConfigStorage.get(module.getProject()).readData();
            List<ConfigEnvTableBO> envConfigList = fuRequestConfigPO.getEnvConfigList();
            String envName = fuRequestConfigPO.getEnv(module.getName());
            if (CollectionUtils.isNotEmpty(envConfigList) && StringUtils.isNotBlank(envName)) {
                Optional<ConfigEnvTableBO> first = envConfigList.stream().filter(f -> f.getEnvName().equals(envName)).findFirst();
                if(first.isPresent()){
                    FuRequestData request = fuHttpRequestData.getRequest();
                    request.setDomain(first.get().getDomain());
                    request.setContextPath(null);
                    return;
                }
            }
            paddingDefaultDomain(fuHttpRequestData, module);
        }
    }


    private static void paddingDefaultDomain(FuHttpRequestData fuHttpRequestData, Module module) {
        //设置接口url
        String domainUrl = FuDocConstants.DEFAULT_HOST + ":" + SpringBootEnvLoader.getServerPort(module);
        FuRequestData request = fuHttpRequestData.getRequest();
        if (Objects.isNull(request)) {
            request = new FuRequestData();
        }
        //读取server.servlet.context-path属性 issue: #20
        String configValue = SpringConfigManager.getContextPath(module);
        if (StringUtils.isNotBlank(configValue)) {
            request.setContextPath(configValue);
        }
        request.setDomain(domainUrl);
    }


}
