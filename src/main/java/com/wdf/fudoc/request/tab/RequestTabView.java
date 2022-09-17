package com.wdf.fudoc.request.tab;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;
import com.intellij.ui.JBColor;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.test.factory.FuTabBuilder;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * http请求部分内容
 *
 * @author wangdingfu
 * @date 2022-09-17 18:05:36
 */
@Getter
public class RequestTabView implements FuTab {

    private final Project project;

    private final JPanel rootPanel;

    /**
     * 请求类型
     */
    private final JComboBox<String> requestType;

    /**
     * 请求地址
     */
    private final JTextField requestUrl;

    /**
     * 发送按钮
     */
    private final JButton sendBtn;


    private final HttpHeaderTab httpHeaderTab;
    private final HttpParamsTab httpParamsTab;
    private final HttpRequestBodyTab httpRequestBodyTab;

    public RequestTabView(Project project) {
        this.project = project;
        this.rootPanel = new JPanel(new BorderLayout());
        this.requestType = new ComboBox<>(RequestType.getItems());
        this.requestUrl = new JTextField();
        this.sendBtn = new JButton("Send");
        this.httpHeaderTab = new HttpHeaderTab();
        this.httpParamsTab = new HttpParamsTab();
        this.httpRequestBodyTab = new HttpRequestBodyTab();
        initUI();
    }


    private void initUI() {
        //send区域
        this.rootPanel.add(initSendPanel(), BorderLayout.NORTH);
        //请求参数区域
        this.rootPanel.add(FuTabBuilder.getInstance()
                .addTab(this.httpHeaderTab.getTabInfo())
                .addTab(this.httpParamsTab.getTabInfo())
                .addTab(this.httpRequestBodyTab.getTabInfo())
                .build(), BorderLayout.CENTER);
    }

    private JPanel initSendPanel() {
        JPanel sendPane = new JPanel(new BorderLayout());
        //请求类型
        this.requestType.setBackground(new JBColor(new Color(74, 136, 199), new Color(74, 136, 199)));
        sendPane.add(this.requestType, BorderLayout.WEST);
        //请求url
        sendPane.add(this.requestUrl, BorderLayout.CENTER);
        //send按钮
        sendPane.add(this.sendBtn, BorderLayout.EAST);
        return sendPane;
    }


    @Override
    public TabInfo getTabInfo() {
        JRootPane rootPane = new JRootPane();
        final IdeGlassPaneImpl glass = new IdeGlassPaneImpl(rootPane);
        rootPane.setGlassPane(glass);
        glass.setVisible(true);
        rootPane.setContentPane(this.rootPanel);
        rootPane.setDefaultButton(this.getSendBtn());
        return FuTabComponent.getInstance("Request", null, rootPane).builder();
    }
}
