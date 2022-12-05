package com.wdf.fudoc.request.view.toolwindow;

import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.RegisterToolWindowTask;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.wdf.fudoc.request.global.FuRequestWindowData;
import com.wdf.fudoc.request.manager.FuRequestToolBarManager;
import icons.FuDocIcons;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Fu Doc 工具窗口
 *
 * @author wangdingfu
 * @date 2022-08-25 21:50:01
 */
public class FuDocToolWindowFactory implements ToolWindowFactory, DumbAware {


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        FuRequestWindow fuRequestWindow = new FuRequestWindow(project, toolWindow);
        Content content = contentFactory.createContent(fuRequestWindow, "", false);
        DefaultActionGroup defaultActionGroup = FuRequestToolBarManager.getInstance(fuRequestWindow).initToolBar();
        toolWindow.setTitleActions(Lists.newArrayList(defaultActionGroup.getChildActionsOrStubs()));
        toolWindow.getContentManager().addContent(content);
    }


}
