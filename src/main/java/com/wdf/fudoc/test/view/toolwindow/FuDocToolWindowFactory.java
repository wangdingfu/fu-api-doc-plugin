package com.wdf.fudoc.test.view.toolwindow;

import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
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
        Content content = contentFactory.createContent(new FuRequestWindow(project), "", false);
        final ActionManager actionManager = ActionManager.getInstance();
        DefaultActionGroup defaultActionGroup = (DefaultActionGroup) actionManager.getAction("fudoc.request.toolbar.action");
        List<AnAction> actionList = Lists.newArrayList(defaultActionGroup.getChildActionsOrStubs());
        actionList.add(new AnAction("Save", "Save", AllIcons.Actions.MenuSaveall) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                System.out.println("点击了保存按钮");
            }
        });
        actionList.add(new AnAction("Last", "上一个", AllIcons.Actions.Back) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                System.out.println("点击了上一个按钮");
            }
        });
        actionList.add(new AnAction("Next", "下一个", AllIcons.Actions.Forward) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                System.out.println("点击了下一个按钮");
            }
        });
        actionList.add(new AnAction("帮助文档", "查看帮助文档", FuDocIcons.FU_DOC) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                System.out.println("点击了保存按钮");
            }
        });
        toolWindow.setTitleActions(actionList);
        toolWindow.getContentManager().addContent(content);

    }


}
