package com.wdf.fudoc.test.action.editor;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.InspectionWidgetActionProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author wangdingfu
 * @date 2022-09-08 21:09:36
 */
public class FormatActionProvider  implements InspectionWidgetActionProvider {
    @Nullable
    @Override
    public AnAction createAction(@NotNull Editor editor) {
        return (FuEditorFormatAction) ActionManager.getInstance().getAction("fu.doc.format");
    }
}
