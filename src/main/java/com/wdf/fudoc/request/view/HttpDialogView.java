package com.wdf.fudoc.request.view;

import cn.hutool.core.util.IdUtil;
import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import com.wdf.fudoc.components.listener.SendHttpListener;
import com.wdf.fudoc.components.message.MessageComponent;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.callback.FuRequestCallback;
import com.wdf.fudoc.request.constants.RequestConstants;
import com.wdf.fudoc.request.execute.HttpApiExecutor;
import com.wdf.fudoc.request.manager.FuRequestManager;
import com.wdf.fudoc.request.manager.FuRequestToolBarManager;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.tab.request.RequestTabView;
import com.wdf.fudoc.request.tab.request.ResponseTabView;
import com.wdf.fudoc.util.ToolBarUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 发起http请求的窗口
 *
 * @author wangdingfu
 * @date 2022-09-17 18:06:24
 */
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
     * 状态信息面板
     */
    private final MessageComponent messageComponent;
    private final JPanel statusInfoPanel;

    @Getter
    private PsiElement psiElement;

    private final JPanel toolBarPanel;

    private final boolean isSave;


    @Getter
    private ProgressIndicator progressIndicator;

    @Getter
    public FuHttpRequestData httpRequestData;

    private final AtomicBoolean sendStatus = new AtomicBoolean(false);

    @Getter
    public final String httpId;

    public boolean getSendStatus() {
        return sendStatus.get();
    }

    @Override
    protected void dispose() {
        //当前窗体被销毁了 需要手动移除
        FuRequestManager.remove(this.project, this.httpId);
        super.dispose();
    }


    public HttpDialogView(Project project, PsiElement psiElement, FuHttpRequestData httpRequestData) {
        this(project, psiElement, httpRequestData, false);
    }

    public HttpDialogView(Project project, PsiElement psiElement, FuHttpRequestData httpRequestData, boolean isSave) {
        super(project, true);
        this.project = project;
        this.httpId = IdUtil.getSnowflakeNextIdStr();
        this.isSave = isSave;
        this.requestTabView = new RequestTabView(this.project, this, FuRequestStatusInfoView.getInstance());
        this.responseTabView = new ResponseTabView(this.project, FuRequestStatusInfoView.getInstance());
        this.messageComponent = new MessageComponent(true);
        this.statusInfoPanel = this.messageComponent.getRootPanel();
        this.toolBarPanel = initToolBarUI();
        initRequestUI();
        initResponseUI();
        setModal(isSave);
        init();
        reset(psiElement, httpRequestData);
    }


    public void reset(PsiElement psiElement, FuHttpRequestData fuHttpRequestData) {
        this.psiElement = psiElement;
        this.httpRequestData = fuHttpRequestData;
        initData(fuHttpRequestData);
        setTitle((Objects.isNull(fuHttpRequestData) || StringUtils.isBlank(fuHttpRequestData.getApiName())) ? "Send Http Request" : fuHttpRequestData.getApiName());
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
        if (!this.isSave && Objects.nonNull(this.toolBarPanel)) {
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

    @Override
    protected void doOKAction() {
        //保存数据
        doSendBefore(this.httpRequestData);
        super.doOKAction();
    }

    @Override
    public void doSendHttp() {
        sendStatus.set(true);
        doSendBefore(httpRequestData);
        //发起请求
        HttpApiExecutor.doSendRequest(project, httpRequestData);
        sendStatus.set(false);
        doSendAfter(httpRequestData);
    }

}
