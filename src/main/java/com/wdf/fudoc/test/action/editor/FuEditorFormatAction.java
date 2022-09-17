package com.wdf.fudoc.test.action.editor;

import com.intellij.json.JsonFileType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.wdf.fudoc.common.constant.FuDocConstants;
import icons.FuDocIcons;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-09-08 21:05:09
 */
public class FuEditorFormatAction extends AnAction {


    public FuEditorFormatAction() {
        super("美化json", "Format", FuDocIcons.FU_REQUEST_MAGIC);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        if (presentation.isEnabled()) {
            Editor editor = e.getData(CommonDataKeys.EDITOR);
            if (editor != null && e.getProject() != null) {
                PsiFile file = PsiDocumentManager.getInstance(e.getProject()).getPsiFile(editor.getDocument());
                if (Objects.nonNull(file) && StringUtils.isNotBlank(file.getName())) {
                    e.getPresentation().setEnabledAndVisible(file.getName().equals(FuDocConstants.FU_DOC_FILE + JsonFileType.DEFAULT_EXTENSION));
                }
            } else {
                e.getPresentation().setEnabledAndVisible(false);
            }
        }
        super.update(e);
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
