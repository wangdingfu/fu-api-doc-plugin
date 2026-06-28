package com.wdf.fudoc.components.action;

import cn.fudoc.common.enumtype.IssueSource;
import cn.fudoc.common.storage.FuDocConfigStorage;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareToggleAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class IssueAction extends DumbAwareToggleAction {

    @NotNull
    protected final IssueSource myMode;

    public IssueAction(@NotNull IssueSource mode) {
        myMode = mode;
        getTemplatePresentation().setText(myMode.myActionName);
        getTemplatePresentation().setDescription(myMode.myActionName);
    }

    @Override
    public final boolean isSelected(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null || project.isDisposed()) {
            return false;
        }
        return myMode.myActionID.equals(FuDocConfigStorage.INSTANCE.readData().getIssueTo());
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        FuDocConfigStorage instance = FuDocConfigStorage.INSTANCE;
        instance.readData().setIssueTo(myMode.myActionID);
        instance.saveData();
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

}
