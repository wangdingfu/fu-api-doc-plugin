package com.wdf.fudoc.request.view;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.URLUtil;
import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.common.constant.FuDocConstants;
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
import com.wdf.fudoc.request.tab.request.RequestTabView;
import com.wdf.fudoc.request.tab.request.ResponseTabView;
import com.wdf.fudoc.spring.SpringConfigManager;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.util.ToolBarUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

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
     * 状态信息面板
     */
    private final MessageComponent messageComponent;
    private final JPanel statusInfoPanel;

    @Getter
    private PsiElement psiElement;

    private final JPanel toolBarPanel;

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
        try {
            //当前窗体被销毁了 需要手动移除
            FuRequestManager.remove(this.project, this.httpId);
            //保存当前请求
            FuRequestManager.saveRequest(project, httpRequestData);
            //保存一些配置数据
            FuRequestConfigStorage.getInstance(project).saveData();
        } catch (Exception e) {
            log.error("持久化请求数据异常", e);
        }
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
        this.requestTabView = new RequestTabView(this.project, this, FuRequestStatusInfoView.getInstance(project));
        this.responseTabView = new ResponseTabView(this.project, FuRequestStatusInfoView.getInstance(project));
        this.messageComponent = new MessageComponent(true);
        this.statusInfoPanel = this.messageComponent.getRootPanel();
        this.toolBarPanel = initToolBarUI();
        this.sendRequestHandler = new SendRequestHandler(project, this);
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
//        toolBarPanel.setBackground(new JBColor(new Color(55, 71, 82), new Color(55, 71, 82)));
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

    @Override
    public boolean isShowViewMode() {
        return true;
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
        centerPanel.setMaximumSize(new Dimension(1000, 840));
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
