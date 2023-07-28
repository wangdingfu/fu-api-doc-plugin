package com.wdf.fudoc.common.exception;

import com.intellij.diagnostic.IdeMessagePanel;
import com.intellij.diagnostic.MessagePool;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2023-07-28 23:43:22
 */
public class ErrorMessageComponent {

    private final Map<Project, IdeMessagePanel> messagePanelMap = new ConcurrentHashMap<>();

    public IdeMessagePanel get(Project project) {
        IdeMessagePanel ideMessagePanel = messagePanelMap.get(project);
        if (Objects.isNull(ideMessagePanel)) {
            ideMessagePanel = new IdeMessagePanel(WindowManager.getInstance().getIdeFrame(project), MessagePool.getInstance());
            messagePanelMap.put(project, ideMessagePanel);
        }
        return ideMessagePanel;
    }


    public void showError(Project project) {
        get(project).openErrorsDialog(null);
    }

}
