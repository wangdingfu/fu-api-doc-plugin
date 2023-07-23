package com.wdf.fudoc.request.manager;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.body.FormUrlEncodedBody;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.intellij.execution.impl.ConsoleViewUtil;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.httpClient.http.request.HttpRequestFileType;
import com.intellij.json.JsonFileType;
import com.wdf.fudoc.common.FuDocRender;
import com.wdf.fudoc.common.base.KeyValueBO;
import com.wdf.fudoc.common.constant.FuConsoleConstants;
import com.wdf.fudoc.components.FuConsole;
import com.wdf.fudoc.request.constants.enumtype.RequestStatus;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestConsoleData;
import com.wdf.fudoc.request.pojo.FuResponseConsoleData;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-14 14:00:58
 */
public class FuRequestConsoleManager {


    public static void requestConsole(FuConsole fuConsole, HttpRequest httpRequest, HttpResponse httpResponse) {
        if (Objects.isNull(fuConsole) || fuConsole.isEmpty()) {
            return;
        }
        logRequest(fuConsole, httpRequest);
        logResponse(fuConsole, httpResponse);
    }


    public static void requestConsole(FuConsole fuConsole, HttpRequest httpRequest, Exception e) {
        if (Objects.isNull(fuConsole) || fuConsole.isEmpty()) {
            return;
        }
        logRequest(fuConsole, httpRequest);
        logResponse(fuConsole, e);
    }


    public static void logResult(FuConsole fuConsole, FuHttpRequestData fuHttpRequestData, RequestStatus result) {
        if (Objects.isNull(fuConsole) || fuConsole.isEmpty()) {
            return;
        }
        fuConsole.println();

        //第一行
        fuConsole.debugLog("[FU REQUEST] ");
        fuConsole.debug(FuConsoleConstants.LINE);
        //第二行 请求结果
        fuConsole.debugLog("[FU REQUEST] ");
        fuConsole.infoLog("REQUEST ");
        fuConsole.log(result.getName(), RequestStatus.SUCCESS.equals(result) ? ConsoleViewContentType.USER_INPUT : ConsoleViewContentType.ERROR_OUTPUT);
        fuConsole.println();

        Integer httpCode = fuHttpRequestData.getHttpCode();
        if (Objects.nonNull(httpCode)) {
            //第三行 状态码
            fuConsole.debugLog("[FU REQUEST] ");
            fuConsole.infoLog("Status Code: ");
            fuConsole.log(String.valueOf(httpCode), fuHttpRequestData.isOk() ? ConsoleViewContentType.USER_INPUT : ConsoleViewContentType.ERROR_OUTPUT);
            fuConsole.println();
        }

        //第三行 耗时
        fuConsole.debugLog("[FU REQUEST] ");
        fuConsole.infoLog("Total time: ");
        fuConsole.log(fuHttpRequestData.getTime() + " ms", fuHttpRequestData.getTime() < 3000 ? ConsoleViewContentType.USER_INPUT : ConsoleViewContentType.ERROR_OUTPUT);
        fuConsole.println();

        //第四行
        fuConsole.debugLog("[FU REQUEST] ");
        fuConsole.debugLog(FuConsoleConstants.LINE);
        fuConsole.println();
    }


    private static void logRequest(FuConsole fuConsole, HttpRequest httpRequest) {
        fuConsole.debug(FuConsoleConstants.LINE);
        fuConsole.debug(FuConsoleConstants.lineContent("REQUEST"));
        fuConsole.debug(FuConsoleConstants.LINE);

        FuRequestConsoleData requestConsoleData = new FuRequestConsoleData();
        requestConsoleData.setMethodName(httpRequest.getMethod().name());
        requestConsoleData.setUrl(httpRequest.getUrl());
        requestConsoleData.setHeaders(buildHeaderList(httpRequest.headers()));
        ConsoleViewUtil.printAsFileType(fuConsole.getConsoleView(), FuDocRender.render(requestConsoleData, "console/request_console.ftl"), HttpRequestFileType.INSTANCE);
        String bodyContent = buildRequestBody(httpRequest);
        if (JSONUtil.isTypeJSON(bodyContent)) {
            ConsoleViewUtil.printAsFileType(fuConsole.getConsoleView(), bodyContent, JsonFileType.INSTANCE);
            fuConsole.println();
        } else {
            fuConsole.info(bodyContent);
        }
        fuConsole.println();

    }


    private static void logResponse(FuConsole fuConsole, HttpResponse httpResponse) {
        fuConsole.println();
        fuConsole.debug(FuConsoleConstants.LINE);
        fuConsole.debug(FuConsoleConstants.lineContent("RESPONSE"));
        fuConsole.debug(FuConsoleConstants.LINE);

        FuResponseConsoleData responseConsoleData = new FuResponseConsoleData();
        responseConsoleData.setHttpType(httpResponse.httpVersion());
        responseConsoleData.setHttpStatus(httpResponse.getStatus());
        responseConsoleData.setHeaders(buildHeaderList(httpResponse.headers()));
        String bodyContent = httpResponse.body();
        responseConsoleData.setResponseBody(bodyContent);
        ConsoleViewUtil.printAsFileType(fuConsole.getConsoleView(), FuDocRender.render(responseConsoleData, "console/response_console.ftl"), HttpRequestFileType.INSTANCE);
        if (StringUtils.isBlank(bodyContent)) {
            return;
        }
        if (JSONUtil.isTypeJSON(bodyContent)) {
            ConsoleViewUtil.printAsFileType(fuConsole.getConsoleView(), JSONUtil.toJsonPrettyStr(bodyContent), JsonFileType.INSTANCE);
            fuConsole.println();
        } else {
            fuConsole.info(bodyContent);
        }
    }

    private static void logResponse(FuConsole fuConsole, Exception e) {
        fuConsole.println();
        fuConsole.error(e.toString());
    }


    private static String buildRequestBody(HttpRequest httpRequest) {
        byte[] bytes = httpRequest.bodyBytes();
        if (Objects.nonNull(bytes)) {
            String bodyContent = StringUtils.toEncodedString(bytes, StandardCharsets.UTF_8);
            if (StringUtils.isNotBlank(bodyContent)) {
                return JSONUtil.isTypeJSON(bodyContent) ? JSONUtil.toJsonPrettyStr(bodyContent) : bodyContent;
            }
        }
        return FormUrlEncodedBody.create(httpRequest.form(), CharsetUtil.CHARSET_UTF_8).toString();
    }


    private static List<KeyValueBO> buildHeaderList(Map<String, List<String>> headers) {
        List<KeyValueBO> headerList = Lists.newArrayList();
        headers.forEach((key, value) -> headerList.add(new KeyValueBO(key, StringUtils.join(value, ";"))));
        return headerList;
    }

}
