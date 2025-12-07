package com.wdf.fudoc.apilist.view;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * API 列表工具窗口工厂
 *
 * @author wangdingfu
 * @date 2025-01-12
 */
public class ApiListToolWindowFactory implements ToolWindowFactory, DumbAware {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // 创建 API 列表面板
        ApiListToolWindow apiListToolWindow = new ApiListToolWindow(project);

        // 创建 Content 并添加到 ToolWindow
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(apiListToolWindow, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
