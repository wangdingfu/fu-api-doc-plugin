package com.wdf.fudoc.view.toolwindow;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
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
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(new FuRequestWindow(project), "", false);
        toolWindow.getContentManager().addContent(content);

    }


}
