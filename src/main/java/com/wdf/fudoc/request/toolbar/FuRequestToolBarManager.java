package com.wdf.fudoc.request.toolbar;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.wdf.fudoc.request.constants.RequestConstants;
import com.wdf.fudoc.request.view.HttpDialogView;
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
    private final HttpDialogView httpDialogView;

    /**
     * 【Fu Request】工具类分组
     */
    private final DefaultActionGroup defaultActionGroup;

    /**
     * pin 状态 默认pin住
     */
    @Getter
    public final AtomicBoolean pinStatus = new AtomicBoolean(true);


    public FuRequestToolBarManager(HttpDialogView httpDialogView) {
        this.httpDialogView = httpDialogView;
        this.defaultActionGroup = new DefaultActionGroup();
    }


    public static FuRequestToolBarManager getInstance(HttpDialogView httpDialogView) {
        return new FuRequestToolBarManager(httpDialogView);
    }

    /**
     * 初始化工具栏
     */
    public DefaultActionGroup initToolBar() {
        ActionManager actionManager = ActionManager.getInstance();

        //添加保存事件
        defaultActionGroup.add(new AnAction("Save", "Save", AllIcons.Actions.MenuSaveall) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                System.out.println("点击了保存按钮");
            }
        });

        defaultActionGroup.addSeparator();

        //添加刷新事件
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
                System.out.println("点击了设置按钮");
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

        defaultActionGroup.addSeparator();

        //添加pin
        defaultActionGroup.add(new ToggleAction() {
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
        return defaultActionGroup;
    }


}
