package com.wdf.fudoc.components.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareToggleAction;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.request.constants.enumtype.ViewMode;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.storage.factory.FuRequestConfigStorageFactory;
import org.jetbrains.annotations.NotNull;

public class FuRequestViewModeAction extends DumbAwareToggleAction {

    @NotNull
    protected final ViewMode myMode;

    public FuRequestViewModeAction(@NotNull ViewMode mode) {
        myMode = mode;
        getTemplatePresentation().setText(myMode.myActionID);
        getTemplatePresentation().setDescription(myMode.myActionID);
    }

    @Override
    public final boolean isSelected(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null || project.isDisposed()) {
            return false;
        }
        String viewMode = FuRequestConfigStorageFactory.get(e.getProject()).readData().getViewMode();
        return myMode.myActionID.equals(viewMode);
    }

    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        FuRequestConfigStorage fuRequestConfigStorage = FuRequestConfigStorageFactory.get(e.getProject());
        FuRequestConfigPO fuRequestConfigPO = fuRequestConfigStorage.readData();
        fuRequestConfigPO.setViewMode(myMode.myActionID);
        fuRequestConfigStorage.saveData(fuRequestConfigPO);


    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

}
