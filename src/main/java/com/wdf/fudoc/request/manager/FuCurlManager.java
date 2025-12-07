package com.wdf.fudoc.request.manager;

import com.intellij.httpClient.converters.RequestBuilder;
import com.intellij.httpClient.converters.curl.CurlRequestBuilder;
import com.intellij.httpClient.http.request.*;
import com.intellij.httpClient.http.request.psi.HttpRequest;
import com.intellij.httpClient.http.request.run.HttpRequestValidationException;
import com.intellij.openapi.project.Project;
import cn.fudoc.common.listener.FuDocActionListener;
import com.wdf.fudoc.common.FuDocRender;
import cn.fudoc.common.enumtype.FuDocAction;
import com.wdf.fudoc.common.exception.FuDocException;
import com.wdf.fudoc.request.http.convert.HttpDataConvert;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import lombok.extern.slf4j.Slf4j;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-27 21:22:52
 */
@Slf4j
public class FuCurlManager {


    public static String toCurl(Project project, FuHttpRequestData requestData) {
        if (Objects.isNull(requestData)) {
            return FuStringUtils.EMPTY;
        }
        try {
            //发布动作事件
            project.getMessageBus().syncPublisher(FuDocActionListener.TOPIC).action(FuDocAction.GEN_CURL.getCode());
            HttpRequestPsiFile psiFile = HttpRequestPsiFactory.createDummyFile(project, FuDocRender.httpRender(HttpDataConvert.convert(requestData)));
            HttpRequest newRequest = HttpRequestPsiUtils.getFirstRequest(psiFile);
            if (Objects.isNull(newRequest)) {
                return FuStringUtils.EMPTY;
            }
            CurlRequestBuilder curlRequestBuilder = new CurlRequestBuilder();
            // IDEA 2025.1+ API 变更: getDefault() 的 contextFile 参数不再允许为 null,使用 psiFile
            HttpRequestVariableSubstitutor substitutor = HttpRequestVariableSubstitutor.getDefault(project, psiFile);
            Object curlResult = HttpRequestPsiConverter.convertFromHttpRequest(newRequest, substitutor, (RequestBuilder) curlRequestBuilder);
            // IDEA 2025.1+ API 变更: convertFromHttpRequest 返回 Object,需要调用 toString()
            return curlResult.toString();
        } catch (Exception e) {
            log.error("生成curl命令失败", e);
            throw new FuDocException("生成curl命令失败");
        }
    }


}
