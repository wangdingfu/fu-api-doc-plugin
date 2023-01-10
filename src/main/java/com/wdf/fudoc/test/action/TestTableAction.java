package com.wdf.fudoc.test.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.view.dialog.SyncApiCategoryDialog;
import com.wdf.fudoc.util.FuDocUtils;
import com.wdf.fudoc.util.PsiClassUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangdingfu
 * @date 2022-09-05 19:39:54
 */
public class TestTableAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Module module = ModuleUtil.findModuleForPsiElement(PsiClassUtils.getTargetElement(e));
        SyncApiCategoryDialog syncApiCategoryDialog = new SyncApiCategoryDialog(e.getProject(), false, module.getName(), null);
        if (syncApiCategoryDialog.showAndGet()) {
            ApiProjectDTO selectCategory = syncApiCategoryDialog.getSelected();
            System.out.println("OK:" + selectCategory);
        } else {
            ApiProjectDTO selectCategory = syncApiCategoryDialog.getSelected();
            System.out.println("CANCEL:" + selectCategory);
        }
    }
}
