package com.wdf.fudoc.request.view;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import com.wdf.fudoc.components.listener.FuActionListener;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.factory.FuHttpRequestDataFactory;
import com.wdf.fudoc.request.pojo.AuthConfigData;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.tab.auth.GeneralAuthTab;
import com.wdf.fudoc.request.tab.auth.JavaCodeAuthTab;
import com.wdf.fudoc.request.tab.auth.JavaScriptCodeAuthTab;
import com.wdf.fudoc.request.tab.request.RequestTabView;
import com.wdf.fudoc.request.tab.request.ResponseTabView;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-02-20 23:54:03
 */
public class AuthConfigView implements HttpCallback, FuActionListener<AuthConfigData> {

    @Getter
    private final JPanel rootPanel;

    /**
     * 请求页
     */
    @Getter
    private final RequestTabView requestTabView;

    /**
     * 响应页
     */
    private final ResponseTabView responseTabView;

    /**
     * 通用配置tab
     */
    private final GeneralAuthTab generalAuthTab;

    /**
     * java代码tab
     */
    private final JavaCodeAuthTab javaCodeAuthTab;

    /**
     * javaScript代码tab
     */
    private final JavaScriptCodeAuthTab javaScriptCodeAuthTab;


    public AuthConfigView(Project project) {
        this.rootPanel = new JPanel(new BorderLayout());
        Splitter splitter = new Splitter(true, 0.4F);
        this.requestTabView = new RequestTabView(project, this, null);
        this.responseTabView = new ResponseTabView(project, null);
        this.generalAuthTab = new GeneralAuthTab(project);
        this.javaCodeAuthTab = new JavaCodeAuthTab();
        this.javaScriptCodeAuthTab = new JavaScriptCodeAuthTab();
        splitter.setFirstComponent(FuTabBuilder.getInstance().addTab(this.requestTabView).addTab(this.responseTabView).build());
        splitter.setSecondComponent(FuTabBuilder.getInstance().addTab(this.javaCodeAuthTab).addTab(this.javaScriptCodeAuthTab).build());
        this.rootPanel.add(splitter, BorderLayout.CENTER);
    }


    @Override
    public void initData(FuHttpRequestData httpRequestData) {
        requestTabView.initData(httpRequestData);
        responseTabView.initData(httpRequestData);
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        this.requestTabView.doSendBefore(fuHttpRequestData);
    }

    @Override
    public void doSendAfter(FuHttpRequestData fuHttpRequestData) {
        responseTabView.doSendAfter(fuHttpRequestData);
    }



    @Override
    public void doAction(AuthConfigData data) {

        initData(formatHttpData(data));
        generalAuthTab.doAction(data);
        javaCodeAuthTab.doAction(data);
        javaScriptCodeAuthTab.doAction(data);
    }

    @Override
    public void doActionAfter(AuthConfigData data) {
        doSendBefore(formatHttpData(data));
        generalAuthTab.doActionAfter(data);
        javaCodeAuthTab.doActionAfter(data);
        javaScriptCodeAuthTab.doActionAfter(data);
    }


    private FuHttpRequestData formatHttpData(AuthConfigData data) {
        if (Objects.isNull(data.getHttpRequestData())) {
            data.setHttpRequestData(FuHttpRequestDataFactory.buildEmptyHttpRequestData());
        }
        return data.getHttpRequestData();
    }

}
