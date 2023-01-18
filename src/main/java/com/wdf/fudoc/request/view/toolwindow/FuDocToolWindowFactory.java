package com.wdf.fudoc.request.view.toolwindow;

import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.wdf.fudoc.request.manager.FuRequestToolBarManager;
import org.jetbrains.annotations.NotNull;


/**
 * Fu Doc 工具窗口
 *
 * @author wangdingfu
 * @date 2022-08-25 21:50:01
 */
public class FuDocToolWindowFactory implements ToolWindowFactory, DumbAware {


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.getInstance();
        FuRequestWindow fuRequestWindow = new FuRequestWindow(project, toolWindow);
        Content content = contentFactory.createContent(fuRequestWindow, "", false);
        DefaultActionGroup defaultActionGroup = FuRequestToolBarManager.getInstance(fuRequestWindow).initToolBar();
        toolWindow.setTitleActions(Lists.newArrayList(defaultActionGroup.getChildActionsOrStubs()));
        toolWindow.getContentManager().addContent(content);
        //订阅工具栏窗体主题监听器
        project.getMessageBus().connect().subscribe(ToolWindowManagerListener.TOPIC, new ToolWindowManagerListener() {
            @Override
            public void toolWindowShown(@NotNull ToolWindow toolWindow) {
                if ("Fu Request".equals(toolWindow.getId())) {
                    fuRequestWindow.initRootPane();
                }
                ToolWindowManagerListener.super.toolWindowShown(toolWindow);
            }
        });
    }


}
