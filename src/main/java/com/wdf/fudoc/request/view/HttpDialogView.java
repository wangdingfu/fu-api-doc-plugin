package com.wdf.fudoc.request.view;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.components.toolbar.PinToolBarAction;
import com.wdf.fudoc.request.constants.RequestConstants;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.tab.RequestTabView;
import com.wdf.fudoc.request.tab.ResponseTabView;
import com.wdf.fudoc.test.factory.FuTabBuilder;
import com.wdf.fudoc.util.PopupUtils;
import com.wdf.fudoc.util.ToolBarUtils;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * 发起http请求的窗口
 *
 * @author wangdingfu
 * @date 2022-09-17 18:06:24
 */
public class HttpDialogView {

    /**
     * 根面板
     */
    @Getter
    private JPanel rootPanel;

    /**
     * 工具栏面板
     */
    @Getter
    private JPanel toolBarPanel;

    /**
     * 当前项目
     */
    private final Project project;

    /**
     * 请求接口标题
     */
    private final JLabel titleLabel = new JLabel("  测试http接口请求");

    /**
     * tab页构建器
     */
    private final FuTabBuilder fuTabBuilder = FuTabBuilder.getInstance();

    /**
     * 请求页
     */
    @Getter
    private final RequestTabView requestTabView;

    /**
     * 响应页
     */
    private final ResponseTabView responseTabView;


    public HttpDialogView(Project project) {
        this.project = project;
        this.requestTabView = new RequestTabView(this.project, this);
        this.responseTabView = new ResponseTabView(this.project);

        initToolBarUI();
        initRequestUI();
        initResponseUI();
        initUI();
    }

    /**
     * 初始化工具栏面板
     */
    private void initToolBarUI() {
        this.toolBarPanel = new JPanel(new BorderLayout());
        this.toolBarPanel.setBackground(new JBColor(new Color(55, 71, 82), new Color(55, 71, 82)));
        this.toolBarPanel.add(this.titleLabel, BorderLayout.WEST);
        final ActionManager actionManager = ActionManager.getInstance();
        DefaultActionGroup defaultActionGroup = (DefaultActionGroup) actionManager.getAction(RequestConstants.ACTION_REQUEST_TOOLBAR);
        defaultActionGroup.addSeparator();
        ToolBarUtils.genToolBarPanel(this.toolBarPanel, RequestConstants.PLACE_REQUEST_TOOLBAR, defaultActionGroup, BorderLayout.EAST);
    }


    /**
     * 初始化请求部分面板
     */
    private void initRequestUI() {
        fuTabBuilder.addTab(this.requestTabView);
    }

    /**
     * 初始化响应部分面板
     */
    private void initResponseUI() {
        fuTabBuilder.addTab(this.responseTabView);
    }


    /**
     * 初始化当前页面
     */
    private void initUI() {
        this.rootPanel = new JPanel(new BorderLayout());
        this.rootPanel.setMinimumSize(new Dimension(700, 440));
        this.rootPanel.setPreferredSize(new Dimension(700, 440));
        // 边框
        this.rootPanel.setBorder(JBUI.Borders.empty());
        this.rootPanel.add(this.toolBarPanel, BorderLayout.NORTH);
        this.rootPanel.add(fuTabBuilder.build(), BorderLayout.CENTER);
    }

    /**
     * 初始化请求数据
     *
     * @param fuHttpRequestData http请求数据
     */
    public void initData(FuHttpRequestData fuHttpRequestData) {
        if (Objects.isNull(fuHttpRequestData)) {
            return;
        }
        this.titleLabel.setText(fuHttpRequestData.getApiName());
        this.requestTabView.initData(fuHttpRequestData);
        initResponseData(fuHttpRequestData);
    }



    public void initResponseData(FuHttpRequestData fuHttpRequestData) {
        this.responseTabView.initData(fuHttpRequestData);
    }


    public void sendAfter(FuHttpRequestData fuHttpRequestData) {
        initResponseData(fuHttpRequestData);
        this.fuTabBuilder.select(ResponseTabView.RESPONSE);
    }
    /**
     * 弹出当前页面
     *
     * @param project 当前项目
     */
    public static void popup(Project project, FuHttpRequestData fuHttpRequestData) {
        HttpDialogView httpDialogView = new HttpDialogView(project);
        httpDialogView.initData(fuHttpRequestData);
        PopupUtils.create(httpDialogView.getRootPanel(), httpDialogView.getToolBarPanel(), PinToolBarAction.getPinStatus());
    }
}
