package com.wdf.fudoc.test.action.editor;

import com.intellij.json.JsonFileType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.wdf.fudoc.common.constant.FuDocConstants;
import icons.FuDocIcons;
import com.wdf.fudoc.util.FuStringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-09-08 21:05:09
 */
public class FuEditorFormatAction extends AnAction {

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    public FuEditorFormatAction() {
        super("格式化json", "Format", FuDocIcons.FU_DOC);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        if (presentation.isEnabled()) {
            Editor editor = e.getData(CommonDataKeys.EDITOR);
            if (editor != null && e.getProject() != null) {
                PsiFile file = PsiDocumentManager.getInstance(e.getProject()).getPsiFile(editor.getDocument());
                if (Objects.nonNull(file) && FuStringUtils.isNotBlank(file.getName())) {
                    e.getPresentation().setEnabledAndVisible(file.getName().equals(FuDocConstants.FU_DOC_FILE + JsonFileType.DEFAULT_EXTENSION));
                }
            } else {
                e.getPresentation().setEnabledAndVisible(false);
            }
            super.update(e);
        }
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        if (psiFile != null && !editor.getDocument().getText().isBlank()) {
            WriteCommandAction.runWriteCommandAction(project, () -> {
                CodeStyleManager.getInstance(project).reformat(psiFile);
            });
        }
    }
}
