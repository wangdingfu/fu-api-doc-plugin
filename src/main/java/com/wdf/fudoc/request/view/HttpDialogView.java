package com.wdf.fudoc.request.view;

import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;
import com.intellij.ui.GuiUtils;
import com.intellij.ui.JBColor;
import com.intellij.ui.WindowMoveListener;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import com.wdf.fudoc.components.message.MessageComponent;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.constants.RequestConstants;
import com.wdf.fudoc.request.manager.FuRequestToolBarManager;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.tab.request.RequestTabView;
import com.wdf.fudoc.request.tab.request.ResponseTabView;
import com.wdf.fudoc.request.view.widget.HttpToolBarWidget;
import com.wdf.fudoc.util.ToolBarUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Objects;

/**
 * 发起http请求的窗口
 *
 * @author wangdingfu
 * @date 2022-09-17 18:06:24
 */
public class HttpDialogView extends DialogWrapper implements HttpCallback {

    /**
     * 根面板
     */
    @Getter
    private JPanel rootPanel;

    /**
     * 当前项目
     */
    @Getter
    private final Project project;

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
    private final MessageComponent messageComponent;
    private final JPanel statusInfoPanel;

    @Getter
    private final PsiElement psiElement;

    private final JPanel toolBarPanel;

    private final boolean isSave;


    public HttpDialogView(Project project, PsiElement psiElement, FuHttpRequestData httpRequestData) {
        this(project, psiElement, httpRequestData, false);
    }

    public HttpDialogView(Project project, PsiElement psiElement, FuHttpRequestData httpRequestData, boolean isSave) {
        super(project, true);
        this.project = project;
        this.isSave = isSave;
        this.psiElement = psiElement;
        this.requestTabView = new RequestTabView(this.project, this, FuRequestStatusInfoView.getInstance());
        this.responseTabView = new ResponseTabView(this.project, FuRequestStatusInfoView.getInstance());
        this.messageComponent = new MessageComponent(true);
        this.statusInfoPanel = this.messageComponent.getRootPanel();
        this.toolBarPanel = initToolBarUI();
        initRequestUI();
        initResponseUI();
        initData(httpRequestData);
        setModal(isSave);
        init();
        setTitle((Objects.isNull(httpRequestData) || StringUtils.isBlank(httpRequestData.getApiName())) ? "Send Http Request" : httpRequestData.getApiName());
    }

    @Override
    protected @Nullable Border createContentPaneBorder() {
        return JBUI.Borders.empty();
    }


    @Override
    protected Action @NotNull [] createActions() {
        return isSave ? super.createActions() : new Action[]{};
    }

    @Override
    protected @Nullable JComponent createTitlePane() {
        if (Objects.nonNull(this.psiElement)) {
            //如果不是在代码中弹出的接口 则不需要添加工具栏面板
            return this.toolBarPanel;
        }
        return super.createTitlePane();
    }

    @Override
    protected JComponent createSouthPanel() {
        return isSave ? super.createSouthPanel() : this.statusInfoPanel;
    }


    /**
     * 初始化工具栏面板
     */
    private JPanel initToolBarUI() {
        JPanel toolBarPanel = new JPanel(new BorderLayout());
        Utils.setSmallerFontForChildren(toolBarPanel);
        toolBarPanel.setBackground(new JBColor(new Color(55, 71, 82), new Color(55, 71, 82)));
        FuRequestToolBarManager instance = FuRequestToolBarManager.getInstance(this);
        //创建及初始化工具栏
        ToolBarUtils.addActionToToolBar(toolBarPanel, RequestConstants.PLACE_REQUEST_TOOLBAR, instance.initToolBar(), BorderLayout.EAST);
        return toolBarPanel;
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
        if (Objects.nonNull(this.psiElement)) {
            //如果不是在代码中弹出的接口 则不需要添加工具栏面板
            this.rootPanel.add(initToolBarUI(), BorderLayout.NORTH);
        }
        //添加请求相应主面板到跟面板上
        this.rootPanel.add(fuTabBuilder.build(), BorderLayout.CENTER);
        //添加状态信息展示面板到跟面板上
        this.rootPanel.add(this.statusInfoPanel, BorderLayout.SOUTH);
        GuiUtils.replaceJSplitPaneWithIDEASplitter(this.rootPanel, true);
    }

    /**
     * 初始化请求数据
     *
     * @param fuHttpRequestData http请求数据
     */
    public void initData(FuHttpRequestData fuHttpRequestData) {
        //切换消息
        messageComponent.switchInfo();
        if (Objects.isNull(fuHttpRequestData)) {
            return;
        }
        this.requestTabView.initData(fuHttpRequestData);
        initResponseData(fuHttpRequestData);
    }


    public void initResponseData(FuHttpRequestData fuHttpRequestData) {
        this.responseTabView.initData(fuHttpRequestData);
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        this.requestTabView.doSendBefore(fuHttpRequestData);
    }

    @Override
    public void doSendAfter(FuHttpRequestData fuHttpRequestData) {
        this.fuTabBuilder.select(ResponseTabView.RESPONSE);
        this.requestTabView.doSendAfter(fuHttpRequestData);
        this.responseTabView.initData(fuHttpRequestData);
        //切换消息展示
        messageComponent.switchInfo();
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel centerPanel = fuTabBuilder.build();
        centerPanel.setMinimumSize(new Dimension(700, 440));
        centerPanel.setPreferredSize(new Dimension(700, 440));
        return centerPanel;
    }
}
