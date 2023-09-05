package com.wdf.fudoc.request.manager;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.body.FormUrlEncodedBody;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.intellij.execution.impl.ConsoleViewUtil;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.wdf.fudoc.common.FuDocRender;
import com.wdf.fudoc.common.base.KeyValueBO;
import com.wdf.fudoc.common.constant.FuConsoleConstants;
import com.wdf.fudoc.console.FuConsoleLogger;
import com.wdf.fudoc.console.FuLogger;
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


    public static void requestConsole(FuLogger fuLogger, HttpRequest httpRequest, HttpResponse httpResponse) {
        if (Objects.isNull(fuLogger) || fuLogger.isEmpty()) {
            return;
        }
        logRequest(fuLogger, httpRequest);
        logResponse(fuLogger, httpResponse);
    }


    public static void requestConsole(FuLogger fuLogger, HttpRequest httpRequest, Exception e) {
        if (Objects.isNull(fuLogger) || fuLogger.isEmpty()) {
            return;
        }
        logRequest(fuLogger, httpRequest);
        logResponse(fuLogger, e);
    }


    public static void logResult(FuLogger fuLogger, FuHttpRequestData fuHttpRequestData, RequestStatus result) {
        if (Objects.isNull(fuLogger) || fuLogger.isEmpty()) {
            return;
        }
        fuLogger.println();

        //第一行
        fuLogger.debugLog("[FU REQUEST] ");
        fuLogger.debug(FuConsoleConstants.LINE);
        //第二行 请求结果
        fuLogger.debugLog("[FU REQUEST] ");
        fuLogger.infoLog("REQUEST ");
        log(fuLogger, result.getName(), RequestStatus.SUCCESS.equals(result));
        fuLogger.println();

        Integer httpCode = fuHttpRequestData.getHttpCode();
        if (Objects.nonNull(httpCode)) {
            //第三行 状态码
            fuLogger.debugLog("[FU REQUEST] ");
            fuLogger.infoLog("Status Code: ");
            log(fuLogger, String.valueOf(httpCode), fuHttpRequestData.isOk());
            fuLogger.println();
        }

        //第三行 耗时
        fuLogger.debugLog("[FU REQUEST] ");
        fuLogger.infoLog("Total time: ");
        log(fuLogger, fuHttpRequestData.getTime() + " ms", fuHttpRequestData.getTime() < 3000);
        fuLogger.println();

        //第四行
        fuLogger.debugLog("[FU REQUEST] ");
        fuLogger.debugLog(FuConsoleConstants.LINE);
        fuLogger.println();
        fuLogger.println();
    }


    private static void log(FuLogger fuLogger, String logContent, boolean isSuccess) {
        if (fuLogger instanceof FuConsoleLogger fuConsoleLogger) {
            fuConsoleLogger.log(logContent, isSuccess ? ConsoleViewContentType.USER_INPUT : ConsoleViewContentType.ERROR_OUTPUT);
        } else {
            fuLogger.info(logContent);
        }
    }


    private static void logRequest(FuLogger fuLogger, HttpRequest httpRequest) {
        fuLogger.debug(FuConsoleConstants.LINE);
        fuLogger.debug(FuConsoleConstants.lineContent("REQUEST"));
        fuLogger.debug(FuConsoleConstants.LINE);

        FuRequestConsoleData requestConsoleData = new FuRequestConsoleData();
        requestConsoleData.setMethodName(httpRequest.getMethod().name());
        requestConsoleData.setUrl(httpRequest.getUrl());
        requestConsoleData.setHeaders(buildHeaderList(httpRequest.headers()));
        ConsoleViewUtil.printAsFileType(fuLogger.getConsoleView(), FuDocRender.render(requestConsoleData, "console/request_console.ftl"), PlainTextFileType.INSTANCE);
        String bodyContent = buildRequestBody(httpRequest);
        if (JSONUtil.isTypeJSON(bodyContent)) {
            ConsoleViewUtil.printAsFileType(fuLogger.getConsoleView(), bodyContent, JsonFileType.INSTANCE);
        } else {
            fuLogger.info(bodyContent);
        }
        fuLogger.println();

    }


    private static void logResponse(FuLogger fuLogger, HttpResponse httpResponse) {
        fuLogger.println();
        fuLogger.debug(FuConsoleConstants.LINE);
        fuLogger.debug(FuConsoleConstants.lineContent("RESPONSE"));
        fuLogger.debug(FuConsoleConstants.LINE);

        FuResponseConsoleData responseConsoleData = new FuResponseConsoleData();
        responseConsoleData.setHttpType(httpResponse.httpVersion());
        responseConsoleData.setHttpStatus(httpResponse.getStatus());
        responseConsoleData.setHeaders(buildHeaderList(httpResponse.headers()));
        String bodyContent = httpResponse.body();
        responseConsoleData.setResponseBody(bodyContent);
        ConsoleViewUtil.printAsFileType(fuLogger.getConsoleView(), FuDocRender.render(responseConsoleData, "console/response_console.ftl"), PlainTextFileType.INSTANCE);
        if (StringUtils.isBlank(bodyContent)) {
            return;
        }
        if (JSONUtil.isTypeJSON(bodyContent)) {
            ConsoleViewUtil.printAsFileType(fuLogger.getConsoleView(), JSONUtil.toJsonPrettyStr(bodyContent), JsonFileType.INSTANCE);
            fuLogger.println();
        } else {
            fuLogger.info(bodyContent);
        }
    }

    private static void logResponse(FuLogger fuLogger, Exception e) {
        fuLogger.println();
        fuLogger.error("请求接口异常:" + e.getMessage());
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
