package com.wdf.fudoc.request.execute;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestParamType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.apidoc.data.FuDocDataContent;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.console.FuLogger;
import com.wdf.fudoc.request.po.FuCookiePO;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.pojo.ConfigAuthTableBO;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestBodyData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.spring.SpringBootEnvLoader;
import com.wdf.fudoc.util.ObjectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2022-09-23 18:07:16
 */
public class FuHttpRequestBuilder {

    /**
     * http请求对象
     */
    private final HttpRequest httpRequest;


    private final FuRequestConfigPO configPO;

    private final Module module;

    private final FuLogger fuLogger;

    private ConfigAuthTableBO authTableBO;

    public FuHttpRequestBuilder(FuHttpRequestData fuHttpRequestData, HttpRequest httpRequest, FuRequestConfigPO fuRequestConfigPO, FuLogger fuLogger) {
        this.httpRequest = httpRequest;
        this.fuLogger = fuLogger;
        this.configPO = fuRequestConfigPO;
        this.module = FuDocDataContent.getFuDocData().getModule();
        FuRequestData request = fuHttpRequestData.getRequest();
        FuRequestBodyData body = request.getBody();
        if (Objects.isNull(body)) {
            body = new FuRequestBodyData();
            request.setBody(body);
        }

        //添加请求头
        this.addHeader(this.httpRequest, request.getHeaders());
        //添加全局请求头
        this.addHeader(this.httpRequest, configPO.getGlobalHeaderList());

        //添加Cookie
        List<FuCookiePO> cookies = configPO.getCookies();
        if (CollectionUtils.isNotEmpty(cookies)) {
            httpRequest.cookie(cookies.stream().map(this::buildCookie).collect(Collectors.toList()));
        }
        //添加form-data
        addForm(body.getFormDataList(), true);
        //添加x-www-form-urlencoded
        addForm(body.getFormUrlEncodedList(), false);
        //添加raw
        addBody(body.getRaw());
        //添加json
        addBody(body.getJson());
        //添加binary
        addBody(body.getBinary());
        //处理pathVariable
        String baseUrl = formatBaseUrl(request.getBaseUrl(), request.getPathVariables());
        //设置请求地址(GET请求参数直接在请求地址中)
        httpRequest.setUrl(formatUrl(request.getRequestUrl(baseUrl)));
    }


    private String formatBaseUrl(String baseUrl, List<KeyValueTableBO> pathVariables) {
        if (CollectionUtils.isEmpty(pathVariables)) {
            return baseUrl;
        }
        Map<String, KeyValueTableBO> pathVariableMap = ObjectUtils.listToMap(pathVariables, KeyValueTableBO::getKey);
        if (StringUtils.isNotBlank(baseUrl)) {
            String[] split = baseUrl.split("/");
            for (String urlItem : split) {
                if (urlItem.contains("{{") || urlItem.contains("}}")) {
                    continue;
                }
                if (StringUtils.startsWith(urlItem, "{") && StringUtils.endsWith(urlItem, "}")) {
                    String name = urlItem.replace("{", "").replace("}", "");
                    KeyValueTableBO keyValueTableBO = pathVariableMap.get(name);
                    if (Objects.isNull(keyValueTableBO)) {
                        continue;
                    }
                    baseUrl = baseUrl.replace(urlItem, keyValueTableBO.getValue());
                }
            }
        }
        return baseUrl;
    }


    private String formatUrl(String url) {
        if (url.contains("{{") && url.contains("}}")) {
            String[] varList = StringUtils.substringsBetween(url, "{{", "}}");
            if (Objects.nonNull(varList)) {
                for (String variable : varList) {
                    url = url.replace("{{" + variable + "}}", formatVariable(variable));
                }
            }
        }
        return url;
    }


    private HttpCookie buildCookie(FuCookiePO fuCookiePO) {
        HttpCookie httpCookie = new HttpCookie(fuCookiePO.getName(), fuCookiePO.getValue());
        httpCookie.setHttpOnly(fuCookiePO.isHttpOnly());
        return httpCookie;
    }


    private void addForm(List<KeyValueTableBO> formDataList, boolean isMultiFile) {
        if (CollectionUtils.isNotEmpty(formDataList)) {
            for (KeyValueTableBO keyValueTableBO : formDataList) {
                String value = keyValueTableBO.getValue();
                if (Objects.isNull(keyValueTableBO.getSelect()) || !keyValueTableBO.getSelect()) {
                    continue;
                }
                String requestParamType = keyValueTableBO.getRequestParamType();

                if (isMultiFile && RequestParamType.FILE.getCode().equals(requestParamType) && StringUtils.isNotBlank(value)) {
                    File file = new File(value);
                    byte[] bytes = FileUtil.readBytes(file);
                    this.httpRequest.form(keyValueTableBO.getKey(), bytes, file.getName());
                } else {
                    this.httpRequest.form(keyValueTableBO.getKey(), formatValue(keyValueTableBO.getValue()));
                }
            }
        }
    }


