package com.wdf.fudoc.common.notification;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.NlsContexts;
import com.wdf.fudoc.util.PopupUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 弹出面板通知事件
 *
 * @author wangdingfu
 * @date 2023-01-19 14:56:39
 */
public class PanelNotificationAction extends NotificationAction {

    private JPanel rootPanel;

    public PanelNotificationAction(@Nullable @NlsContexts.NotificationContent String text, JPanel showPanel) {
        super(text);
        this.rootPanel = showPanel;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
        PopupUtils.create(this.rootPanel, this.rootPanel, new AtomicBoolean(false));
    }
}
