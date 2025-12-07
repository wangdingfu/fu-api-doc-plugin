package com.wdf.fudoc.request.view.toolwindow;

import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiElement;
import com.wdf.fudoc.common.datakey.FuDocDataKey;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import com.wdf.fudoc.components.listener.SendHttpListener;
import com.wdf.fudoc.components.message.MessageComponent;
import cn.fudoc.common.msg.FuMsgBuilder;
import cn.fudoc.common.enumtype.FuColor;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.SendRequestHandler;
import com.wdf.fudoc.request.callback.FuRequestCallback;
import com.wdf.fudoc.request.manager.FuRequestManager;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.tab.request.RequestConsoleTabView;
import com.wdf.fudoc.request.tab.request.RequestTabView;
import com.wdf.fudoc.request.tab.request.ResponseHeaderTabView;
import com.wdf.fudoc.request.tab.request.ResponseTabView;
import com.wdf.fudoc.request.view.widget.EnvWidget;
import com.wdf.fudoc.request.view.widget.UserWidget;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangdingfu
 * @date 2022-08-25 22:07:47
 */
@Slf4j
public class FuRequestWindow extends SimpleToolWindowPanel implements DataProvider, HttpCallback, SendHttpListener, FuRequestCallback {

    /**
     * 根面板
     */
    @Getter
    private final JPanel rootPanel;

    @Getter
    private final ToolWindow toolWindow;

    @Getter
    private final Project project;

    /**
     * 请求面板
     */
    @Getter
    private final RequestTabView requestTabView;

    /**
     * 响应面板
     */
    private final ResponseTabView responseTabView;
    /**
     * 响应头面板
     */
    private final ResponseHeaderTabView responseHeaderTabView;

    /**
     * 状态信息面板
     */
    private final MessageComponent messageComponent;

    private final SendRequestHandler sendRequestHandler;

    private final EnvWidget envWidget;
    private final UserWidget userWidget;

    @Setter
    @Getter
    private PsiElement psiElement;


    private FuHttpRequestData httpRequestData;


    public boolean getSendStatus() {
        return sendRequestHandler.getSendStatus();
    }

    public FuRequestWindow(@NotNull Project project, ToolWindow toolWindow) {
        // IDEA 2025.1+ 修复: 使用 primitive boolean 而非 Boolean 对象
        super(true, true);
        this.project = project;
        this.toolWindow = toolWindow;
        this.rootPanel = new JPanel(new BorderLayout());
        Splitter splitter = new Splitter(true, 0.6F);
        this.requestTabView = new RequestTabView(project, this, null, toolWindow.getDisposable());
        this.responseTabView = new ResponseTabView(project, null, toolWindow.getDisposable());
        this.envWidget = new EnvWidget(this.project, this.requestTabView, this);
        this.userWidget = new UserWidget(project, this);
        RequestConsoleTabView requestConsoleTabView = new RequestConsoleTabView(this.project, null, toolWindow.getDisposable());
        this.responseHeaderTabView = new ResponseHeaderTabView(project);
        splitter.setFirstComponent(this.requestTabView.getRootPane());
        FuTabBuilder fuTabBuilder = FuTabBuilder.getInstance().addTab(this.responseTabView).addTab(this.responseHeaderTabView).addTab(requestConsoleTabView);
        splitter.setSecondComponent(fuTabBuilder.build());
        this.rootPanel.add(splitter, BorderLayout.CENTER);
        this.messageComponent = new MessageComponent(true);
        this.messageComponent.addWidget(this.envWidget);
        this.messageComponent.addWidget(this.userWidget);
        this.messageComponent.switchInfo();
        this.rootPanel.add(this.messageComponent.getRootPanel(), BorderLayout.SOUTH);
        setContent(this.rootPanel);
        this.sendRequestHandler = new SendRequestHandler(project, this, requestConsoleTabView.getFuLogger());
    }


    @Override
    public void refresh() {
        this.envWidget.refresh();
        this.userWidget.refresh();
    }

    @Override
    public @Nullable Object getData(@NotNull @NonNls String dataId) {
        if (FuDocDataKey.WINDOW_PANE.is(dataId)) {
            return this;
        }
        return super.getData(dataId);
    }

    @Override
    public void stopHttp() {
        this.sendRequestHandler.stopHttp();
    }

    @Override
    public void initData(FuHttpRequestData httpRequestData) {
        this.httpRequestData = httpRequestData;
        this.requestTabView.initData(httpRequestData);
        this.responseTabView.initData(httpRequestData);
        this.responseHeaderTabView.initData(httpRequestData);
        this.messageComponent.switchInfo();
        refresh();
    }

    @Override
    public boolean isWindow() {
        return true;
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        this.requestTabView.doSendBefore(fuHttpRequestData);
    }

    @Override
    public void doSendAfter(FuHttpRequestData fuHttpRequestData) {
        ApplicationManager.getApplication().invokeLater(() -> {
            // IDEA 2025.1+ 修复: 必须调用 requestTabView.doSendAfter 来恢复 Send 按钮状态
            // 无论请求成功、失败还是被取消,都必须调用以恢复 UI 状态
            this.requestTabView.doSendAfter(fuHttpRequestData);

            // IDEA 2025.1+ 修复: 被取消的请求(fuHttpRequestData == null)不填充响应数据
            if (fuHttpRequestData != null) {
                //填充响应面板数据
                this.responseTabView.initData(fuHttpRequestData);
                //填充响应头面板数据
                this.responseHeaderTabView.initData(fuHttpRequestData);

                // 请求完成后保存数据（包含响应数据）
                try {
                    FuRequestManager.saveRequest(project, fuHttpRequestData);
                    FuRequestConfigStorage.get(project).saveData();
                } catch (Exception e) {
                    log.info("持久化请求数据异常", e);
                }

                // IDEA 2025.1+ 新增: 在底部状态栏显示请求结果
                Integer httpCode = fuHttpRequestData.getHttpCode();
                Long time = fuHttpRequestData.getTime();
                if (httpCode != null && time != null) {
                    boolean isOk = fuHttpRequestData.isOk();
                    String statusIcon = isOk ? "✓" : "✗";
                    FuColor color = isOk ? FuColor.GREEN : FuColor.RED;

                    // 格式化请求时间(精确到毫秒)
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
                    String requestTime = sdf.format(new Date());

                    cn.fudoc.common.msg.bo.FuMsgBO msgBO = FuMsgBuilder.getInstance()
                            .text(statusIcon + " 请求完成 | 状态: ")
                            .text(String.valueOf(httpCode), color)
                            .text(" | 耗时: ")
                            .text(time + "ms", FuColor.GREEN)
                            .text(" | 时间: " + requestTime)
                            .build();
                    this.messageComponent.setMsg(msgBO);
                }
            }
        });
    }


    public void initRootPane() {
        this.requestTabView.initRootPane();
        this.responseTabView.initRootPane();
    }


    @Override
    public void doSendHttp() {
        //同步表单数据到 httpRequestData（确保请求参数都被保存）
        doSendBefore(httpRequestData);
        sendRequestHandler.doSend(httpRequestData);
        // 注意：不在这里保存请求，因为 doSend 是异步的
        // 响应数据会在请求完成后由 doSendAfter 保存
    }
}
