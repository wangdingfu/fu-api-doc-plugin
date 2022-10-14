package com.wdf.fudoc.request.view;

import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.components.FuStatusBarComponent;
import com.wdf.fudoc.components.toolbar.PinToolBarAction;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.constants.RequestConstants;
import com.wdf.fudoc.request.global.GlobalHttpRequestView;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.tab.RequestTabView;
import com.wdf.fudoc.request.tab.ResponseTabView;
import com.wdf.fudoc.request.toolbar.FuRequestToolBarManager;
import com.wdf.fudoc.test.factory.FuTabBuilder;
import com.wdf.fudoc.util.PopupUtils;
import com.wdf.fudoc.util.ToolBarUtils;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * 发起http请求的窗口
 *
 * @author wangdingfu
 * @date 2022-09-17 18:06:24
 */
public class HttpDialogView implements HttpCallback {

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

    /**
     * 状态信息面板
     */
    private final FuStatusBarComponent fuStatusBarComponent;
    private final JPanel statusInfoPanel;

    @Setter
    private JBPopup jbPopup;


    public HttpDialogView(Project project) {
        this.project = project;
        this.requestTabView = new RequestTabView(this.project, this);
        this.responseTabView = new ResponseTabView(this.project);
        this.fuStatusBarComponent = new FuStatusBarComponent();
        this.statusInfoPanel = this.fuStatusBarComponent.getRootPanel();
        initToolBarUI();
        initRequestUI();
        initResponseUI();
        initUI();
        fuStatusBarComponent.setInfo("您可以按下esc键来退出当前窗口");
    }

    public void close() {
        this.jbPopup.cancel();
    }

    /**
     * 初始化工具栏面板
     */
    private void initToolBarUI() {
        this.toolBarPanel = new JPanel(new BorderLayout());
        Utils.setSmallerFontForChildren(this.toolBarPanel);
        this.toolBarPanel.setBackground(new JBColor(new Color(55, 71, 82), new Color(55, 71, 82)));
        this.toolBarPanel.add(this.titleLabel, BorderLayout.WEST);
        //创建及初始化工具栏
        DefaultActionGroup defaultActionGroup = FuRequestToolBarManager.getInstance(this).initToolBar();
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
        // 设置边框
        this.rootPanel.setBorder(JBUI.Borders.empty());
        //添加工具栏面板到跟面板上
        this.rootPanel.add(this.toolBarPanel, BorderLayout.NORTH);
        //添加请求相应主面板到跟面板上
        this.rootPanel.add(fuTabBuilder.build(), BorderLayout.CENTER);
        //添加状态信息展示面板到跟面板上
        this.rootPanel.add(this.statusInfoPanel, BorderLayout.SOUTH);
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


    /**
     * 弹出当前页面
     *
     * @param project 当前项目
     */
    public static void popup(Project project, FuHttpRequestData fuHttpRequestData) {
        HttpDialogView httpDialogView = new HttpDialogView(project);
        httpDialogView.initData(fuHttpRequestData);
        httpDialogView.setJbPopup(PopupUtils.create(httpDialogView.getRootPanel(), httpDialogView.getToolBarPanel(), PinToolBarAction.getPinStatus()));
        GlobalHttpRequestView.addHttpDialogView(project, httpDialogView);
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        this.requestTabView.doSendBefore(fuHttpRequestData);
    }

    @Override
    public void doSendAfter(FuHttpRequestData fuHttpRequestData) {
        ApplicationManager.getApplication().invokeLater(() -> {
            this.fuTabBuilder.select(ResponseTabView.RESPONSE);
            this.responseTabView.initData(fuHttpRequestData);
            fuStatusBarComponent.setInfo("请求失败. 连接被拒绝!!!");
        });
    }

}
