package com.wdf.fudoc.request.view;

import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;
import com.intellij.ui.GuiUtils;
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
    private final FuRequestToolBarManager fuRequestToolBarManager;

    @Getter
    private final PsiElement psiElement;

    private final DefaultActionGroup actionGroup;

    public HttpDialogView(Project project, PsiElement psiElement, FuHttpRequestData httpRequestData) {
        super(project, true);
        this.project = project;
        this.psiElement = psiElement;
        this.fuRequestToolBarManager = FuRequestToolBarManager.getInstance(this);
        this.actionGroup = this.fuRequestToolBarManager.initToolBar();
        initToolBarUI();
        this.requestTabView = new RequestTabView(this.project, this, FuRequestStatusInfoView.getInstance().addWidget(new HttpToolBarWidget(initToolBarUI())).revalidate());
        this.responseTabView = new ResponseTabView(this.project,FuRequestStatusInfoView.getInstance().addWidget(new HttpToolBarWidget(initToolBarUI())).revalidate());
        this.messageComponent = new MessageComponent(true);
        this.statusInfoPanel = this.messageComponent.getRootPanel();
        initRequestUI();
        initResponseUI();
        initUI();
        addMouseListeners();
        initData(httpRequestData);
        setModal(false);
        init();
        setTitle(Objects.isNull(httpRequestData) ? "Send Http Request" : httpRequestData.getApiName());
    }

    @Override
    protected @Nullable Border createContentPaneBorder() {
        return JBUI.Borders.empty();
    }


    @Override
    protected Action @NotNull [] createActions() {
        return new Action[]{};
    }

    @Override
    protected JComponent createSouthPanel() {
        return this.statusInfoPanel;
    }


    /**
     * 初始化工具栏面板
     */
    private JPanel initToolBarUI() {
        JPanel toolBarPanel = new JPanel(new BorderLayout());
        Utils.setSmallerFontForChildren(toolBarPanel);
//        this.toolBarPanel.setBackground(new JBColor(new Color(55, 71, 82), new Color(55, 71, 82)));
        //创建及初始化工具栏
        ToolBarUtils.addActionToToolBar(toolBarPanel, RequestConstants.PLACE_REQUEST_TOOLBAR, this.actionGroup, BorderLayout.EAST);
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



    private void addMouseListeners() {
        WindowMoveListener windowMoveListener = new WindowMoveListener(this.rootPanel);
        this.rootPanel.addMouseListener(windowMoveListener);
        this.rootPanel.addMouseMotionListener(windowMoveListener);
        this.statusInfoPanel.addMouseListener(windowMoveListener);
        this.statusInfoPanel.addMouseMotionListener(windowMoveListener);
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        this.requestTabView.doSendBefore(fuHttpRequestData);
    }

    @Override
    public void doSendAfter(FuHttpRequestData fuHttpRequestData) {
        ApplicationManager.getApplication().invokeLater(() -> {
            this.fuTabBuilder.select(ResponseTabView.RESPONSE);
            this.requestTabView.doSendAfter(fuHttpRequestData);
            this.responseTabView.initData(fuHttpRequestData);
            //切换消息展示
            messageComponent.switchInfo();
        });
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel centerPanel = fuTabBuilder.build();
        centerPanel.setMinimumSize(new Dimension(700, 440));
        centerPanel.setPreferredSize(new Dimension(700, 440));
        return centerPanel;
    }
}
