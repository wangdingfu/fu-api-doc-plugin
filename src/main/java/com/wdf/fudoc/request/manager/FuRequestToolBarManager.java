package com.wdf.fudoc.request.manager;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.wdf.fudoc.request.constants.RequestConstants;
import com.wdf.fudoc.request.constants.enumtype.RequestDialog;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.tab.request.RequestTabView;
import com.wdf.fudoc.request.view.FuRequestSettingView;
import com.wdf.fudoc.request.view.HttpDialogView;
import com.wdf.fudoc.request.view.toolwindow.FuRequestWindow;
import icons.FuDocIcons;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 【Fu Request】模块工具类管理类
 *
 * @author wangdingfu
 * @date 2022-10-14 18:15:30
 */
public class FuRequestToolBarManager {

    /**
     * 【Fu Request】弹窗
     */
    private HttpDialogView httpDialogView;

    /**
     * IDEA工具栏窗体
     */
    private FuRequestWindow fuRequestWindow;

    /**
     * 【Fu Request】工具类分组
     */
    private final DefaultActionGroup defaultActionGroup = new DefaultActionGroup();

    /**
     * pin 状态 默认pin住
     */
    @Getter
    public final AtomicBoolean pinStatus = new AtomicBoolean(true);

    /**
     * 请求窗体方式
     */
    private final RequestDialog requestDialog;

    /**
     * 配置面板
     */
    private final FuRequestSettingView fuRequestSettingView;


    public FuRequestToolBarManager(HttpDialogView httpDialogView) {
        this.httpDialogView = httpDialogView;
        this.fuRequestSettingView = new FuRequestSettingView(httpDialogView.getProject());
        this.requestDialog = RequestDialog.HTTP_DIALOG;
    }

    public FuRequestToolBarManager(FuRequestWindow fuRequestWindow) {
        this.fuRequestWindow = fuRequestWindow;
        this.fuRequestSettingView = new FuRequestSettingView(fuRequestWindow.getProject());
        this.requestDialog = RequestDialog.TOOL_WINDOW;
    }

    public static FuRequestToolBarManager getInstance(HttpDialogView httpDialogView) {
        return new FuRequestToolBarManager(httpDialogView);
    }

    public static FuRequestToolBarManager getInstance(FuRequestWindow fuRequestWindow) {
        return new FuRequestToolBarManager(fuRequestWindow);
    }

    /**
     * 初始化工具栏
     */
    public DefaultActionGroup initToolBar() {
        if (RequestDialog.HTTP_DIALOG.equals(this.requestDialog)) {
            addActionByHttpDialog();
        }
        if (RequestDialog.TOOL_WINDOW.equals(this.requestDialog)) {
            addCommonAction(fuRequestWindow.getRequestTabView());
        }
        return defaultActionGroup;
    }


    private void addActionByHttpDialog() {
        //添加公共动作
        addCommonAction(this.httpDialogView.getRequestTabView());


        defaultActionGroup.addSeparator();

        //添加pin
        defaultActionGroup.add(new ToggleAction("Pin", "Pin", AllIcons.General.Pin_tab) {
            @Override
            public boolean isSelected(@NotNull AnActionEvent e) {
                return pinStatus.get();
            }

            @Override
            public void setSelected(@NotNull AnActionEvent e, boolean state) {
                pinStatus.set(state);
            }
        });

        //添加关闭窗口事件
        defaultActionGroup.addAction(new AnAction("Close", "Close", AllIcons.Actions.Cancel) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                httpDialogView.close();
            }
        });

    }


    private void addCommonAction(RequestTabView requestTabView) {
        ActionManager actionManager = ActionManager.getInstance();
        //添加保存事件
        defaultActionGroup.add(new AnAction("Save", "Save", AllIcons.Actions.MenuSaveall) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                FuHttpRequestData fuHttpRequestData = requestTabView.getFuHttpRequestData();
                requestTabView.doSendBefore(fuHttpRequestData);
                //保存当前请求
                FuRequestManager.saveRequest(requestTabView.getProject(), fuHttpRequestData);
            }
        });

        defaultActionGroup.addSeparator();

        //添加刷新事件 JbossJbpmIcons.Icons.Bpmn.Events.Start_16_Message
        defaultActionGroup.add(new AnAction("Refresh", "Refresh", AllIcons.Actions.Refresh) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                System.out.println("点击了刷新按钮");
            }
        });

        //添加请求状态显示按钮 支持终止请求
        AnAction stopAction = actionManager.getAction(RequestConstants.ACTION_REQUEST_TOOLBAR_STOP);
        if (Objects.nonNull(stopAction)) {
            defaultActionGroup.add(stopAction);
        }

        //添加设置按钮
        defaultActionGroup.add(new AnAction("Setting", "Setting", AllIcons.General.Settings) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                if (fuRequestSettingView.showAndGet()) {
                    //保存数据
                    fuRequestSettingView.apply();
                }
            }
        });

        defaultActionGroup.addSeparator();


        //添加帮助文档按钮
        defaultActionGroup.add(new AnAction("帮助文档", "Help", FuDocIcons.FU_DOC) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                System.out.println("点击了帮助文档按钮");
            }
        });

    }


}
