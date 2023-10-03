package com.wdf.fudoc.test.action;

import cn.hutool.core.util.RandomUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiFile;
import com.intellij.httpClient.converters.RequestBuilder;
import com.intellij.httpClient.converters.curl.CurlRequestBuilder;
import com.intellij.httpClient.http.request.HttpRequestPsiConverter;
import com.intellij.httpClient.http.request.HttpRequestPsiFile;
import com.intellij.httpClient.http.request.HttpRequestPsiUtils;
import com.intellij.httpClient.http.request.HttpRequestVariableSubstitutor;
import com.intellij.httpClient.http.request.psi.HttpQuery;
import com.intellij.httpClient.http.request.psi.HttpQueryParameter;
import com.intellij.httpClient.http.request.psi.HttpRequest;
import com.intellij.httpClient.http.request.psi.HttpRequestTarget;
import com.intellij.httpClient.http.request.run.HttpRequestValidationException;
import com.wdf.fudoc.common.exception.FuDocException;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiFile;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.navigation.ApiNavigationItem;
import com.wdf.fudoc.navigation.FuApiNavigationExecutor;
import com.wdf.fudoc.navigation.recent.ProjectRecentApi;
import com.wdf.fudoc.navigation.recent.RecentNavigationManager;
import com.wdf.fudoc.util.FuRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@Slf4j
public class TestAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(TestAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

    }


    public void showPanel() {
        KeyValueTableBO keyValueTableBO = new KeyValueTableBO();
    }


    private void apiTest(AnActionEvent e) {
        ProjectRecentApi projectRecentApi = RecentNavigationManager.create(e.getProject());
        long start = System.currentTimeMillis();
        List<ApiNavigationItem> apiList = FuApiNavigationExecutor.getInstance(e.getProject(), projectRecentApi).getApiList();
        System.out.println("读取api【" + apiList.size() + "】条 共计耗时:" + (System.currentTimeMillis() - start) + "ms");
    }

    private void curlTest(AnActionEvent e) {
        HttpRequestVariableSubstitutor substitutor = HttpRequestVariableSubstitutor.getDefault(e.getProject(), null);
        //读取http文件
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);

        HttpRequest httpRequest = FuRequestUtils.getHttpRequest((HttpRequestPsiFile) psiFile, e.getData(LangDataKeys.EDITOR));
        try {
            Object o = HttpRequestPsiConverter.convertFromHttpRequest(httpRequest, substitutor, (RequestBuilder) (new CurlRequestBuilder()));
            System.out.println(o);
        } catch (HttpRequestValidationException e3) {
            log.error("aaa", e3);
        }
    }


    private void request(AnActionEvent e) {
        HttpRequestVariableSubstitutor substitutor = HttpRequestVariableSubstitutor.getDefault(e.getProject(), null);
        //读取http文件
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);

        HttpRequest httpRequest = FuRequestUtils.getHttpRequest((HttpRequestPsiFile) psiFile, e.getData(LangDataKeys.EDITOR));
        try {
            Object o = HttpRequestPsiConverter.convertFromHttpRequest(httpRequest, substitutor, (RequestBuilder) (new CurlRequestBuilder()));
        } catch (HttpRequestValidationException e3) {
            log.error("aaa", e3);
        }
        if (Objects.isNull(psiFile)) {
            return;
        }
        HttpRequest firstRequest = HttpRequestPsiUtils.getFirstRequest(psiFile);
        if (Objects.isNull(firstRequest)) {
            return;
        }
        HttpRequestTarget requestTarget = firstRequest.getRequestTarget();
        String httpUrl = firstRequest.getHttpUrl(substitutor);
        log.info("httpUrl:" + httpUrl);
        String httpMethod = firstRequest.getHttpMethod();
        log.info("httpMethod:" + httpMethod);
        String text = firstRequest.getText();
        log.info("text:" + text);
        HttpQuery query;
        if (Objects.nonNull(requestTarget) && Objects.nonNull(query = requestTarget.getQuery())) {
            List<HttpQueryParameter> queryParameterList = query.getQueryParameterList();
            for (HttpQueryParameter httpQueryParameter : queryParameterList) {
                String value = httpQueryParameter.getValue(substitutor);
                String key = httpQueryParameter.getKey(substitutor);
            }
        }
//        try {
//            RestClientRequest restClientRequest = HttpRequestPsiConverter.convertFromHttpRequest(firstRequest, substitutor, requestBuilder);
//            CurlCopyPastePreProcessor preProcessor = new CurlCopyPastePreProcessor();
//            HttpRequestCopyAsCurlAction httpRequestCopyAsCurlAction = new HttpRequestCopyAsCurlAction();
//            List<File> files = restClientRequest.getFiles();
//            String url = restClientRequest.getURL();
//            String textToSend = restClientRequest.getTextToSend();
//        } catch (Exception exception) {
//            log.info("解析http请求错误", exception);
//        }
    }


}
