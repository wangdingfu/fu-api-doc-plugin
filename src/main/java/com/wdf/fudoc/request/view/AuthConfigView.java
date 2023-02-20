package com.wdf.fudoc.request.view;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.factory.FuHttpRequestDataFactory;
import com.wdf.fudoc.request.pojo.AuthConfigData;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
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
public class AuthConfigView implements HttpCallback {

    @Getter
    private final JPanel rootPanel;

    /**
     * 鉴权配置数据
     */
    private AuthConfigData authConfigData;

    /**
     * 请求页
     */
    @Getter
    private RequestTabView requestTabView;

    /**
     * 响应页
     */
    private ResponseTabView responseTabView;


    public AuthConfigView(Project project) {
        this.rootPanel = new JPanel(new BorderLayout());
        Splitter splitter = new Splitter(true, 0.6F);
        this.requestTabView = new RequestTabView(project, this);
        this.responseTabView = new ResponseTabView(project);
        splitter.setFirstComponent(FuTabBuilder.getInstance().addTab(this.requestTabView).addTab(this.responseTabView).build());
        splitter.setSecondComponent(new JPanel());
        this.rootPanel.add(splitter, BorderLayout.CENTER);
    }

    public void init(AuthConfigData authConfigData) {
        this.authConfigData = authConfigData;
        FuHttpRequestData httpRequestData = this.authConfigData.getHttpRequestData();
        if(Objects.isNull(httpRequestData)){
            httpRequestData = FuHttpRequestDataFactory.buildEmptyHttpRequestData();
        }
        initData(httpRequestData);
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
}
