package com.wdf.fudoc.test.action.editor;

import com.intellij.openapi.editor.toolbar.floating.AbstractFloatingToolbarProvider;

/**
 * @author wangdingfu
 * @date 2023-07-26 16:37:20
 */
public class FuEditorFloatingToolbarProvider extends AbstractFloatingToolbarProvider {

    public FuEditorFloatingToolbarProvider() {
        super("fu.doc.editor.format");
    }

    @Override
    public boolean getAutoHideable() {
        return true;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
