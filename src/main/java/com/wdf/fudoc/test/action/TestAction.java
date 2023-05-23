package com.wdf.fudoc.test.action;

import com.intellij.httpClient.converters.RequestBuilder;
import com.intellij.httpClient.execution.HttpRequestConfig;
import com.intellij.httpClient.execution.RestClientFormBodyPart;
import com.intellij.httpClient.execution.RestClientRequest;
import com.intellij.httpClient.execution.RestClientRequestBuilder;
import com.intellij.httpClient.http.request.HttpRequestPsiConverter;
import com.intellij.httpClient.http.request.HttpRequestPsiFile;
import com.intellij.httpClient.http.request.HttpRequestPsiUtils;
import com.intellij.httpClient.http.request.HttpRequestVariableSubstitutor;
import com.intellij.httpClient.http.request.psi.HttpQuery;
import com.intellij.httpClient.http.request.psi.HttpQueryParameter;
import com.intellij.httpClient.http.request.psi.HttpRequest;
import com.intellij.httpClient.http.request.psi.HttpRequestTarget;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.impl.EditorComponentImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ui.UIUtil;
import com.wdf.fudoc.apidoc.sync.data.YApiProjectTableData;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.view.AuthSettingView;
import com.wdf.fudoc.request.view.FuRequestStatusInfoView;
import com.wdf.fudoc.test.view.TestTipPanel;
import com.wdf.fudoc.util.FuRequestUtils;
import com.wdf.fudoc.util.PopupUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.Objects;

@Slf4j
public class TestAction extends AnAction {




    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
//        AuthSettingView authSettingView = new AuthSettingView(e.getProject());
//        PopupUtils.create(authSettingView.getRootPanel(),null,new AtomicBoolean(true));
        HttpRequestVariableSubstitutor substitutor = HttpRequestVariableSubstitutor.getDefault(e.getProject());
        //读取http文件
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);

        HttpRequest httpRequest = FuRequestUtils.getHttpRequest((HttpRequestPsiFile) psiFile, e.getData(LangDataKeys.EDITOR));

        if(Objects.isNull(psiFile)){
            return;
        }
        HttpRequest firstRequest = HttpRequestPsiUtils.getFirstRequest(psiFile);
        if(Objects.isNull(firstRequest)){
            return;
        }
        HttpRequestTarget requestTarget = firstRequest.getRequestTarget();
        String httpUrl = firstRequest.getHttpUrl(substitutor);
        log.info("httpUrl:"+httpUrl);
        String httpMethod = firstRequest.getHttpMethod();
        log.info("httpMethod:"+httpMethod);
        String text = firstRequest.getText();
        log.info("text:"+text);
        HttpQuery query;
        if(Objects.nonNull(requestTarget) && Objects.nonNull(query = requestTarget.getQuery())){
            List<HttpQueryParameter> queryParameterList = query.getQueryParameterList();
            for (HttpQueryParameter httpQueryParameter : queryParameterList) {
                String value = httpQueryParameter.getValue(substitutor);
                String key = httpQueryParameter.getKey(substitutor);
                log.info("key:"+key+"="+value);
            }
        }
        RequestBuilder<RestClientRequest, RestClientFormBodyPart> requestBuilder = new RestClientRequestBuilder();
        HttpRequestConfig requestConfig = HttpRequestPsiConverter.toRequestConfig(firstRequest);
        try {
            RestClientRequest restClientRequest = HttpRequestPsiConverter.convertFromHttpRequest(firstRequest, substitutor, requestBuilder);
            List<File> files = restClientRequest.getFiles();
            String url = restClientRequest.getURL();
            String textToSend = restClientRequest.getTextToSend();
        }catch (Exception exception){
            log.info("解析http请求错误",exception);
        }
    }


}
