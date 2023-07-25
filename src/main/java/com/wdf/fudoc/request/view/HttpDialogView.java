package com.wdf.fudoc.request.view;

import cn.hutool.core.util.IdUtil;
import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import com.wdf.fudoc.components.listener.SendHttpListener;
import com.wdf.fudoc.components.message.MessageComponent;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.SendRequestHandler;
import com.wdf.fudoc.request.callback.FuRequestCallback;
import com.wdf.fudoc.request.constants.RequestConstants;
import com.wdf.fudoc.request.manager.FuRequestManager;
import com.wdf.fudoc.request.manager.FuRequestToolBarManager;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.tab.request.RequestConsoleTabView;
import com.wdf.fudoc.request.tab.request.RequestTabView;
import com.wdf.fudoc.request.tab.request.ResponseTabView;
import com.wdf.fudoc.request.view.widget.EnvWidget;
import com.wdf.fudoc.request.view.widget.UserWidget;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.util.ToolBarUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class HttpDialogView extends DialogWrapper implements HttpCallback, SendHttpListener, FuRequestCallback {

    /**
     * 当前项目
     */
    @Getter
    public final Project project;

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
     * 日志控制台
     */
    private final RequestConsoleTabView requestConsoleTabView;

    /**
     * 状态信息面板
     */
    private final MessageComponent messageComponent;
    private final JPanel statusInfoPanel;

    private final EnvWidget envWidget;
    private final UserWidget userWidget;

    @Getter
    private PsiElement psiElement;

    private final boolean isSave;


    private final SendRequestHandler sendRequestHandler;

    @Getter
    public FuHttpRequestData httpRequestData;

    @Getter
    public final String httpId;

    public boolean getSendStatus() {
        return sendRequestHandler.getSendStatus();
    }

    @Override
    public void stopHttp() {
        sendRequestHandler.stopHttp();
    }

    @Override
    protected void dispose() {
        //当前窗体被销毁了 需要手动移除
        FuRequestManager.remove(this.project, this.httpId);
        //持久化数据
        saveData();
        super.dispose();
    }


    public void saveData() {
        try {
            //保存当前请求
            FuRequestManager.saveRequest(project, httpRequestData);
            //保存一些配置数据
            FuRequestConfigStorage.get(project).saveData();
        } catch (Exception e) {
            log.info("持久化请求数据异常", e);
        }
    }


    public HttpDialogView(Project project, PsiElement psiElement, FuHttpRequestData httpRequestData) {
        this(project, psiElement, httpRequestData, false);
    }

    public HttpDialogView(Project project, PsiElement psiElement, FuHttpRequestData httpRequestData, boolean isSave) {
        super(project, true);
        this.project = project;
        this.httpId = IdUtil.getSnowflakeNextIdStr();
        this.isSave = isSave;
        JPanel slidePanel = new JPanel(new BorderLayout());
        slidePanel.add(initToolBarUI(), BorderLayout.EAST);
        this.requestTabView = new RequestTabView(this.project, this, slidePanel, getDisposable());
        this.responseTabView = new ResponseTabView(this.project, slidePanel, getDisposable());
        this.requestConsoleTabView = new RequestConsoleTabView(this.project, slidePanel, getDisposable());
        this.messageComponent = new MessageComponent(true);
        this.statusInfoPanel = this.messageComponent.getRootPanel();
        this.sendRequestHandler = new SendRequestHandler(project, this, this.requestConsoleTabView.getFuLogger());
        this.envWidget = new EnvWidget(this.project, this.requestTabView);
        this.userWidget = new UserWidget(project);
        this.messageComponent.addWidget(this.envWidget);
        this.messageComponent.addWidget(this.userWidget);
        initUI();
        setModal(isSave);
        init();
        reset(psiElement, httpRequestData);
    }


    public void reset(PsiElement psiElement, FuHttpRequestData fuHttpRequestData) {
        this.psiElement = psiElement;
        this.httpRequestData = fuHttpRequestData;
        initData(fuHttpRequestData);
        setTitle((Objects.isNull(fuHttpRequestData) || StringUtils.isBlank(fuHttpRequestData.getApiName())) ? "Send Http Request" : fuHttpRequestData.getApiName());
        refresh();
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
    protected JComponent createSouthPanel() {
        return isSave ? super.createSouthPanel() : this.statusInfoPanel;
    }


    @Override
    public void refresh() {
        this.envWidget.refresh();
        this.userWidget.refresh();
    }

    /**
     * 初始化工具栏面板
     */
    private JPanel initToolBarUI() {
        JPanel toolBarPanel = new JPanel(new BorderLayout());
        Utils.setSmallerFontForChildren(toolBarPanel);
        FuRequestToolBarManager instance = FuRequestToolBarManager.getInstance(this);
        //创建及初始化工具栏
        ToolBarUtils.addActionToToolBar(toolBarPanel, RequestConstants.PLACE_REQUEST_TOOLBAR, instance.initToolBar(), BorderLayout.EAST);
        return toolBarPanel;
    }


    /**
     * 初始化请求部分面板
     */
    private void initUI() {
        fuTabBuilder.addTab(this.requestTabView, initToolBarUI()).addTab(this.responseTabView, initToolBarUI()).addTab(this.requestConsoleTabView, initToolBarUI());
    }

    /**
     * 初始化请求数据
     *
     * @param fuHttpRequestData http请求数据
     */
    public void initData(FuHttpRequestData fuHttpRequestData) {
        this.httpRequestData = fuHttpRequestData;
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
        centerPanel.setMinimumSize(new Dimension(400, 340));
        centerPanel.setPreferredSize(new Dimension(700, 440));
        centerPanel.setMaximumSize(new Dimension(700, 840));
        return centerPanel;
    }

    @Override
    protected void doOKAction() {
        //保存数据
        doSendBefore(this.httpRequestData);
        super.doOKAction();
    }

    @Override
    public void doSendHttp() {
        sendRequestHandler.doSend(this.httpRequestData);
    }

}