    private String formatValue(String value) {
        if (StringUtils.isNotBlank(value) && value.startsWith("{{") && value.endsWith("}}")) {
            return formatVariable(StringUtils.substringBetween(value, "{{", "}}"));
        }
        return value;
    }

    private String formatVariable(String variable) {
        if (variable.startsWith(FuDocConstants.FU_AUTH)) {
            return getAuthVariable(variable);
        }
        return configPO.variable(variable, SpringBootEnvLoader.getApplication(this.module));
    }


    private String getAuthVariable(String authVariableName) {
        String authName = StringUtils.substringAfterLast(authVariableName, FuDocConstants.FU_AUTH);
        if (StringUtils.isBlank(authName)) {
            fuLogger.error("鉴权用户变量[{}]未正确填写. 无法解析", authVariableName);
            return authVariableName;
        }
        List<ConfigAuthTableBO> authConfigList = this.configPO.getAuthConfigList();
        if (CollectionUtils.isEmpty(authConfigList)) {
            fuLogger.error("未配置鉴权用户信息. 无法解析鉴权用户变量[{}]", authVariableName);
            return authVariableName;
        }
        String userName = this.configPO.getUserName();
        if (Objects.isNull(authTableBO)) {
            this.authTableBO = StringUtils.isBlank(userName) ? authConfigList.get(0)
                    : authConfigList.stream().filter(f -> StringUtils.isNotBlank(f.getUserName()))
                    .filter(f -> f.getUserName().equals(userName)).findFirst().orElse(null);
            if (Objects.isNull(authTableBO)) {
                fuLogger.error("鉴权用户[{}]已不存在. 请重新选择一个已经存在的用户", userName);
                return authVariableName;
            }
        }
        if (authName.equals(FuDocConstants.FU_AUTH_USER_NAME)) {
            return authTableBO.getUserName();
        }
        if (authName.equals(FuDocConstants.FU_AUTH_PASSWORD)) {
            return authTableBO.getPassword();
        }
        Map<String, Object> dataMap = authTableBO.getDataMap();
        Object value = dataMap.get(authName);
        if (Objects.isNull(value)) {
            fuLogger.error("解析变量失败. 鉴权用户[{}]未配置变量[{}]", userName, authVariableName);
            return StringUtils.EMPTY;
        }
        return value.toString();
    }


    private void addBody(String content) {
        if (StringUtils.isNotBlank(content)) {
            this.httpRequest.body(content);
        }
    }


    private void addBody(byte[] body) {
        if (Objects.nonNull(body) && body.length > 0) {
            this.httpRequest.body(body);
        }
    }

    public static FuHttpRequestBuilder getInstance(FuHttpRequestData fuHttpRequestData, FuRequestConfigPO fuRequestConfigPO, FuLogger fuLogger) {
        FuRequestData request = fuHttpRequestData.getRequest();
        String requestUrl = request.getRequestUrl();
        RequestType requestType = request.getRequestType();
        return new FuHttpRequestBuilder(fuHttpRequestData, createHttpRequest(requestType, requestUrl), fuRequestConfigPO, fuLogger);
    }

    public HttpRequest builder() {
        return this.httpRequest;
    }


    /**
     * 将请求头信息添加到http请求中
     *
     * @param httpRequest http请求对象
     * @param headers     请求头
     */
    private <T extends KeyValueTableBO> void addHeader(HttpRequest httpRequest, List<T> headers) {
        if (CollectionUtils.isNotEmpty(headers)) {
            headers.stream().filter(KeyValueTableBO::getSelect).forEach(data -> httpRequest.header(data.getKey(), formatValue(data.getValue())));
        }
    }


    private static HttpRequest createHttpRequest(RequestType requestType, String requestUrl) {
        if (Objects.nonNull(requestType)) {
            switch (requestType) {
                case GET -> {
                    return HttpUtil.createGet(requestUrl);
                }
                case POST -> {
                    return HttpUtil.createPost(requestUrl);
                }
                case DELETE -> {
                    return HttpUtil.createRequest(Method.DELETE, requestUrl);
                }
            }
        }
        return HttpUtil.createGet(requestUrl);
    }

}
