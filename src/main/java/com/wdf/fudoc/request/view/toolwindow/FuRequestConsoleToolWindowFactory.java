package com.wdf.fudoc.request.view.toolwindow;

import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleView;
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
public class FuRequestConsoleToolWindowFactory implements ToolWindowFactory, DumbAware {


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.getInstance();
        // 创建控制台实例
        ConsoleView consoleView = new ConsoleViewImpl(project, false);
        Content content = contentFactory.createContent(consoleView.getComponent(), "", false);
        toolWindow.getContentManager().addContent(content);
        toolWindow.setToHideOnEmptyContent(true);

    }


}
