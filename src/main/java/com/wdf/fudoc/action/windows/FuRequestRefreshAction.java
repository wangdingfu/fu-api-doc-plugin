package com.wdf.fudoc.action.windows;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.view.toolwindow.FuRequestWindow;
import org.jetbrains.annotations.NotNull;

/**
 * 【Fu Request】刷新动作
 *
 * @author wangdingfu
 * @date 2022-09-01 20:53:09
 */
public class FuRequestRefreshAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        // 获取当前project对象
        Project project = e.getData(PlatformDataKeys.PROJECT);
        FuRequestWindow fuRequestWindow = e.getData(DataKey.create("FuRequestWindow"));

        if (fuRequestWindow == null || project == null) {
            return;
        }
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Fu request", true) {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                progressIndicator.setText("Fu Request refreshing");
                //TODO 刷新端口号和接口请求信息
            }
        });
    }
}
